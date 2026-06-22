package cl.sys.registro.registro.service;

import cl.sys.registro.registro.dto.LoginRequest;
import cl.sys.registro.registro.dto.LoginResponse;
import cl.sys.registro.registro.model.Profile;
import cl.sys.registro.registro.repository.RegisterRepository;
import cl.sys.registro.registro.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AutoCloseable mocks;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(registerRepository, passwordEncoder, jwtService);
    }

    @Test
    void login_caminoFeliz_retornaTokenYDatosDePerfil() {
        UUID id = UUID.randomUUID();
        Profile profile = new Profile();
        profile.setId(id);
        profile.setEmail("user@test.cl");
        profile.setPasswordHash("hash");
        profile.setRole("PERSONA");
        profile.setUserType("persona");
        profile.setPhone(56911111111L);

        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.cl");
        request.setPassword("clave123");

        when(registerRepository.findByEmail("user@test.cl")).thenReturn(Optional.of(profile));
        when(passwordEncoder.matches("clave123", "hash")).thenReturn(true);
        when(jwtService.generateToken(id, "PERSONA", "persona", 56911111111L, "user@test.cl"))
                .thenReturn("jwt-token");

        LoginResponse response = authenticationService.login(request);

        assertEquals(200, response.getStatus());
        assertEquals("jwt-token", response.getToken());
        assertEquals("user@test.cl", response.getEmail());
        assertEquals("PERSONA", response.getRole());
    }

    @Test
    void login_emailNoExiste_lanzaBadCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("noexiste@test.cl");
        request.setPassword("clave123");

        when(registerRepository.findByEmail("noexiste@test.cl")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(request));
    }

    @Test
    void login_passwordIncorrecta_lanzaBadCredentials() {
        Profile profile = new Profile();
        profile.setEmail("user@test.cl");
        profile.setPasswordHash("hash");

        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.cl");
        request.setPassword("claveMala");

        when(registerRepository.findByEmail("user@test.cl")).thenReturn(Optional.of(profile));
        when(passwordEncoder.matches("claveMala", "hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(request));
    }
}
