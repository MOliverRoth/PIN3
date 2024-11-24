package com.udesc.KeyControl.models;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="TB_PERMISSAO_FIXA") @Data
public class PermissaoFixa {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int idPermissaoFixa;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_permissao")
    private Permissao permissao;

    @ElementCollection
    private String[] diasEmprestimo;

    private Date horaInicio;
    private Date horaFim;
}
