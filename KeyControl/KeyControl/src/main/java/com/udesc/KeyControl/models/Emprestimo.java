package com.udesc.KeyControl.models;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity @Table(name="TB_EMPRESTIMO") @Data
public class Emprestimo {
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private int idEmprestimo;

    private Date dataRetirada;
    private Time horaRetirada;
    private Date dataEntrega;
    private Time horaEntrega;

@JsonManagedReference
    @ManyToOne
    @JoinColumn(name="id_solicitante")
    private Usuario solicitante;

@JsonManagedReference
    @ManyToOne
    @JoinColumn(name="id_devolvente")
    private Usuario devolvente;

@JsonManagedReference
    @ManyToOne
    @JoinColumn(name="id_vigilante_retirada")
    private Usuario vigilanteRetirada;

@JsonManagedReference
    @ManyToOne
    @JoinColumn(name="id_vigilante_entrega")
    private Usuario vigilanteEntrega;

@JsonManagedReference
    @ManyToOne
    @JoinColumn(name="id_Chave")
    private Chave chave;

    private boolean atraso;
    
}