package cl.sys.bff.bfffsanys.exception;

import feign.RetryableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MicroservicioException.class)
    public ResponseEntity<Object> handleMicroservicioException(MicroservicioException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getBody());
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Object> handleConexionException(RetryableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Servicio no disponible");
        body.put("message", "No se pudo conectar con el servicio destino");
        body.put("status", 503);
        body.put("token", null);
        return ResponseEntity.status(503).body(body);
    }
}
