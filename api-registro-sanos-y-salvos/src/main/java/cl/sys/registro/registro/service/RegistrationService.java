package cl.sys.registro.registro.service;

import cl.sys.registro.registro.dto.RegisterRequest;
import cl.sys.registro.registro.dto.RegisterResponse;
import cl.sys.registro.registro.factory.RegistrationHandlerFactory;
import cl.sys.registro.registro.model.Profile;
import cl.sys.registro.registro.repository.RegisterRepository;
import cl.sys.registro.registro.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegisterRepository registerRepository;
    private final RegistrationHandlerFactory factory;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PlatformTransactionManager transactionManager;

    public RegisterResponse register(RegisterRequest request) {
        String role = request.getUserType().equalsIgnoreCase("persona") ? "PERSONA" : "INSTITUCION";
        UUID userId = UUID.randomUUID();

        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            Profile profile = new Profile();
            profile.setId(userId);
            profile.setEmail(request.getEmail());
            profile.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            profile.setRole(role);
            profile.setUserType(request.getUserType());
            profile.setPhone(request.getPhone());
            registerRepository.save(profile);

            factory.getHandler(request.getUserType()).handle(request, userId);
        });

        String token = jwtService.generateToken(userId, role, request.getUserType(), request.getPhone(), request.getEmail());

        return RegisterResponse.builder()
                .status(201)
                .token(token)
                .message("Usuario registrado exitosamente")
                .build();
    }
}
