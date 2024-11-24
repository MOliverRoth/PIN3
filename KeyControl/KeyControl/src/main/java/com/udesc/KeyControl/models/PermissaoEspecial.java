package com.udesc.KeyControl.models;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="TB_PERMISSAO_ESPECIAL") @Data
public class PermissaoEspecial {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int idPermissaoEspecial;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_permissao")
    private Permissao permissao;

    private Date dataInicio;
    private Date dataFim;
}
