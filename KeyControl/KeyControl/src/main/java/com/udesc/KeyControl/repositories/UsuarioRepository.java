package com.udesc.KeyControl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    Optional<Usuario> findByCpfAndSenha(String username, String senha);
}
