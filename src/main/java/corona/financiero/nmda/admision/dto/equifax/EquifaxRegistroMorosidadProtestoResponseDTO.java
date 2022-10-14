package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxRegistroMorosidadProtestoResponseDTO {

    private String cantidadDocumentosBoletinProtestosEImpagos;
    private String cantidadDocumentosICOM;
    private String cantidadImpagosInformados;
    private String cantidadMorososComercio;
    private String cantidadMultasEInfraccionesEnLaboralYPrevisional;
    private String montoTotalImpago;

    private EquifaxMorosidadesBEDResponseDTO morosidadesBED;
    private EquifaxMorosidadesBolabResponseDTO morosidadesBOLAB;
    private EquifaxMorosidadesBolcomResponseDTO morosidadesBOLCOM;
    private EquifaxMorosidadesIcomResponseDTO morosidadesICOM;
    private EquifaxMercadosReponseDTO porMercados;
}
