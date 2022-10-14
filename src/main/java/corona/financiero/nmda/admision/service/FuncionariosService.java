package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.FuncionarioRequestDTO;
import corona.financiero.nmda.admision.dto.FuncionarioResponseDTO;
import corona.financiero.nmda.admision.dto.PaginacionFuncionarioResponseDTO;
import corona.financiero.nmda.admision.entity.FuncionariosEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.FuncionariosRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionariosService {

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    public FuncionariosEntity crearFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {
        validarRegistroFuncionario(funcionarioRequestDTO);

        String rutFormateado = validaciones.formateaRutHaciaBD(funcionarioRequestDTO.getRut());

        Optional<FuncionariosEntity> byId = funcionariosRepository.findByRut(rutFormateado);
        if (byId.isPresent()) {
            throw new BadRequestException("El rut " + funcionarioRequestDTO.getRut() + " ya se encuentra registrado como funcionario");
        }

        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut(rutFormateado);
        funcionariosEntity.setNombreCompleto(funcionarioRequestDTO.getNombreCompleto());
        funcionariosEntity.setNombreUsuario(funcionarioRequestDTO.getNombreUsuario());
        funcionariosEntity.setSucursalId(funcionarioRequestDTO.getSucursalId());
        funcionariosEntity.setNombreSucursal(funcionarioRequestDTO.getNombreSucursal());
        funcionariosEntity.setCargoId(funcionarioRequestDTO.getCargoId());
        funcionariosEntity.setNombreCargo(funcionarioRequestDTO.getNombreCargo());
        funcionariosEntity.setFechaRegistro(LocalDateTime.now());
        funcionariosEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        funcionariosEntity.setVigencia(true);
        funcionariosRepository.save(funcionariosEntity);

        return funcionariosEntity;
    }

    private void validarRegistroFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {

        if (funcionarioRequestDTO == null) {
            throw new BadRequestException();
        }

        if (funcionarioRequestDTO.getNombreCompleto() == null || funcionarioRequestDTO.getNombreCompleto().trim().isEmpty()) {
            throw new BadRequestException("El nombre del funcionario es requerido");
        }

        if (funcionarioRequestDTO.getNombreUsuario() == null || funcionarioRequestDTO.getNombreUsuario().trim().isEmpty()) {
            throw new BadRequestException("El nombre del funcionario es requerido");
        }

        if (funcionarioRequestDTO.getSucursalId() <= 0) {
            throw new BadRequestException("La sucursal asociada al funcionario no tiene un ID válido");
        }

        if (funcionarioRequestDTO.getNombreSucursal() == null || funcionarioRequestDTO.getNombreSucursal().trim().isEmpty()) {
            throw new BadRequestException("La sucursal asociada al funcionario es requerida");
        }

        if (funcionarioRequestDTO.getCargoId() <= 0) {
            throw new BadRequestException("El cargo del funcionario no tiene un ID válido");
        }

        if (funcionarioRequestDTO.getNombreCargo() == null || funcionarioRequestDTO.getNombreCargo().trim().isEmpty()) {
            throw new BadRequestException("El cargo del funcionario es requerido");
        }


        validarRut(funcionarioRequestDTO.getRut());
    }

    public void validarRut(String rutFuncionario) {
        if (rutFuncionario == null || rutFuncionario.trim().isEmpty()) {
            throw new BadRequestException("El rut del funcionario es requerido");
        }
        if (Boolean.FALSE.equals(validaciones.validaRut(rutFuncionario))) {
            throw new BadRequestException("El rut del funcionario no es valido");
        }
    }

    public boolean existeFuncionario(String rutFuncionario) {
        validarRut(rutFuncionario);
        String rutFormateado = validaciones.formateaRutHaciaBD(rutFuncionario);

        Optional<FuncionariosEntity> optional = funcionariosRepository.findByRut(rutFormateado);

        return optional.isPresent();
    }

    public PaginacionFuncionarioResponseDTO listarFuncionariosVigentes(int numPagina, String filtro) {

        if (numPagina < 0) {
            throw new BadRequestException("Numero de página no permitida");
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by("nombreCompleto"));

        Page<FuncionariosEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = funcionariosRepository.listarFuncionariosVigentes(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = funcionariosRepository.listarFuncionariosVigentesPorFiltro(filtro, pageable);
        }

        PaginacionFuncionarioResponseDTO paginacionFuncionarioResponseDTO = new PaginacionFuncionarioResponseDTO();
        paginacionFuncionarioResponseDTO.setTotalElementos(all.getTotalElements());
        paginacionFuncionarioResponseDTO.setTotalPagina(all.getTotalPages());
        paginacionFuncionarioResponseDTO.setPagina(all.getNumber());

        List<FuncionarioResponseDTO> funcionarios = all.stream().map(funcionario -> {
            FuncionarioResponseDTO funcionarioResponseDTO = new FuncionarioResponseDTO();

            funcionarioResponseDTO.setRut(validaciones.formateaRutHaciaFront(funcionario.getRut()));
            funcionarioResponseDTO.setNombreCompleto(funcionario.getNombreCompleto());
            funcionarioResponseDTO.setNombreUsuario(funcionario.getNombreUsuario());
            funcionarioResponseDTO.setSucursalId(funcionario.getSucursalId());
            funcionarioResponseDTO.setNombreSucursal(funcionario.getNombreSucursal());
            funcionarioResponseDTO.setCargoId(funcionario.getCargoId());
            funcionarioResponseDTO.setNombreCargo(funcionario.getNombreCargo());
            return funcionarioResponseDTO;
        }).collect(Collectors.toList());

        paginacionFuncionarioResponseDTO.setFuncionarios(funcionarios);
        return paginacionFuncionarioResponseDTO;
    }

}
