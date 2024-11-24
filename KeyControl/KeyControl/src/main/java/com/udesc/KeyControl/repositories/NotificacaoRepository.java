package com.udesc.KeyControl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udesc.KeyControl.models.Notificacao;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer>{
    
}
