package cl.sys.bff.bfffsanys.service.impl;

import cl.sys.bff.bfffsanys.client.RegistroClient;
import cl.sys.bff.bfffsanys.model.RegisterRequestDTO;
import cl.sys.bff.bfffsanys.model.RegisterResponseDTO;
import cl.sys.bff.bfffsanys.model.UsuarioResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RegistroServiceImplTest {

    @Mock
    private RegistroClient registroClient;

    private AutoCloseable mocks;
    private RegistroServiceImpl registroService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        registroService = new RegistroServiceImpl(registroClient);
    }

    @Test
    void registrar_delegaEnElClienteYRetornaSuRespuesta() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        RegisterResponseDTO expected = new RegisterResponseDTO();
        expected.setStatus(201);
        expected.setToken("jwt-token");
        when(registroClient.registrar(request)).thenReturn(expected);

        RegisterResponseDTO response = registroService.registrar(request);

        assertEquals(expected, response);
    }

    @Test
    void getUsuario_delegaEnElClienteConIdYAuthorization() {
        UsuarioResponseDTO expected = new UsuarioResponseDTO();
        expected.setStatus(200);
        when(registroClient.getUsuario("user-1", "Bearer jwt")).thenReturn(expected);

        UsuarioResponseDTO response = registroService.getUsuario("user-1", "Bearer jwt");

        assertEquals(expected, response);
    }
}
