package com.udesc.KeyControl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer>{
    
}
