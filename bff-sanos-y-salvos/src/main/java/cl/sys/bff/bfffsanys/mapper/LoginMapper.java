package cl.sys.bff.bfffsanys.mapper;

import cl.sys.bff.bfffsanys.model.LoginRegistroResponseDTO;
import cl.sys.bff.bfffsanys.model.LoginResponseDTO;
import cl.sys.bff.bfffsanys.model.UserMetadataDTO;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    private static final int TOKEN_EXPIRATION_SECONDS = 3600;

    public LoginResponseDTO toLoginResponse(LoginRegistroResponseDTO registroResponse) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setStatus(200);
        response.setAccessToken(registroResponse.getToken());
        response.setExpiresIn(TOKEN_EXPIRATION_SECONDS);
        response.setRefreshToken(null);

        UserMetadataDTO user = new UserMetadataDTO();
        user.setEmail(registroResponse.getEmail());
        user.setRole(registroResponse.getRole());
        user.setUserType(registroResponse.getUserType());
        user.setPhone(registroResponse.getPhone() != null ? registroResponse.getPhone().toString() : null);
        response.setUser(user);

        return response;
    }
}
