package com.udesc.KeyControl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer>, EmprestimoRepositoryCustom{
    Optional<Emprestimo> findByChaveAndVigilanteEntregaIsNull(int idChave);
}
