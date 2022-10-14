package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudAdmisionRepository extends JpaRepository<SolicitudAdmisionEntity, Long> {
    SolicitudAdmisionEntity findByProspectoEntityProspectoIdAndVigenciaIsTrue(long prospectoId);

    List<SolicitudAdmisionEntity> findAllByProspectoEntityAndVigenciaIsTrue(ProspectoEntity prospectoEntity);

    @Query("select s from SolicitudAdmisionEntity s join s.parCanalEntity pc join s.prospectoEntity p join s.parEstadoSolicitudEntity pes where s.solicitudId = :solicitudAdmisionId and p.rut = :rutProspecto")
    Optional<SolicitudAdmisionEntity> detalleSolicitudAdmision(long solicitudAdmisionId, String rutProspecto);

    @Query("select sa from SolicitudAdmisionEntity sa where sa.solicitudId = :solicitudId and sa.vigencia is true and sa.prospectoEntity.rut = :rut and sa.prospectoEntity.vigencia is true")
    Optional<SolicitudAdmisionEntity> recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(long solicitudId, String rut);

    @Query("select sa from SolicitudAdmisionEntity sa where sa.vigencia is true and (sa.parEstadoSolicitudEntity.estadoSolicitudId = 1 or sa.parEstadoSolicitudEntity.estadoSolicitudId = 2) and sa.prospectoEntity.prospectoId = ?1")
    SolicitudAdmisionEntity obtenerSolicitudActivaYVigente(long prospectoId);

    @Query("select sa from SolicitudAdmisionEntity sa where sa.solicitudId = :solicitudId and now() between sa.fechaSolicitud and :hasta")
    SolicitudAdmisionEntity validarFecha(long solicitudId, Date hasta);

    @Query("select sa from SolicitudAdmisionEntity sa join sa.prospectoEntity p where lower(p.rut) = lower(:rut) and sa.fechaSolicitud between :desde and :now")
    List<SolicitudAdmisionEntity> verificarIntentosAnterioresXDias(String rut, LocalDate desde, LocalDate now);

    SolicitudAdmisionEntity findByProspectoEntityRutAndVigenciaIsTrue(String rut);
}
