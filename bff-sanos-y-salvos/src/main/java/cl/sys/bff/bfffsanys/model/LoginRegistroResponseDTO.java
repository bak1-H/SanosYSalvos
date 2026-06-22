package cl.sys.bff.bfffsanys.model;

import lombok.Data;

@Data
public class LoginRegistroResponseDTO {
    private Integer status;
    private String token;
    private String message;
    private String error;
    private String email;
    private String role;
    private String userType;
    private Long phone;
}
