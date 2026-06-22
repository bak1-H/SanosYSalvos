package cl.sys.registro.registro.factory;

import cl.sys.registro.registro.dto.RegisterRequest;
import cl.sys.registro.registro.model.Clinica;
import cl.sys.registro.registro.repository.ClinicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class ClinicaHandlerTest {

    @Mock
    private ClinicaRepository clinicaRepository;

    private AutoCloseable mocks;
    private ClinicaHandler handler;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        handler = new ClinicaHandler(clinicaRepository);
    }

    @Test
    void handle_guardaClinicaConDatosDelRequest() {
        UUID id = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest();
        request.setClinicaName("Clinica Veterinaria Sur");
        request.setAddress("Calle 123");

        handler.handle(request, id);

        ArgumentCaptor<Clinica> captor = ArgumentCaptor.forClass(Clinica.class);
        verify(clinicaRepository).save(captor.capture());
        assertEquals(id, captor.getValue().getId());
        assertEquals("Clinica Veterinaria Sur", captor.getValue().getClinicaName());
        assertEquals("Calle 123", captor.getValue().getAddress());
    }

    @Test
    void getUserType_retornaClinica() {
        assertEquals("clinica", handler.getUserType());
    }
}
