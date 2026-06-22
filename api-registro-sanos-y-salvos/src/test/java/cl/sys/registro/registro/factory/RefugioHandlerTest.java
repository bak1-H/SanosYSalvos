package cl.sys.registro.registro.factory;

import cl.sys.registro.registro.dto.RegisterRequest;
import cl.sys.registro.registro.model.Refugio;
import cl.sys.registro.registro.repository.RefugioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class RefugioHandlerTest {

    @Mock
    private RefugioRepository refugioRepository;

    private AutoCloseable mocks;
    private RefugioHandler handler;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        handler = new RefugioHandler(refugioRepository);
    }

    @Test
    void handle_guardaRefugioConDatosDelRequest() {
        UUID id = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest();
        request.setRefugioName("Refugio Patitas");
        request.setAdress("Calle 456");

        handler.handle(request, id);

        ArgumentCaptor<Refugio> captor = ArgumentCaptor.forClass(Refugio.class);
        verify(refugioRepository).save(captor.capture());
        assertEquals(id, captor.getValue().getId());
        assertEquals("Refugio Patitas", captor.getValue().getRefugioName());
        assertEquals("Calle 456", captor.getValue().getAdress());
    }

    @Test
    void getUserType_retornaRefugio() {
        assertEquals("refugio", handler.getUserType());
    }
}
