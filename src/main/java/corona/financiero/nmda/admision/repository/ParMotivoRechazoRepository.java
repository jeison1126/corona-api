package corona.financiero.nmda.admision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import corona.financiero.nmda.admision.entity.ParMotivoRechazoEntity;

@Repository
public interface ParMotivoRechazoRepository extends JpaRepository<ParMotivoRechazoEntity, Long> {
	
    List<ParMotivoRechazoEntity> findAllByVigenciaIsTrueOrderByDescripcion();
    
    @Query("select mr from ParMotivoRechazoEntity mr where mr.motivoRechazoId = :motivoRechazoId and mr.vigencia is true")
    Optional<ParMotivoRechazoEntity> recuperarMotivoRechazoByMotivoRechazoId(long motivoRechazoId);

}
