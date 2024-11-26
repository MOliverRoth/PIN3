package com.udesc.KeyControl.repositories;

import java.util.List;

import com.udesc.KeyControl.dtos.EmprestimoDto;
import com.udesc.KeyControl.models.Emprestimo;

public interface EmprestimoRepositoryCustom {
    List<Emprestimo> buscarComFiltros(EmprestimoDto filtro);
}
