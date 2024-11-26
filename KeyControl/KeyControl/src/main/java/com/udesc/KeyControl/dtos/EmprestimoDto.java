package com.udesc.KeyControl.dtos;

import java.sql.Date;

public record EmprestimoDto(Date dataRetirada,
                            Date dataEntrega,
                            Integer solicitante,
                            Integer devolvente,
                            Integer vigilanteEntrega,
                            Integer vigilanteRetirada,
                            Integer chave,
                            Boolean atraso) {
}
