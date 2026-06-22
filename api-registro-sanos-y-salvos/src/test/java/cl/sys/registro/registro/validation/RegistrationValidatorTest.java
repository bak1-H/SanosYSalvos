package cl.sys.registro.registro.validation;

import cl.sys.registro.registro.dto.RegisterRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RegistrationValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    private AutoCloseable mocks;
    private RegistrationValidator validator;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        validator = new RegistrationValidator();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    private RegisterRequest baseRequest(String userType) {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.cl");
        request.setPassword("clave123");
        request.setPhone(56912345678L);
        request.setUserType(userType);
        return request;
    }

    @Test
    void persona_conNombreYApellido_esValida() {
        RegisterRequest request = baseRequest("persona");
        request.setName("Daniel");
        request.setLastName("Beltran");

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void persona_sinApellido_noEsValida() {
        RegisterRequest request = baseRequest("persona");
        request.setName("Daniel");

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void clinica_conNombreYDireccion_esValida() {
        RegisterRequest request = baseRequest("clinica");
        request.setClinicaName("Clinica Sur");
        request.setAddress("Calle 123");

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void refugio_conNombreYDireccion_esValida() {
        RegisterRequest request = baseRequest("refugio");
        request.setRefugioName("Refugio Patitas");
        request.setAdress("Calle 456");

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void tipoUsuarioNoSoportado_noEsValida() {
        RegisterRequest request = baseRequest("alien");

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void sinEmail_noEsValida() {
        RegisterRequest request = baseRequest("persona");
        request.setEmail(" ");
        request.setName("Daniel");
        request.setLastName("Beltran");

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void sinTelefono_noEsValida() {
        RegisterRequest request = baseRequest("persona");
        request.setPhone(null);
        request.setName("Daniel");
        request.setLastName("Beltran");

        assertFalse(validator.isValid(request, context));
    }

    @Test
    void requestNulo_noEsValida() {
        assertFalse(validator.isValid(null, context));
    }
}
