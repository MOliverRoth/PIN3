package com.udesc.KeyControl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
}
