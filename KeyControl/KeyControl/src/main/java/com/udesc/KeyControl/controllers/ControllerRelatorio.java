package com.udesc.KeyControl.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.udesc.KeyControl.dtos.EmprestimoDto;
import com.udesc.KeyControl.models.Emprestimo;
import com.udesc.KeyControl.repositories.EmprestimoRepository;

@RestController
public class ControllerRelatorio {

    @Autowired
    EmprestimoRepository emprestimoRepository;

    // Recupera todos os emprestimos
    @GetMapping("/visualizar-emprestimos")
    public ResponseEntity<List<Emprestimo>> getEmprestimos() {
        List<Emprestimo> lp = emprestimoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoRepository.findAll());
    }

    // recupera emprestimos com filtro
    @PostMapping("/filtrar-emprestimos")
    public ResponseEntity<List<Emprestimo>> buscarEmprestimosFiltro(@RequestBody EmprestimoDto filtro) {
        List<Emprestimo> emprestimos = emprestimoRepository.buscarComFiltros(filtro);
        return ResponseEntity.ok(emprestimos);
    }
}
