package com.udesc.KeyControl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Chave;
import com.udesc.KeyControl.models.Permissao;
import com.udesc.KeyControl.models.Usuario;
import java.util.List;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer>{
    Optional<Permissao> findByChaveAndUsuario(Chave chave, Usuario usuario);
    List<Permissao> findByChave(Chave chave);
    boolean existsByChaveAndUsuario(Chave chave, Usuario usuario);

}
