package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodigoPaisCelularDTO {

    private long codigoPaisId;
    private int codigoPais;
    private String descripcion;
}
