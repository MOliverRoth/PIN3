package com.udesc.KeyControl.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="TB_USUARIO") @Data
public class Usuario {
       
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int idUsuario;

    private String nome;
    private String email;
    private String vinculo;
    private String cpf;
    private String senha;

    
    @JsonBackReference
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name="TB_USUARIO_NOTIFICACAO",
        joinColumns= @JoinColumn(name="id_usuario"),
        inverseJoinColumns = @JoinColumn(name="id_curso")
    )
    private List<Notificacao> notificacoes;

}
