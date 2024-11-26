package com.udesc.KeyControl.controllers;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.udesc.KeyControl.KeyControlApplication;
import com.udesc.KeyControl.models.Chave;
import com.udesc.KeyControl.models.Emprestimo;
import com.udesc.KeyControl.models.Usuario;
import com.udesc.KeyControl.repositories.ChaveRepository;
import com.udesc.KeyControl.repositories.EmprestimoRepository;
import com.udesc.KeyControl.repositories.UsuarioRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class ControllerTelaInicial {
    
    @Autowired
    ChaveRepository chaveRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EmprestimoRepository emprestimoRepository;

    //Recupera todas as chaves  
    @GetMapping("/visualizar-chaves")
    public ResponseEntity<List<Chave>> getChaves() {
        return ResponseEntity.status(HttpStatus.OK).body(chaveRepository.findAll());
    }

    //Realiza a ação de emprestar uma chave  
    @PostMapping("/emprestar/{id}")
    public ResponseEntity<Object> emprestar(@PathVariable(value="id") int idChave, @RequestParam String cpf, @RequestParam String senha) {
        Chave key = chaveRepository.findById(idChave).get();

        // valida se a chave está disponível  
        if (!key.getStatus().equalsIgnoreCase("DISPONÍVEL")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Chave indisponível");
        }

        // valida se o Login de usuário é valido
        Optional<Usuario> usr = usuarioRepository.findByCpfAndSenha(cpf, senha);
        if (usr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        Usuario tomador = usr.get();
        
        // adicionar código para validar se o tomador possui permissão para recolher a chave

        Emprestimo emp = new Emprestimo();
        emp.setAtraso(false);
        emp.setChave(key);
        emp.setSolicitante(tomador);
        emp.setVigilanteRetirada(KeyControlApplication.actualUser);
        emp.setDataRetirada(new Date(System.currentTimeMillis()));
        emp.setHoraRetirada(new Time(System.currentTimeMillis()));
        return ResponseEntity.status(1).body(emprestimoRepository.save(emp));
    }

    @PutMapping("devolver-chave/{id}")
    public ResponseEntity<Object> putMethodName(@PathVariable(value="id") int idChave, @RequestParam String cpf, @RequestParam String senha) {

        // Valida se a chave realmente foi retirada
        Chave key = chaveRepository.findById(idChave).get();
        if (key.getStatus().equalsIgnoreCase("DISPONÍVEL")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Chave não emprestada");
        }

        // valida se o Login de usuário é valido
        Optional<Usuario> usr = usuarioRepository.findByCpfAndSenha(cpf, senha);
        if (usr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        Usuario devolvente = usr.get();

        Emprestimo emp = emprestimoRepository.findByChaveAndVigilanteEntregaIsNull(idChave).get();
        emp.setDevolvente(devolvente);
        emp.setVigilanteEntrega(KeyControlApplication.actualUser);
        emp.setDataEntrega(new Date(System.currentTimeMillis()));
        emp.setHoraEntrega(new Time(System.currentTimeMillis()));
        emprestimoRepository.save(emp);
        key.setStatus("DISPONÍVEL");
        chaveRepository.save(key);
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoRepository.save(emp));
    }
    
}