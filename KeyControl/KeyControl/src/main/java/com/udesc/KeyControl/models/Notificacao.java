package com.udesc.KeyControl.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="TB_NOTIFICACAO") @Data
public class Notificacao {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int idNotificacao;

    private String titulo;
    private String descricao;
    
    @JsonBackReference
    @ManyToMany(mappedBy = "notificacoes", cascade= CascadeType.REMOVE)
    private List<Usuario> usuarios;
}
