package com.udesc.pin3.dtos;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRecordDTO( @NotBlank   String nome,
                                            String email,
                                            String vinculo,
                                @NotBlank   String cpf,
                                @NotBlank   String senha) {
    
}
