package cl.sys.registro.registro.controller;

import cl.sys.registro.registro.dto.LoginRequest;
import cl.sys.registro.registro.dto.LoginResponse;
import cl.sys.registro.registro.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    private AutoCloseable mocks;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new LoginController(authenticationService);
    }

    @Test
    void login_caminoFeliz_retorna200() {
        LoginRequest request = new LoginRequest();
        LoginResponse expected = LoginResponse.builder().status(200).token("jwt").build();
        when(authenticationService.login(request)).thenReturn(expected);

        ResponseEntity<LoginResponse> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt", response.getBody().getToken());
    }

    @Test
    void login_credencialesInvalidas_retorna401() {
        LoginRequest request = new LoginRequest();
        when(authenticationService.login(request)).thenThrow(new BadCredentialsException("Credenciales inválidas"));

        ResponseEntity<LoginResponse> response = controller.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
