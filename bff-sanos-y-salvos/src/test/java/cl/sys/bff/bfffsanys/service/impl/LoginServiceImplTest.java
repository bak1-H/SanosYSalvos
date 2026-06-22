package cl.sys.bff.bfffsanys.service.impl;

import cl.sys.bff.bfffsanys.client.RegistroClient;
import cl.sys.bff.bfffsanys.mapper.LoginMapper;
import cl.sys.bff.bfffsanys.model.LoginRegistroResponseDTO;
import cl.sys.bff.bfffsanys.model.LoginRequestDTO;
import cl.sys.bff.bfffsanys.model.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginServiceImplTest {

    @Mock
    private RegistroClient registroClient;

    @Mock
    private LoginMapper loginMapper;

    private AutoCloseable mocks;
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        loginService = new LoginServiceImpl(registroClient, loginMapper);
    }

    @Test
    void login_delegaEnClienteYMapeaLaRespuesta() {
        LoginRequestDTO request = new LoginRequestDTO();
        LoginRegistroResponseDTO registroResponse = new LoginRegistroResponseDTO();
        LoginResponseDTO mapped = new LoginResponseDTO();
        mapped.setStatus(200);
        mapped.setAccessToken("jwt-token");

        when(registroClient.login(request)).thenReturn(registroResponse);
        when(loginMapper.toLoginResponse(registroResponse)).thenReturn(mapped);

        LoginResponseDTO response = loginService.login(request);

        assertEquals(200, response.getStatus());
        assertEquals("jwt-token", response.getAccessToken());
    }
}
