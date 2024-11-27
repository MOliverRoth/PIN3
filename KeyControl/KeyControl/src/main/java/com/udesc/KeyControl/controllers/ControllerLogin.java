package com.udesc.KeyControl.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udesc.KeyControl.KeyControlApplication;
import com.udesc.KeyControl.models.Usuario;
import com.udesc.KeyControl.repositories.UsuarioRepository;

@RestController
public class ControllerLogin {

    @Autowired
    UsuarioRepository usuarioRepository;

    //Para Logar (supondo que esteja sendo alimentado pelo idUdesc)
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String cpf, @RequestParam String senha) {
        Optional<Usuario> usr = usuarioRepository.findByCpfAndSenha(cpf, senha);
        if (usr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        KeyControlApplication.actualUser = usr.get();
        return ResponseEntity.status(HttpStatus.OK).body(usr.get());
    }

    @PostMapping("/sair")
    public ResponseEntity<Object> deslogar() {
        KeyControlApplication.actualUser = null;
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deslogado");
    }
}
