package com.udesc.KeyControl.dtos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;

public record PermissaoEspecialDto(@NotBlank Date dataInicio,
                                    @NotBlank Date datafim) {
    
}
