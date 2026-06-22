package com.sanosysalvos.notification.repository;

import com.sanosysalvos.notification.model.Notificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Query("SELECT n FROM Notificacion n WHERE n.id_usuario_reporte_perdida = :idUsuario ORDER BY n.fecha_creacion DESC")
    List<Notificacion> findByIdUsuarioReportePerdida(@Param("idUsuario") String idUsuario);
}