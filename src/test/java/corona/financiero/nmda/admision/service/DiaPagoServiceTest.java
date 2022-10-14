package corona.financiero.nmda.admision.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import corona.financiero.nmda.admision.entity.ParDiaPagoEntity;
import corona.financiero.nmda.admision.repository.ParDiaPagoRepository;

@ExtendWith(MockitoExtension.class)
class DiaPagoServiceTest {
    @Mock
    private ParDiaPagoRepository parDiaPagoRepository;
    
    @InjectMocks
    private DiaPagoService diaPagoService;

    @BeforeEach
    public void initEach() {
    }


    @Test
    void listarDiasPagoVigentesTest() {
    	
        List<ParDiaPagoEntity> lista = new ArrayList<>();
        lista.add(datosDiaPagoEntity());
        
        when(parDiaPagoRepository.findAllByVigenciaIsTrueOrderByDiaPagoId()).thenReturn(lista);

        diaPagoService.listarDiasPagoVigentes();

    }

    @Test
    void listarDiasPagoVigentesTestCuandoNoExistenDiasPago() {
    	
        List<ParDiaPagoEntity> lista = new ArrayList<ParDiaPagoEntity>();
        when(parDiaPagoRepository.findAllByVigenciaIsTrueOrderByDiaPagoId()).thenReturn(lista);

        diaPagoService.listarDiasPagoVigentes();

    }
    
    private ParDiaPagoEntity datosDiaPagoEntity() {
        ParDiaPagoEntity parDiaPagoEntity = new ParDiaPagoEntity();
        parDiaPagoEntity.setDiaPagoId(1);
        parDiaPagoEntity.setVigencia(true);
        return parDiaPagoEntity;
    }
}
