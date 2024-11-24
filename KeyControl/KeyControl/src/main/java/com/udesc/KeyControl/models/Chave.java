package com.udesc.KeyControl.models;

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

    @OneToMany(mappedBy= "chave", orphanRemoval = true)
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy="chave", orphanRemoval = true)
    private List<Permissao> permissoes;
}
