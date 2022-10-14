package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ProductoCupoOfrecidoDTO;
import corona.financiero.nmda.admision.entity.EvaluacionProductoEntity;
import corona.financiero.nmda.admision.entity.ParTipoProductoEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.EvaluacionProductoRepository;
import corona.financiero.nmda.admision.repository.ParTipoProductoRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class EvaluacionProductoService {

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private EvaluacionProductoRepository evaluacionProductoRepository;

    @Autowired
    private ParTipoProductoRepository parTipoProductoRepository;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    public List<ProductoCupoOfrecidoDTO> listarProductosOfercidosBySolicitudIdAndRut(Long solicitudId, String rut) {

        validarRequest(solicitudId, rut);

        String rutFormateado = validaciones.formateaRutHaciaBD(rut);

        List<EvaluacionProductoEntity> listaEvaluacionProducto = evaluacionProductoRepository.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rutFormateado);

        List<ProductoCupoOfrecidoDTO> productosConCupoOfrecidos = new ArrayList<>();

        if (!listaEvaluacionProducto.isEmpty()) {
            productosConCupoOfrecidos = listaEvaluacionProducto.stream().map(ep -> {
                ProductoCupoOfrecidoDTO productoCupo = new ProductoCupoOfrecidoDTO();
                productoCupo.setEvaluacionProductoId(ep.getEvaluacionProductoId());
                productoCupo.setCupoAprobado(ep.getCupoAprobado());
                productoCupo.setDescripcionTipoProducto(ep.getParTipoProductoEntity().getDescripcion());
                productoCupo.setRecomendado(ep.isRecomendado());
                return productoCupo;
            }).collect(Collectors.toList());
        }
        return productosConCupoOfrecidos;

    }

    private void validarRequest(Long solicitudId, String rut) {

        if (solicitudId == null || solicitudId.longValue() <= 0) {
            throw new BadRequestException("El id de la solicitud es requerido");
        }

        validaciones.validacionGeneralRut(rut);

    }

    @Transactional
    public void cargaProductoCortesia(SolicitudAdmisionEntity solicitudAdmisionEntity) {

        List<EvaluacionProductoEntity> evaluacionesPrevias = evaluacionProductoRepository.findAllBySolicitudAdmisionEntityAndVigenciaIsTrue(solicitudAdmisionEntity);
        evaluacionesPrevias.stream().forEach(e -> e.setVigencia(false));

        evaluacionProductoRepository.saveAll(evaluacionesPrevias);
        evaluacionProductoRepository.flush();


        List<ParTipoProductoEntity> parTipoProductoEntities = parTipoProductoRepository.findAllByCortesiaIsTrueAndVigenciaIsTrue();

        ParTipoProductoEntity productoRecomendado = parTipoProductoRepository.obtenerProductoCortesiaRecomendado();

        List<EvaluacionProductoEntity> evaluacionProductoEntities = parTipoProductoEntities.stream().map(p -> {
            log.debug("Producto: {}", p.toString());
            EvaluacionProductoEntity evaluacionProductoEntity = new EvaluacionProductoEntity();
            evaluacionProductoEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
            evaluacionProductoEntity.setParTipoProductoEntity(p);
            if (p.getTipoProductoId() == productoRecomendado.getTipoProductoId()) {
                evaluacionProductoEntity.setRecomendado(true);
            }
            evaluacionProductoEntity.setCupoAprobado(p.getCupoCortesia());
            evaluacionProductoEntity.setVigencia(true);
            evaluacionProductoEntity.setFechaRegistro(LocalDateTime.now());
            evaluacionProductoEntity.setUsuarioRegistro(USUARIO_TEMPORAL);

            return evaluacionProductoEntity;
        }).collect(Collectors.toList());

        evaluacionProductoRepository.saveAll(evaluacionProductoEntities);
        evaluacionProductoRepository.flush();
    }

    @Transactional
    public void cargaProductosPreEvaluados(SolicitudAdmisionEntity solicitudAdmisionEntity, long cupoAprobado, String productoPredeterminado) {

        List<EvaluacionProductoEntity> evaluacionesPrevias = evaluacionProductoRepository.findAllBySolicitudAdmisionEntityAndVigenciaIsTrue(solicitudAdmisionEntity);
        evaluacionesPrevias.stream().forEach(e -> e.setVigencia(false));

        evaluacionProductoRepository.saveAll(evaluacionesPrevias);
        evaluacionProductoRepository.flush();


        List<ParTipoProductoEntity> parTipoProductoEntities = parTipoProductoRepository.findAllByVigenciaIsTrue();

        ParTipoProductoEntity productoRecomendado = null;
        if (productoPredeterminado != null) {
            log.debug("Producto predeterminado desde carga masiva: {}",productoPredeterminado);
            productoRecomendado = parTipoProductoRepository.buscarProductoPorDescripcion(productoPredeterminado);

        }

        if (productoRecomendado == null) {
            productoRecomendado = parTipoProductoRepository.obtenerProductoRecomendado();
        }

        ParTipoProductoEntity finalProductoRecomendado = productoRecomendado;

        List<EvaluacionProductoEntity> evaluacionProductoEntities = parTipoProductoEntities.stream().map(p -> {
            EvaluacionProductoEntity evaluacionProductoEntity = new EvaluacionProductoEntity();
            evaluacionProductoEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
            evaluacionProductoEntity.setParTipoProductoEntity(p);
            if (p.getTipoProductoId() == finalProductoRecomendado.getTipoProductoId()) {
                evaluacionProductoEntity.setRecomendado(true);
            }
            evaluacionProductoEntity.setCupoAprobado(cupoAprobado);
            evaluacionProductoEntity.setVigencia(true);
            evaluacionProductoEntity.setFechaRegistro(LocalDateTime.now());
            evaluacionProductoEntity.setUsuarioRegistro(USUARIO_TEMPORAL);

            return evaluacionProductoEntity;
        }).collect(Collectors.toList());

        evaluacionProductoRepository.saveAll(evaluacionProductoEntities);
        evaluacionProductoRepository.flush();
    }


}
