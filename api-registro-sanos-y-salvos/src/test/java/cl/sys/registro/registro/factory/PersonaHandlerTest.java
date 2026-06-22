package cl.sys.registro.registro.factory;

import cl.sys.registro.registro.dto.RegisterRequest;
import cl.sys.registro.registro.model.Persona;
import cl.sys.registro.registro.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class PersonaHandlerTest {

    @Mock
    private PersonaRepository personaRepository;

    private AutoCloseable mocks;
    private PersonaHandler handler;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        handler = new PersonaHandler(personaRepository);
    }

    @Test
    void handle_guardaPersonaConDatosDelRequest() {
        UUID id = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest();
        request.setName("Daniel");
        request.setLastName("Beltran");

        handler.handle(request, id);

        ArgumentCaptor<Persona> captor = ArgumentCaptor.forClass(Persona.class);
        verify(personaRepository).save(captor.capture());
        assertEquals(id, captor.getValue().getId());
        assertEquals("Daniel", captor.getValue().getName());
        assertEquals("Beltran", captor.getValue().getLastName());
    }

    @Test
    void getUserType_retornaPersona() {
        assertEquals("persona", handler.getUserType());
    }
}
