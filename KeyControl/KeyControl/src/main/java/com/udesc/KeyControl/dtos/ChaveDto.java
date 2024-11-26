package com.udesc.KeyControl.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChaveDto( @NotBlank int codigo,
                        @NotBlank String titulo,
                        @NotBlank String descricao,
                        String status) {
    
}
