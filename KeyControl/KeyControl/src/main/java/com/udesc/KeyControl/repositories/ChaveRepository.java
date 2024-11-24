package com.udesc.KeyControl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Chave;

public interface ChaveRepository extends JpaRepository<Chave, Integer>{
    
}
