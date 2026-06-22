package cl.sys.registro.registro.service;

import cl.sys.registro.registro.dto.LoginRequest;
import cl.sys.registro.registro.dto.LoginResponse;
import cl.sys.registro.registro.model.Profile;
import cl.sys.registro.registro.repository.RegisterRepository;
import cl.sys.registro.registro.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        Profile profile = registerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), profile.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(profile.getId(), profile.getRole(), profile.getUserType(), profile.getPhone(), profile.getEmail());

        return LoginResponse.builder()
                .status(200)
                .token(token)
                .message("Login exitoso")
                .email(profile.getEmail())
                .role(profile.getRole())
                .userType(profile.getUserType())
                .phone(profile.getPhone())
                .build();
    }
}
