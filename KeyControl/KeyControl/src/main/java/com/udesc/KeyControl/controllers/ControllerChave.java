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
import java.sql.Time;
import java.time.LocalTime;

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
        Optional<Chave> chaveExistente = chaveRepository.findByCodigo(chaveDto.codigo());
        if (chaveExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código já existente");
        }

        Chave chave = new Chave();
        BeanUtils.copyProperties(chaveDto, chave);

        chave.setStatus(chave.getStatus() != null ? chave.getStatus() : "DISPONÍVEL");

        if (chave.getHoraInicio() != null && chave.getHoraFim() != null
                && chave.getHoraInicio().after(chave.getHoraFim())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hora de início deve ser anterior à hora de fim");
        }

        Chave chaveSalva = chaveRepository.save(chave);

        return ResponseEntity.status(HttpStatus.CREATED).body(chaveSalva);
    }

    @GetMapping("/visualizar-chave/{id}")
    public ResponseEntity<Object> getChave(@PathVariable(value = "id") int id) {
        Optional<Chave> chave = chaveRepository.findById(id);
        if (chave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave inexistente");
        }
        return ResponseEntity.status(HttpStatus.OK).body(chave.get());
    }

    @PutMapping("editar-chave/{id}")
    public ResponseEntity<?> editarChave(@PathVariable(value = "id") int idChave, @RequestBody ChaveDto chaveDto) {
        Optional<Chave> chaveOptional = chaveRepository.findById(idChave);
        if (!chaveOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave não encontrada");
        }

        Chave chave = chaveOptional.get();
        BeanUtils.copyProperties(chaveDto, chave, "id");
        Chave chaveAtualizada = chaveRepository.save(chave);
        return ResponseEntity.status(HttpStatus.OK).body(chaveAtualizada);
    }

    @DeleteMapping("deletar-chave/{id}")
    public ResponseEntity<Object> deletarChave(@PathVariable(value = "id") int idChave) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        if (chave.isPresent()) {
            chaveRepository.delete(chave.get());
            return ResponseEntity.status(HttpStatus.OK).body("Chave deletada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave não encontrada");
        }
    }

    @PostMapping("atribuir-chave")
    public ResponseEntity<Object> atribuir(@RequestParam int idChave, @RequestBody int idUsuario) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

        if (chave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave inexistente");
        }
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário inexistente");
        }

        boolean jaAtribuido = permissaoRepository.existsByChaveAndUsuario(chave.get(), usuario.get());
        if (jaAtribuido) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já está atribuído a esta chave");
        }

        Permissao permissao = new Permissao();
        permissao.setChave(chave.get());
        permissao.setUsuario(usuario.get());
        permissao.setConcessor(KeyControlApplication.actualUser);

        return ResponseEntity.status(HttpStatus.OK).body(permissaoRepository.save(permissao));
    }

    @DeleteMapping("Desatribuir-chave/{id}")
    public ResponseEntity<Object> desatribuir(@PathVariable(value = "id") int idPermissao) {
        Optional<Permissao> permissao = permissaoRepository.findById(idPermissao);
        permissaoRepository.delete(permissao.get());
        return ResponseEntity.status(HttpStatus.OK).body("Desatribuição feita com sucesso");
    }

    @GetMapping("/usuarios-chave/{idChave}")
    public ResponseEntity<Object> getUsuariosPorChave(@PathVariable(value = "idChave") int idChave) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        if (chave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave inexistente");
        }

        List<Permissao> permissoes = permissaoRepository.findByChave(chave.get());
        List<Usuario> usuarios = permissoes.stream()
                .map(Permissao::getUsuario)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @DeleteMapping("/desatribuir-chave")
    public ResponseEntity<Object> desatribuirPorChaveEUsuario(@RequestParam int idChave, @RequestParam int idUsuario) {
        Optional<Chave> chave = chaveRepository.findById(idChave);
        if (chave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave inexistente");
        }

        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário inexistente");
        }

        Optional<Permissao> permissao = permissaoRepository.findByChaveAndUsuario(chave.get(), usuario.get());
        if (permissao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permissão não encontrada para a chave e usuário fornecidos");
        }

        permissaoRepository.delete(permissao.get());
        return ResponseEntity.status(HttpStatus.OK).body("Permissão removida com sucesso");
    }

}
