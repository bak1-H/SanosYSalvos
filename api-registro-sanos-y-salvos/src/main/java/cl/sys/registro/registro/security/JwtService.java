package cl.sys.registro.registro.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final long EXPIRATION_SECONDS = 3600;

    private final JwtKeyProvider keyProvider;

    public String generateToken(UUID userId, String role, String userType, Long phone, String email) {
        Instant now = Instant.now();

        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .claim("role", role)
                .claim("user_type", userType)
                .claim("email", email)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(EXPIRATION_SECONDS)));

        if (phone != null) {
            claims.claim("phone", phone);
        }

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claims.build()
        );

        try {
            signedJWT.sign(new RSASSASigner(keyProvider.privateKey()));
        } catch (JOSEException e) {
            throw new IllegalStateException("No se pudo firmar el JWT", e);
        }

        return signedJWT.serialize();
    }
}
