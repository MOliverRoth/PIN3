package com.udesc.KeyControl.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.udesc.KeyControl.KeyControlApplication;
import com.udesc.KeyControl.models.Notificacao;
import com.udesc.KeyControl.models.Usuario;
import com.udesc.KeyControl.repositories.NotificacaoRepository;
import com.udesc.KeyControl.repositories.UsuarioRepository;

import jakarta.validation.Valid;

import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class ControllerNotificacao {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @GetMapping("/notificacoes/{id}")
    public ResponseEntity<List<Notificacao>> getNotificacoesByUser(@PathVariable(value="id") int idUsuario) {
        Optional<Usuario> usr = usuarioRepository.findById(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(usr.get().getNotificacoes());
    }

    @DeleteMapping("/notificacoes/{id}")
    public ResponseEntity<Object> deletarNotificacao(@PathVariable(value="id") int idNotificacao){
        Optional<Notificacao> n = notificacaoRepository.findById(idNotificacao);
        notificacaoRepository.delete(n.get());
        return ResponseEntity.status(HttpStatus.OK).body("Notificação deletada");
    }
    
}
