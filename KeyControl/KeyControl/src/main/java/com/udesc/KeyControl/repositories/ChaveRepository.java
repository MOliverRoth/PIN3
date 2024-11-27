package com.udesc.KeyControl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Chave;


public interface ChaveRepository extends JpaRepository<Chave, Integer>{
    Optional<Chave> findByCodigo(int codigo);
}
