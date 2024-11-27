package com.udesc.KeyControl.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.udesc.KeyControl.KeyControlApplication;
import com.udesc.KeyControl.dtos.ChaveDto;
import com.udesc.KeyControl.models.Chave;
import com.udesc.KeyControl.models.Permissao;
import com.udesc.KeyControl.models.Usuario;
import com.udesc.KeyControl.repositories.ChaveRepository;
import com.udesc.KeyControl.repositories.PermissaoRepository;
import com.udesc.KeyControl.repositories.UsuarioRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class ControllerChave {
    @Autowired
    ChaveRepository chaveRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PermissaoRepository permissaoRepository;

    @PostMapping("/criar-chave")
    public ResponseEntity<Object> criarChave(@RequestBody @Valid ChaveDto chaveDto) {
        var chave = new Chave();
        BeanUtils.copyProperties(chaveDto, chave);
        Optional<Chave> key = chaveRepository.findByCodigo(chave.getCodigo());
        if (!key.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código já existente");
        }
        return ResponseEntity.status(HttpStatus.OK).body(chaveRepository.save(chave));
    }

    @GetMapping("/visualizar-chave/{id}")
    public ResponseEntity<Object> getChave(@PathVariable(value = "id") int id) {
        Optional<Chave> chave = chaveRepository.findById(id);
        if (chave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave inexistente");
        }
        return ResponseEntity.status(HttpStatus.OK).body(chave.get());
    }

    @GetMapping("/visualizar-chaves")
    public ResponseEntity<List<Chave>> getChaves() {
        return ResponseEntity.status(HttpStatus.OK).body(chaveRepository.findAll());
    }
    
    @PutMapping("editar-chave/{id}")
    public ResponseEntity<Chave> editarChave(@PathVariable(value="id") int idChave, @RequestBody ChaveDto chaveDto) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        BeanUtils.copyProperties(chaveDto, chave); 
        return ResponseEntity.status(HttpStatus.OK).body(chaveRepository.save(chave.get()));
    }
    
    @DeleteMapping("deletar-chave/{id}")
    public ResponseEntity<Object> deletarChave(@PathVariable(value="id") int idChave) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        chaveRepository.delete(chave.get());
        return ResponseEntity.status(HttpStatus.OK).body("chave deletada com sucesso");
    }

    @PostMapping("atribuir-chave")
    public ResponseEntity<Object> atribuir(@RequestParam int idChave, @RequestBody int idUsuario) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        Permissao pr = new Permissao();
        pr.setChave(chave.get());
        pr.setUsuario(usuario.get());  
        pr.setConcessor(KeyControlApplication.actualUser);
        return ResponseEntity.status(HttpStatus.OK).body(permissaoRepository.save(pr));
    }

    @DeleteMapping("Desatribuir-chave/{id}")
    public ResponseEntity<Object> desatribuir(@PathVariable(value="id") int idPermissao) {
        Optional<Permissao> permissao = permissaoRepository.findById(idPermissao);
        permissaoRepository.delete(permissao.get());
        return ResponseEntity.status(HttpStatus.OK).body("Desatribuição feita com sucesso");
    }
    
}
