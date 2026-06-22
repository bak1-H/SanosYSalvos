package cl.sys.bff.bfffsanys.mapper;

import cl.sys.bff.bfffsanys.model.LoginRegistroResponseDTO;
import cl.sys.bff.bfffsanys.model.LoginResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoginMapperTest {

    private final LoginMapper mapper = new LoginMapper();

    @Test
    void toLoginResponse_conTelefono_mapeaTodosLosCampos() {
        LoginRegistroResponseDTO registroResponse = new LoginRegistroResponseDTO();
        registroResponse.setToken("jwt-token");
        registroResponse.setEmail("user@test.cl");
        registroResponse.setRole("PERSONA");
        registroResponse.setUserType("persona");
        registroResponse.setPhone(56912345678L);

        LoginResponseDTO response = mapper.toLoginResponse(registroResponse);

        assertEquals(200, response.getStatus());
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals(3600, response.getExpiresIn());
        assertNull(response.getRefreshToken());
        assertEquals("user@test.cl", response.getUser().getEmail());
        assertEquals("PERSONA", response.getUser().getRole());
        assertEquals("persona", response.getUser().getUserType());
        assertEquals("56912345678", response.getUser().getPhone());
    }

    @Test
    void toLoginResponse_sinTelefono_dejaPhoneNulo() {
        LoginRegistroResponseDTO registroResponse = new LoginRegistroResponseDTO();
        registroResponse.setToken("jwt-token");
        registroResponse.setPhone(null);

        LoginResponseDTO response = mapper.toLoginResponse(registroResponse);

        assertNull(response.getUser().getPhone());
    }
}
