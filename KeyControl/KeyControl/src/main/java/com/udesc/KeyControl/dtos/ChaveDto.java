package com.udesc.KeyControl.dtos;

import java.sql.Time;

import jakarta.validation.constraints.NotNull;

public record ChaveDto( @NotNull Integer codigo,
                        String descricao,
                        String status,
                        Time horaInicio,
                        Time horaFim,
                        Boolean segunda,
                        Boolean terca,
                        Boolean quarta,
                        Boolean quinta,
                        Boolean sexta,
                        Boolean sabado,
                        Boolean domingo) {
}
