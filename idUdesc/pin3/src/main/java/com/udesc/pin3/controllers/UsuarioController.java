package com.udesc.pin3.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.udesc.pin3.repositories.UsuarioRepository;


import java.util.List;
import jakarta.validation.Valid;
import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

import com.udesc.pin3.dtos.UsuarioRecordDTO;
import com.udesc.pin3.models.Usuario;

@RestController
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping("/criar-usuario")
    public ResponseEntity<Usuario> createUsuario(@RequestBody @Valid UsuarioRecordDTO usuarioRecordDTO) {
        var usuario = new Usuario();
        BeanUtils.copyProperties(usuarioRecordDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }

    @PostMapping("/criar-usuarios")
    public ResponseEntity<List<Usuario>> createUsuarios(@RequestBody @Valid List<UsuarioRecordDTO> usuariosRecordDTO) {
        List<Usuario> users = new ArrayList<Usuario>();
        for (UsuarioRecordDTO usuario : usuariosRecordDTO) {
            var user = new Usuario();
            BeanUtils.copyProperties(usuario, user);
            users.add(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.saveAll(users));
    }
    
    @GetMapping("/visualizar-usuario/{id}")
    public ResponseEntity<Object> getUsuario(@PathVariable(value="id") int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuario.get());
    }

    @GetMapping("/visualizar-usuarios")
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    @PutMapping("/editar-usuario/{id}")
    public ResponseEntity<Object> editUsuario(  @PathVariable(value="id") int id,
                                                @RequestBody @Valid UsuarioRecordDTO usuarioDTO) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        Usuario user = usuario.get();
        BeanUtils.copyProperties(usuarioDTO, user);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(user));
    }

    @DeleteMapping("/deletar-usuario/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable(value="id") int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        usuarioRepository.delete(usuario.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String cpf, @RequestParam String senha) {
        Boolean foundCPF = false;
        for (Usuario usr : usuarioRepository.findAll()) {
            if (foundCPF) { break; }
            if (cpf.equals(usr.getCpf())) {
                foundCPF = true;
                if (senha.equals(usr.getSenha())) {
                    return ResponseEntity.status(HttpStatus.OK).body(usr);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login inválido");
    }
}
