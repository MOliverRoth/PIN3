package com.udesc.KeyControl.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.sql.Time;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="TB_CHAVE") @Data
public class Chave {

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int idChave;
    
    private int codigo;
    private String descricao;
    private String status;
    private Time horaInicio;
    private Time horaFim;
    private Boolean segunda;
    private Boolean terca;
    private Boolean quarta;
    private Boolean quinta;
    private Boolean sexta;
    private Boolean sabado;
    private Boolean domingo;

    @JsonBackReference
    @OneToMany(mappedBy= "chave", orphanRemoval = true)
    private List<Emprestimo> emprestimos;

    @JsonBackReference
    @OneToMany(mappedBy="chave", orphanRemoval = true)
    private List<Permissao> permissoes;
}
