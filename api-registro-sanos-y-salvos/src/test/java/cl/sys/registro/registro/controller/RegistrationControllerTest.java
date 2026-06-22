package cl.sys.registro.registro.controller;

import cl.sys.registro.registro.dto.NombreUsuarioResponse;
import cl.sys.registro.registro.dto.RegisterRequest;
import cl.sys.registro.registro.dto.RegisterResponse;
import cl.sys.registro.registro.dto.UserProfileResponse;
import cl.sys.registro.registro.dto.UserQueryResponse;
import cl.sys.registro.registro.service.RegistrationService;
import cl.sys.registro.registro.service.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserQueryService userQueryService;

    private AutoCloseable mocks;
    private RegistrationController controller;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new RegistrationController(registrationService, userQueryService);
    }

    @Test
    void register_caminoFeliz_retorna201() {
        RegisterRequest request = new RegisterRequest();
        RegisterResponse expected = RegisterResponse.builder().status(201).token("jwt").message("ok").build();
        when(registrationService.register(request)).thenReturn(expected);

        ResponseEntity<RegisterResponse> response = controller.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("jwt", response.getBody().getToken());
    }

    @Test
    void register_servicioFalla_retorna500() {
        RegisterRequest request = new RegisterRequest();
        when(registrationService.register(request)).thenThrow(new RuntimeException("DB caida"));

        ResponseEntity<RegisterResponse> response = controller.register(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("DB caida", response.getBody().getError());
    }

    @Test
    void getUser_existente_retorna200() {
        UUID id = UUID.randomUUID();
        UserProfileResponse profile = UserProfileResponse.builder().id(id).build();
        when(userQueryService.getById(id)).thenReturn(profile);

        ResponseEntity<UserQueryResponse> response = controller.getUser(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profile, response.getBody().getUser());
    }

    @Test
    void getUser_inexistente_retorna404() {
        UUID id = UUID.randomUUID();
        when(userQueryService.getById(id)).thenThrow(new NoSuchElementException("no existe"));

        ResponseEntity<UserQueryResponse> response = controller.getUser(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getNombre_existente_retorna200() {
        UUID id = UUID.randomUUID();
        NombreUsuarioResponse nombre = NombreUsuarioResponse.builder().nombre("Daniel Beltran").build();
        when(userQueryService.getNombreById(id)).thenReturn(nombre);

        ResponseEntity<NombreUsuarioResponse> response = controller.getNombre(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Daniel Beltran", response.getBody().getNombre());
    }

    @Test
    void getNombre_inexistente_retorna404() {
        UUID id = UUID.randomUUID();
        when(userQueryService.getNombreById(id)).thenThrow(new NoSuchElementException("no existe"));

        ResponseEntity<NombreUsuarioResponse> response = controller.getNombre(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleValidationErrors_retorna400ConErroresConcatenados() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("registerRequest", "email", "El campo email es obligatorio");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<RegisterResponse> response = controller.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("email: El campo email es obligatorio", response.getBody().getError());
    }
}
