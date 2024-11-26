package com.udesc.KeyControl.dtos;

import jakarta.validation.constraints.NotBlank;

public record NotificacaoDto(@NotBlank String titulo,
                             @NotBlank String descricao) {
    
}
