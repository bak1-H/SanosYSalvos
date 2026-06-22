package cl.sys.bff.bfffsanys.model;

import lombok.Data;

@Data
public class UserMetadataDTO {
    private String email;
    private String role;
    private String phone;
    private String userType;
}
