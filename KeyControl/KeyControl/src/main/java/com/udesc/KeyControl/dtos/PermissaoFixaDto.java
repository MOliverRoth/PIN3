package com.udesc.KeyControl.dtos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;

public record PermissaoFixaDto(@NotBlank String[] diasEmprestimo,
                                @NotBlank Date horaInicio,
                                @NotBlank Date horaFim) {
    
}
