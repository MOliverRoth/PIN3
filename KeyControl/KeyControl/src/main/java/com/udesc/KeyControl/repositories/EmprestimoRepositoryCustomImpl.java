package com.udesc.KeyControl.repositories;

import java.util.ArrayList;
import java.util.List;

import com.udesc.KeyControl.dtos.EmprestimoDto;
import com.udesc.KeyControl.models.Emprestimo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;

public class EmprestimoRepositoryCustomImpl implements EmprestimoRepositoryCustom{
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Emprestimo> buscarComFiltros(EmprestimoDto filtro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Emprestimo> query = cb.createQuery(Emprestimo.class);
        Root<Emprestimo> root = query.from(Emprestimo.class);

        // Lista de condições dinâmicas
        List<Predicate> predicates = new ArrayList<>();

        if (filtro.dataRetirada() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("dataRetirada"), filtro.dataRetirada()));
        }

        if (filtro.dataEntrega() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("dataEntrega"), filtro.dataEntrega()));
        }

        if (filtro.solicitante() != null) {
            predicates.add(cb.equal(root.get("solicitante"), filtro.solicitante()));
        }

        if (filtro.devolvente() != null) {
            predicates.add(cb.equal(root.get("devolvente"), filtro.devolvente()));
        }

        if (filtro.vigilanteEntrega() != null) {
            predicates.add(cb.equal(root.get("vigilanteEntrega"), filtro.vigilanteEntrega()));
        }

        if (filtro.vigilanteRetirada() != null) {
            predicates.add(cb.equal(root.get("vigilanteRetirada"), filtro.vigilanteRetirada()));
        }

        if (filtro.chave() != null) {
            predicates.add(cb.equal(root.get("chave"), filtro.chave()));
        }

        if (filtro.atraso() != null) {
            predicates.add(cb.equal(root.get("atraso"), filtro.atraso()));
        }

        // Aplicar os predicados na query
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
