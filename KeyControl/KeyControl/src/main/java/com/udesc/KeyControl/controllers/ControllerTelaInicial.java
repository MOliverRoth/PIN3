package com.udesc.KeyControl.controllers;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.udesc.KeyControl.KeyControlApplication;
import com.udesc.KeyControl.models.Chave;
import com.udesc.KeyControl.models.Emprestimo;
import com.udesc.KeyControl.models.EmprestimoRequest;
import com.udesc.KeyControl.models.Notificacao;
import com.udesc.KeyControl.models.Permissao;
import com.udesc.KeyControl.models.Usuario;
import com.udesc.KeyControl.repositories.ChaveRepository;
import com.udesc.KeyControl.repositories.EmprestimoRepository;
import com.udesc.KeyControl.repositories.NotificacaoRepository;
import com.udesc.KeyControl.repositories.PermissaoRepository;
import com.udesc.KeyControl.repositories.UsuarioRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ControllerTelaInicial {

    @Autowired
    ChaveRepository chaveRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EmprestimoRepository emprestimoRepository;

    @Autowired
    PermissaoRepository permissaoRepository;

    @Autowired
    NotificacaoRepository notificacaoRepository;

    //Recupera todas as chaves  
    @GetMapping("/visualizar-chaves")
    public ResponseEntity<List<Chave>> getChaves() {
        return ResponseEntity.status(HttpStatus.OK).body(chaveRepository.findAll());
    }

    //Realiza a ação de emprestar uma chave  
    @PostMapping("/emprestar/{id}")
    public ResponseEntity<Object> emprestar(@PathVariable(value = "id") int idChave, @RequestBody EmprestimoRequest emprestimoRequest) {
        Chave key = chaveRepository.findById(idChave).get();

        if (!key.getStatus().equalsIgnoreCase("DISPONÍVEL")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Chave indisponível");
        }

        Optional<Usuario> usr = usuarioRepository.findByCpfAndSenha(emprestimoRequest.getCpf(), emprestimoRequest.getSenha());
        if (usr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        Usuario tomador = usr.get();

        Optional<Permissao> permiss = permissaoRepository.findByChaveAndUsuario(key, tomador);
        if (permiss.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permissão não concedida");
        }

        if (!validateEmprestimo(key)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fora do horário de empréstimo");
        }

        key.setStatus("INDISPONÍVEL");
        Emprestimo emp = new Emprestimo();
        emp.setAtraso(false);
        emp.setChave(key);
        emp.setSolicitante(tomador);
        emp.setVigilanteRetirada(KeyControlApplication.actualUser);
        emp.setDataRetirada(new Date(System.currentTimeMillis()));
        emp.setHoraRetirada(new Time(System.currentTimeMillis()));

        return ResponseEntity.status(HttpStatus.OK).body(emprestimoRepository.save(emp));
    }

    public Boolean validateEmprestimo(Chave key) {
        Boolean dia = false;
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        switch (dayOfWeek) {
            case MONDAY:
                dia = key.getSegunda();
                break;
            case TUESDAY:
                dia = key.getTerca();
                break;
            case WEDNESDAY:
                dia = key.getQuarta();
                break;
            case THURSDAY:
                dia = key.getQuinta();
                break;
            case FRIDAY:
                dia = key.getSexta();
                break;
            case SATURDAY:
                dia = key.getSabado();
                break;
            case SUNDAY:
                dia = key.getDomingo();
                break;
            default:
                break;
        }

        if (dia) {
            Time horaRetiro = new Time(System.currentTimeMillis());
            LocalTime horaRetirada = horaRetiro.toLocalTime();
            LocalTime horaInicio = key.getHoraInicio().toLocalTime();
            LocalTime horaFim = key.getHoraFim().toLocalTime();

            if (horaRetirada.isBefore(horaInicio)) {
                return false;
            }
            if (horaRetirada.isAfter(horaFim)) {
                return false;
            }
        }
        return dia;
    }

    @PutMapping("devolver-chave/{id}")
    public ResponseEntity<Object> putMethodName(@PathVariable(value = "id") int idChave, @RequestBody EmprestimoRequest emprestimoRequest) {

        Chave key = chaveRepository.findById(idChave).get();
        if (key.getStatus().equalsIgnoreCase("DISPONÍVEL")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Chave não emprestada");
        }

        Optional<Usuario> usr = usuarioRepository.findByCpfAndSenha(emprestimoRequest.getCpf(), emprestimoRequest.getSenha());
        if (usr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        Usuario devolvente = usr.get();

        Emprestimo emp = emprestimoRepository.findByChaveAndVigilanteEntregaIsNull(key);
        emp.setDevolvente(devolvente);
        emp.setVigilanteEntrega(KeyControlApplication.actualUser);
        emp.setDataEntrega(new Date(System.currentTimeMillis()));
        emp.setHoraEntrega(new Time(System.currentTimeMillis()));
        emprestimoRepository.save(emp);
        key.setStatus("DISPONÍVEL");
        chaveRepository.save(key);

        emp.setAtraso(!validateDevolucao(emp, key));
        if (emp.isAtraso()) {
            Notificacao n = new Notificacao();
            n.setTitulo("Devolução com atraso");
            n.setDescricao("Chave " + emp.getChave().getCodigo() + " devolvida com atraso por " + emp.getDevolvente().getNome());
            notificacaoRepository.save(n);
            Optional<Permissao> pr = permissaoRepository.findByChaveAndUsuario(emp.getChave(), emp.getSolicitante());
            pr.get().getConcessor().getNotificacoes().add(n);
            usuarioRepository.save(pr.get().getConcessor());
        }

        return ResponseEntity.status(HttpStatus.OK).body(emprestimoRepository.save(emp));
    }

    public Boolean validateDevolucao(Emprestimo emp, Chave key) {
        Time horaDev = new Time(System.currentTimeMillis());
        LocalTime horaDevolucao = horaDev.toLocalTime();
        LocalTime horaFim = key.getHoraFim().toLocalTime();

        Date dataAt = new Date(System.currentTimeMillis());
        LocalDate dataAtual = dataAt.toLocalDate();
        LocalDate dataEmprestimo = emp.getDataRetirada().toLocalDate();

        if (dataAtual.isAfter(dataEmprestimo)) {
            return false;
        }
        if (horaDevolucao.isAfter(horaFim)) {
            return false;
        }

        return true;
    }

}
