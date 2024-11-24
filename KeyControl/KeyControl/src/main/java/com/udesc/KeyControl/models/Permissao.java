package com.udesc.KeyControl.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="TB_PERMISSAO") @Data
public class Permissao {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) 
    private int idPermissao;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="id_chave")
    private Chave chave;

    @OneToOne(mappedBy ="permissao")
    private PermissaoFixa permissaoFixa;

    @OneToOne(mappedBy="permissao")
    private PermissaoEspecial permissaoEspecial;
}