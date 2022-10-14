package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.dto.ExportSolicitudAdmisionDTO;
import corona.financiero.nmda.admision.dto.ListaSolicitudAdmisionDTO;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class SolicitudAdmisionFiltrosRepositoryImpl {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Validaciones validaciones;

    public Page<ListaSolicitudAdmisionDTO> findAllByNativo(LocalDate fechaDesde, LocalDate fechaHasta, String rut, Long canalAtencion, Long sucursal, Long estadoSolicitud, Long fase, String zonaGeografica, int columnaOrden, String direccion, Pageable pageable) {

        String jpql = "SELECT sa.id_solicitud, p.rut, pes.descripcion_estado, pf.descripcion_fase, sa.fecha_solicitud, pc.descripcion_canal, su.descripcion_sucursal, su.descripcion_zona_geografica, sa.fecha_ing_reg FROM solicitud_admision sa inner join prospecto p on (sa.id_prospecto = p.id_prospecto) inner join par_estado_solicitud pes on (sa.id_estado_solicitud = pes.id_estado_solicitud) inner join par_canal pc on(sa.id_canal = pc.id_canal) " +
                "inner join admision_fase af on (sa.id_solicitud = af.id_solicitud) inner join par_fase pf on (af.id_fase = pf.id_fase), sucursales su WHERE sa.id_sucursal = su.codigo_sucursal AND sa.fecha_solicitud between :fechaDesde AND :fechaHasta";

        String jpql2 = "SELECT count(sa.id_solicitud) as contador FROM solicitud_admision sa inner join prospecto p on (sa.id_prospecto = p.id_prospecto) inner join par_estado_solicitud pes on (sa.id_estado_solicitud = pes.id_estado_solicitud) inner join par_canal pc on(sa.id_canal = pc.id_canal) " +
                "inner join admision_fase af on (sa.id_solicitud = af.id_solicitud) inner join par_fase pf on (af.id_fase = pf.id_fase), sucursales su WHERE sa.id_sucursal = su.codigo_sucursal AND sa.fecha_solicitud between :fechaDesde AND :fechaHasta";

        if (rut != null) {
            jpql += " AND p.rut = :rut";
            jpql2 += " AND p.rut = :rut";
        }

        if (canalAtencion != null) {
            jpql += " AND sa.id_canal = :canalAtencion";
            jpql2 += " AND sa.id_canal = :canalAtencion";
        }

        if (sucursal != null) {
            jpql += " AND sa.id_sucursal = :sucursal";
            jpql2 += " AND sa.id_sucursal = :sucursal";
        }

        if (estadoSolicitud != null) {
            jpql += " AND sa.id_estado_solicitud = :estadoSolicitud";
            jpql2 += " AND sa.id_estado_solicitud = :estadoSolicitud";
        }

        if (zonaGeografica != null) {
            jpql += " AND su.descripcion_zona_geografica = :zonaGeografica";
            jpql2 += " AND su.descripcion_zona_geografica = :zonaGeografica";
        }

        if (fase != null) {
            jpql += " AND (af.id_fase = :fase OR pf.id_fase_padre = :fase)";
            jpql2 += " AND (af.id_fase = :fase OR pf.id_fase_padre = :fase)";
        }

        jpql += " AND af.id_admision_fase = (select max(af2.id_admision_fase) from admision_fase af2 where af2.id_solicitud = sa.id_solicitud) order by " + columnaOrden + " " + direccion;
        jpql2 += " AND af.id_admision_fase = (select max(af2.id_admision_fase) from admision_fase af2 where af2.id_solicitud = sa.id_solicitud)";

        Query query = entityManager.createNativeQuery(jpql, Tuple.class);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);


        Query queryTotal = entityManager.createNativeQuery(jpql2, Tuple.class);


        query.setParameter("fechaDesde", fechaDesde);
        query.setParameter("fechaHasta", fechaHasta);

        queryTotal.setParameter("fechaDesde", fechaDesde);
        queryTotal.setParameter("fechaHasta", fechaHasta);

        if (rut != null) {
            query.setParameter("rut", rut);
            queryTotal.setParameter("rut", rut);
        }

        if (canalAtencion != null) {
            query.setParameter("canalAtencion", canalAtencion);
            queryTotal.setParameter("canalAtencion", canalAtencion);
        }

        if (sucursal != null) {
            query.setParameter("sucursal", sucursal);
            queryTotal.setParameter("sucursal", sucursal);
        }

        if (estadoSolicitud != null) {
            query.setParameter("estadoSolicitud", estadoSolicitud);
            queryTotal.setParameter("estadoSolicitud", estadoSolicitud);
        }

        if (zonaGeografica != null) {
            query.setParameter("zonaGeografica", zonaGeografica);
            queryTotal.setParameter("zonaGeografica", zonaGeografica);
        }

        if (fase != null) {
            query.setParameter("fase", fase);
            queryTotal.setParameter("fase", fase);
        }

        List<Tuple> resultList = query.getResultList();

        List<ListaSolicitudAdmisionDTO> collect = resultList.stream().map(l -> {
            ListaSolicitudAdmisionDTO customSolicitudes = new ListaSolicitudAdmisionDTO();
            BigInteger id = l.get(0, BigInteger.class);
            customSolicitudes.setSolicitudId(id.longValue());

            String rutTmp = l.get(1, String.class);

            customSolicitudes.setRut(validaciones.formateaRutHaciaFront(rutTmp));
            customSolicitudes.setEstadoSolicitud(l.get(2, String.class));
            customSolicitudes.setFaseEvaluacion(l.get(3, String.class));

            Date fechaSolicitud = l.get(4, Date.class);
            customSolicitudes.setFechaSolicitud(Instant.ofEpochMilli(fechaSolicitud.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

            customSolicitudes.setCanalAtencion(l.get(5, String.class));
            customSolicitudes.setSucursal(l.get(6, String.class));
            customSolicitudes.setZonaGeograficaSucursal(l.get(7, String.class));

            return customSolicitudes;
        }).collect(Collectors.toList());

        List<Tuple> totalResultList = queryTotal.getResultList();
        long totalRegistros = totalResultList.get(0).get(0, BigInteger.class).longValue();
        PageImpl p = new PageImpl<>(collect, pageable, totalRegistros);

        return new PageImpl<>(collect, pageable, totalRegistros);

    }


    public List<ExportSolicitudAdmisionDTO> findAllByNativoExportar(LocalDate fechaDesde, LocalDate fechaHasta) {

        String jpql = "SELECT " +
                "sa.id_solicitud, p.rut, pes.descripcion_estado, pf.descripcion_fase, sa.fecha_solicitud, pc.descripcion_canal, su.descripcion_sucursal, su.descripcion_zona_geografica, p.nombres, p.apellido_paterno, p.apellido_materno, p.fecha_nacimiento, p.email, p.celular, rn.descripcion_regla, fun.rut as rut_funcionario, fun.nombre_completo, po.descripcion as origen " +
                "FROM " +
                "solicitud_admision sa inner join prospecto p on (sa.id_prospecto = p.id_prospecto) " +
                "inner join par_origen po on(sa.id_origen = po.id_origen) " +
                "inner join par_estado_solicitud pes on (sa.id_estado_solicitud = pes.id_estado_solicitud) " +
                "inner join par_canal pc on(sa.id_canal = pc.id_canal) " +
                "inner join admision_fase af on (sa.id_solicitud = af.id_solicitud) " +
                "left join admision_regla_negocio arn on (sa.id_solicitud = arn.id_solicitud) " +
                "left join regla_negocio rn on(arn.id_regla = rn.id_regla) " +
                "inner join par_fase pf on (af.id_fase = pf.id_fase)  " +
                "left join cliente cl on (p.id_prospecto = cl.id_prospecto), sucursales su, funcionarios fun " +
                "WHERE " +
                "sa.id_sucursal = su.codigo_sucursal and sa.usuario_ing_reg = fun.nombre_usuario " +
                "AND sa.fecha_solicitud between :fechaDesde AND :fechaHasta order by sa.fecha_ing_reg DESC";


        Query query = entityManager.createNativeQuery(jpql, Tuple.class);


        query.setParameter("fechaDesde", fechaDesde);
        query.setParameter("fechaHasta", fechaHasta);

        List<Tuple> resultList = query.getResultList();

        List<ExportSolicitudAdmisionDTO> collect = resultList.stream().map(l -> {
            ExportSolicitudAdmisionDTO exportSolicitudAdmisionDTO = new ExportSolicitudAdmisionDTO();
            BigInteger id = l.get(0, BigInteger.class);
            exportSolicitudAdmisionDTO.setSolicitudId(id.longValue());

            String rutTmp = l.get(1, String.class);
            exportSolicitudAdmisionDTO.setRutCliente(validaciones.formateaRutHaciaFront(rutTmp));

            exportSolicitudAdmisionDTO.setEstadoSolicitud(l.get(2, String.class));
            exportSolicitudAdmisionDTO.setFaseEvaluacion(l.get(3, String.class));

            Date fechaSolicitud = l.get(4, Date.class);
            exportSolicitudAdmisionDTO.setFechaSolicitud(Instant.ofEpochMilli(fechaSolicitud.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

            exportSolicitudAdmisionDTO.setCanalAtencion(l.get(5, String.class));
            exportSolicitudAdmisionDTO.setSucursal(l.get(6, String.class));
            exportSolicitudAdmisionDTO.setZonaGeograficaSucursal(l.get(7, String.class));
            exportSolicitudAdmisionDTO.setNombreCliente(l.get(8, String.class));
            exportSolicitudAdmisionDTO.setApellidoPaternoCliente(l.get(9, String.class));
            exportSolicitudAdmisionDTO.setApellidoMaternoCliente(l.get(10, String.class));
            if(l.get(11, Date.class) != null){
                Date fechaNacimiento = (Date) l.get(11, Date.class);

                Instant instant = Instant.ofEpochMilli(fechaNacimiento.getTime());
                LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());

                 int edad = validaciones.calcularEdad(localDate);

                exportSolicitudAdmisionDTO.setEdad(edad);
            }

            exportSolicitudAdmisionDTO.setEmailCliente(l.get(12, String.class));
            exportSolicitudAdmisionDTO.setMovil(l.get(13, Integer.class));
            exportSolicitudAdmisionDTO.setReglaNegocio(l.get(14, String.class));

            String rutTmp2 = l.get(15, String.class);
            exportSolicitudAdmisionDTO.setRutEjecutivo(validaciones.formateaRutHaciaFront(rutTmp2));
            exportSolicitudAdmisionDTO.setNombreEjecutivo(l.get(16, String.class));
            exportSolicitudAdmisionDTO.setOrigen(l.get(17, String.class));


            return exportSolicitudAdmisionDTO;
        }).collect(Collectors.toList());

        return collect;

    }
}
