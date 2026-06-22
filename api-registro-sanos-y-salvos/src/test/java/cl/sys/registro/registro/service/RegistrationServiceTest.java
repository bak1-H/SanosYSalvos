package cl.sys.registro.registro.service;

import cl.sys.registro.registro.dto.RegisterRequest;
import cl.sys.registro.registro.dto.RegisterResponse;
import cl.sys.registro.registro.factory.RegistrationHandler;
import cl.sys.registro.registro.factory.RegistrationHandlerFactory;
import cl.sys.registro.registro.model.Profile;
import cl.sys.registro.registro.repository.RegisterRepository;
import cl.sys.registro.registro.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private RegistrationHandlerFactory factory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private RegistrationHandler handler;

    private AutoCloseable mocks;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        registrationService = new RegistrationService(
                registerRepository, factory, passwordEncoder, jwtService, transactionManager);

        TransactionStatus status = mock(TransactionStatus.class);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(status);
    }

    @Test
    void registrarPersona_caminoFeliz_retornaTokenYGuardaProfileConRolPersona() {
        RegisterRequest request = new RegisterRequest();
        request.setUserType("persona");
        request.setEmail("persona@test.cl");
        request.setPassword("clave123");
        request.setPhone(56912345678L);

        when(passwordEncoder.encode("clave123")).thenReturn("hash-clave123");
        when(factory.getHandler("persona")).thenReturn(handler);
        when(jwtService.generateToken(any(UUID.class), eq("PERSONA"), eq("persona"), anyLong(), anyString()))
                .thenReturn("jwt-token");

        RegisterResponse response = registrationService.register(request);

        assertEquals(201, response.getStatus());
        assertEquals("jwt-token", response.getToken());

        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(registerRepository).save(profileCaptor.capture());
        Profile saved = profileCaptor.getValue();
        assertEquals("persona@test.cl", saved.getEmail());
        assertEquals("hash-clave123", saved.getPasswordHash());
        assertEquals("PERSONA", saved.getRole());
        assertEquals("persona", saved.getUserType());

        verify(handler).handle(request, saved.getId());
    }

    @Test
    void registrarClinica_asignaRolInstitucion() {
        RegisterRequest request = new RegisterRequest();
        request.setUserType("clinica");
        request.setEmail("clinica@test.cl");
        request.setPassword("clave456");

        when(passwordEncoder.encode("clave456")).thenReturn("hash-clave456");
        when(factory.getHandler("clinica")).thenReturn(handler);
        when(jwtService.generateToken(any(UUID.class), eq("INSTITUCION"), eq("clinica"), any(), anyString()))
                .thenReturn("jwt-token-clinica");

        RegisterResponse response = registrationService.register(request);

        assertEquals("jwt-token-clinica", response.getToken());

        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(registerRepository).save(profileCaptor.capture());
        assertEquals("INSTITUCION", profileCaptor.getValue().getRole());
    }

    @Test
    void registrarTipoNoSoportado_lanzaIllegalArgumentExceptionYNoGeneraToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUserType("alien");
        request.setEmail("alien@test.cl");
        request.setPassword("clave789");

        when(passwordEncoder.encode("clave789")).thenReturn("hash-clave789");
        when(factory.getHandler("alien")).thenThrow(new IllegalArgumentException("Tipo de usuario no soportado: alien"));

        assertThrows(IllegalArgumentException.class, () -> registrationService.register(request));

        verify(jwtService, never()).generateToken(any(), any(), any(), any(), any());
    }

    @Test
    void registrarConFalloEnHandler_propagaExcepcionYNoGeneraToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUserType("refugio");
        request.setEmail("refugio@test.cl");
        request.setPassword("claveABC");

        when(passwordEncoder.encode("claveABC")).thenReturn("hash-claveABC");
        when(factory.getHandler("refugio")).thenReturn(handler);
        org.mockito.Mockito.doThrow(new RuntimeException("Violacion de constraint"))
                .when(handler).handle(any(RegisterRequest.class), any(UUID.class));

        assertThrows(RuntimeException.class, () -> registrationService.register(request));

        verify(jwtService, never()).generateToken(any(), any(), any(), any(), any());
    }
}
