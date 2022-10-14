package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxResumenMorosidadesProtestosImpagosResponseDTO {

    private boolean accesoCamaraComercio;
    private String cantidadDocumentos12A24Meses;
    private String cantidadDocumentos6A12Meses;
    private String cantidadDocumentosAcumulados12Meses;
    private String cantidadDocumentosAcumulados24Meses;
    private String cantidadDocumentosAcumuladosMasDe24Meses;
    private String cantidadDocumentosAcumuladosUltimos6Meses;
    private String cantidadDocumentosMasDe24Meses;
    private String cantidadDocumentosUltimos6Meses;
    private String cantidadTotalImpagos;
    private String fechaVencimientoUltimoImpago;
    private boolean indicadorErrorConectarseAlCSS;
    private boolean indicadorExistenciaInformacionParaSeccion;
    private String montoDocumentos12A24Meses;
    private String montoDocumentos6A12Meses;
    private String montoDocumentosMasDe24Meses;
    private String montoDocumentosUltimos6Meses;
    private String montoTotalImpagos;
    private String montoUltimoImpago;
    private String tipoDeudaUltimoImpago;
}
