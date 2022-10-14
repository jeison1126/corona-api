package corona.financiero.nmda.admision.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import corona.financiero.nmda.admision.entity.ParDiaPagoEntity;
import corona.financiero.nmda.admision.repository.ParDiaPagoRepository;


@Service
public class DiaPagoService {

    @Autowired
    private ParDiaPagoRepository parDiaPagoRepository;
    
    public List<Long> listarDiasPagoVigentes() {

        List<ParDiaPagoEntity> diasPagoEntityVigentes = parDiaPagoRepository.findAllByVigenciaIsTrueOrderByDiaPagoId();
        
        List<Long> diasPago = new ArrayList<>();

        if (!diasPagoEntityVigentes.isEmpty()) {
        	diasPago = diasPagoEntityVigentes.stream().map(mr -> mr.getDiaPagoId())
        		.collect(Collectors.toList());
        }
        return diasPago;

    }
}
