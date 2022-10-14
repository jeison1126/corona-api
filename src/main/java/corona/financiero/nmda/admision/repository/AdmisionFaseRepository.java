package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.AdmisionFaseEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmisionFaseRepository extends JpaRepository<AdmisionFaseEntity, Long> {
    List<AdmisionFaseEntity> findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(SolicitudAdmisionEntity solicitudAdmisionEntity);

    Page<AdmisionFaseEntity> findAllBySolicitudAdmisionEntity(SolicitudAdmisionEntity solicitudAdmisionEntity, Pageable pageable);

    @Query("select af FROM AdmisionFaseEntity af join af.solicitudAdmisionEntity sa join sa.prospectoEntity p join af.parFaseEntity pf join sa.parEstadoSolicitudEntity pes where sa.vigencia = true and pf.faseId = :fase and lower(p.rut) = lower(:rutFormateado) and pes.estadoSolicitudId = 1 and sa.solicitudId = :solicitudAdmisionId")
    Optional<AdmisionFaseEntity> validaSolicitudAdmisionYFaseActiva(String rutFormateado, long solicitudAdmisionId, long fase);

    @Query("select af FROM AdmisionFaseEntity af join af.solicitudAdmisionEntity sa join sa.prospectoEntity p join af.parFaseEntity pf join sa.parEstadoSolicitudEntity pes where sa.vigencia = true and pf.faseId = :fase and pes.estadoSolicitudId = 1 and sa.solicitudId = :solicitudAdmisionId")
    Optional<AdmisionFaseEntity> validaSolicitudAdmisionYFaseActivaExcepcion(long solicitudAdmisionId, long fase);


}
