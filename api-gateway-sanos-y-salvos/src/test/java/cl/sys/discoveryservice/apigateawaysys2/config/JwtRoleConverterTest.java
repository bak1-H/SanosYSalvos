package cl.sys.discoveryservice.apigateawaysys2.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtRoleConverterTest {

    private final JwtRoleConverter converter = new JwtRoleConverter();

    @Test
    void convert_conRolPresente_agregaAutoridadConPrefijoRole() {
        Jwt jwt = jwtConClaims(Map.of("role", "ADMIN"));

        JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);

        assertEquals(1, token.getAuthorities().size());
        assertEquals("ROLE_ADMIN", token.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void convert_sinClaimRole_noOtorgaAutoridades() {
        Jwt jwt = jwtConClaims(Map.of());

        JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);

        assertTrue(token.getAuthorities().isEmpty());
    }

    private Jwt jwtConClaims(Map<String, Object> claims) {
        return Jwt.withTokenValue("token-de-prueba")
                .header("alg", "RS256")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claims(map -> map.putAll(claims))
                .claim("sub", "user-1")
                .build();
    }
}
