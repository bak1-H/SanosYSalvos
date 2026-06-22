package cl.sys.registro.registro.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private int status;
    private String token;
    private String message;
    private String error;
    private String email;
    private String role;
    private String userType;
    private Long phone;
}
