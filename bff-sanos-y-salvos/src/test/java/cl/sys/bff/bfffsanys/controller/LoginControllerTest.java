package cl.sys.bff.bfffsanys.controller;

import cl.sys.bff.bfffsanys.model.LoginRequestDTO;
import cl.sys.bff.bfffsanys.model.LoginResponseDTO;
import cl.sys.bff.bfffsanys.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Mock
    private LoginService loginService;

    private AutoCloseable mocks;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new LoginController(loginService);
    }

    @Test
    void login_retorna200ConElCuerpoDelServicio() {
        LoginRequestDTO request = new LoginRequestDTO();
        LoginResponseDTO expected = new LoginResponseDTO();
        expected.setStatus(200);
        expected.setAccessToken("jwt-token");
        when(loginService.login(request)).thenReturn(expected);

        ResponseEntity<LoginResponseDTO> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().getAccessToken());
    }
}
