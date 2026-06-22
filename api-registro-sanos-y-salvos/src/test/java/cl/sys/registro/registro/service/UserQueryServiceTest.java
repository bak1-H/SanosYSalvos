package cl.sys.registro.registro.service;

import cl.sys.registro.registro.dto.NombreUsuarioResponse;
import cl.sys.registro.registro.model.Clinica;
import cl.sys.registro.registro.model.Persona;
import cl.sys.registro.registro.model.Profile;
import cl.sys.registro.registro.model.Refugio;
import cl.sys.registro.registro.repository.ClinicaRepository;
import cl.sys.registro.registro.repository.PersonaRepository;
import cl.sys.registro.registro.repository.RefugioRepository;
import cl.sys.registro.registro.repository.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserQueryServiceTest {

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private ClinicaRepository clinicaRepository;

    @Mock
    private RefugioRepository refugioRepository;

    private AutoCloseable mocks;
    private UserQueryService userQueryService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        userQueryService = new UserQueryService(registerRepository, personaRepository, clinicaRepository, refugioRepository);
    }

    @Test
    void getNombreById_persona_concatenaNombreYApellido() {
        UUID id = UUID.randomUUID();
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUserType("persona");
        when(registerRepository.findById(id)).thenReturn(Optional.of(profile));

        Persona persona = new Persona();
        persona.setName("Daniel");
        persona.setLastName("Beltran");
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));

        NombreUsuarioResponse response = userQueryService.getNombreById(id);

        assertEquals("Daniel Beltran", response.getNombre());
    }

    @Test
    void getNombreById_clinica_devuelveClinicaName() {
        UUID id = UUID.randomUUID();
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUserType("clinica");
        when(registerRepository.findById(id)).thenReturn(Optional.of(profile));

        Clinica clinica = new Clinica();
        clinica.setClinicaName("Clinica Veterinaria Sur");
        when(clinicaRepository.findById(id)).thenReturn(Optional.of(clinica));

        NombreUsuarioResponse response = userQueryService.getNombreById(id);

        assertEquals("Clinica Veterinaria Sur", response.getNombre());
    }

    @Test
    void getNombreById_refugio_devuelveRefugioName() {
        UUID id = UUID.randomUUID();
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUserType("refugio");
        when(registerRepository.findById(id)).thenReturn(Optional.of(profile));

        Refugio refugio = new Refugio();
        refugio.setRefugioName("Refugio Patitas");
        when(refugioRepository.findById(id)).thenReturn(Optional.of(refugio));

        NombreUsuarioResponse response = userQueryService.getNombreById(id);

        assertEquals("Refugio Patitas", response.getNombre());
    }

    @Test
    void getNombreById_perfilInexistente_lanzaNoSuchElement() {
        UUID id = UUID.randomUUID();
        when(registerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userQueryService.getNombreById(id));
    }
}
