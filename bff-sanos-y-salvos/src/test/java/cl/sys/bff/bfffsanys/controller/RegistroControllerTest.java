package cl.sys.bff.bfffsanys.controller;

import cl.sys.bff.bfffsanys.model.RegisterRequestDTO;
import cl.sys.bff.bfffsanys.model.RegisterResponseDTO;
import cl.sys.bff.bfffsanys.model.UsuarioResponseDTO;
import cl.sys.bff.bfffsanys.service.RegistroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RegistroControllerTest {

    @Mock
    private RegistroService registroService;

    private AutoCloseable mocks;
    private RegistroController controller;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new RegistroController(registroService);
    }

    @Test
    void registrar_retorna201ConElCuerpoDelServicio() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        RegisterResponseDTO expected = new RegisterResponseDTO();
        expected.setStatus(201);
        expected.setToken("jwt-token");
        when(registroService.registrar(request)).thenReturn(expected);

        ResponseEntity<RegisterResponseDTO> response = controller.registrar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().getToken());
    }

    @Test
    void getUsuario_retorna200ConElCuerpoDelServicio() {
        UsuarioResponseDTO expected = new UsuarioResponseDTO();
        expected.setStatus(200);
        when(registroService.getUsuario("user-1", "Bearer jwt")).thenReturn(expected);

        ResponseEntity<UsuarioResponseDTO> response = controller.getUsuario("user-1", "Bearer jwt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}
