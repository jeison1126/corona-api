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

import corona.financiero.nmda.admision.entity.ParMotivoRechazoEntity;
import corona.financiero.nmda.admision.repository.ParMotivoRechazoRepository;

@ExtendWith(MockitoExtension.class)
class MotivoRechazoServiceTest {
    @Mock
    private ParMotivoRechazoRepository parMotivoRechazoRepository;
    
    @InjectMocks
    private MotivoRechazoService motivoRechazoService;

    @BeforeEach
    public void initEach() {
    }


    @Test
    void listarMotivosRechazoVigentes() {
    	
        List<ParMotivoRechazoEntity> lista = new ArrayList<>();
        lista.add(datosParMotivoRechazoEntity());
        
        when(parMotivoRechazoRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(lista);

        motivoRechazoService.listarMotivosRechazoVigentes();

    }

    @Test
    void listarMotivosRechazoVigentesCuandoNoExistenMNotivosRechazo() {
    	
        List<ParMotivoRechazoEntity> lista = new ArrayList<ParMotivoRechazoEntity>();
        when(parMotivoRechazoRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(lista);

        motivoRechazoService.listarMotivosRechazoVigentes();

    }
    
    private ParMotivoRechazoEntity datosParMotivoRechazoEntity() {
    	ParMotivoRechazoEntity parMotivoRechazoEntity = new ParMotivoRechazoEntity();
    	parMotivoRechazoEntity.setDescripcion("Motivo1");
    	parMotivoRechazoEntity.setVigencia(true);
        return parMotivoRechazoEntity;
    }
}
