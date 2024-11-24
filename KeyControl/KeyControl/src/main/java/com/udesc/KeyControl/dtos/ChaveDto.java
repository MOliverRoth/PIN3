package com.udesc.KeyControl.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChaveDto(@NotBlank String titulo,
                        @NotBlank String descricao) {
    
}
