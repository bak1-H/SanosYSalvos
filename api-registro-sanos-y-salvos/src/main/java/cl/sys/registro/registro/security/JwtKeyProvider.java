package cl.sys.registro.registro.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Owns the RSA key pair this service uses to sign registration JWTs.
 * Persisted to disk so tokens survive a container restart/redeploy.
 */
@Component
public class JwtKeyProvider {

    private final Path keysPath;
    private KeyPair keyPair;

    public JwtKeyProvider(@Value("${jwt.keys-path}") String keysPath) {
        this.keysPath = Path.of(keysPath);
    }

    @PostConstruct
    void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (Files.exists(keysPath)) {
            keyPair = loadKeyPair();
        } else {
            keyPair = generateKeyPair();
            saveKeyPair(keyPair);
        }
    }

    public RSAPublicKey publicKey() {
        return (RSAPublicKey) keyPair.getPublic();
    }

    public RSAPrivateKey privateKey() {
        return (RSAPrivateKey) keyPair.getPrivate();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    private void saveKeyPair(KeyPair pair) throws IOException {
        if (keysPath.getParent() != null) {
            Files.createDirectories(keysPath.getParent());
        }
        String encoded = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded())
                + System.lineSeparator()
                + Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        Files.writeString(keysPath, encoded);
    }

    private KeyPair loadKeyPair() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var lines = Files.readAllLines(keysPath);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        var privateKey = factory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(lines.get(0))));
        var publicKey = factory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(lines.get(1))));

        return new KeyPair(publicKey, privateKey);
    }
}
