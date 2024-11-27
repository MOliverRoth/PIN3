package com.udesc.KeyControl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Chave;
import com.udesc.KeyControl.models.Permissao;
import com.udesc.KeyControl.models.Usuario;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer>{
    Optional<Permissao> findByCHaveAndUsuario(Chave chave, Usuario usuario);
}
