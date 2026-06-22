package cl.sys.bff.bfffsanys.exception;

import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleMicroservicioException_propagaStatusYBodyOriginal() {
        Map<String, Object> body = Map.of("error", "Tipo de usuario no soportado");
        MicroservicioException ex = new MicroservicioException(400, body);

        ResponseEntity<Object> response = handler.handleMicroservicioException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(body, response.getBody());
    }

    @Test
    void handleConexionException_retorna503ConMensajeGenerico() {
        Request request = Request.create(
                Request.HttpMethod.GET, "/reportes", Map.of(), (byte[]) null, java.nio.charset.StandardCharsets.UTF_8);
        RetryableException ex = new RetryableException(
                503, "timeout", Request.HttpMethod.GET, (Long) null, request);

        ResponseEntity<Object> response = handler.handleConexionException(ex);

        assertEquals(503, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Servicio no disponible", body.get("error"));
        assertNull(body.get("token"));
    }
}
