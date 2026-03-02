package com.example.backend.service;

import com.example.ejb.Beneficio;
import com.example.ejb.BeneficioEjbService;
import com.example.backend.repository.BeneficioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BeneficioService {

    private final BeneficioRepository repository;
    private final BeneficioEjbService ejbService;

    public BeneficioService(BeneficioRepository repository, BeneficioEjbService ejbService) {
        this.repository = repository;
        this.ejbService = ejbService;
    }

    public List<Beneficio> listAll() {
        return repository.findAll();
    }

    public Optional<Beneficio> findById(Long id) {
        return repository.findById(id);
    }

    public Beneficio create(Beneficio b) {
        b.setId(null);
        return repository.save(b);
    }

    public Beneficio update(Long id, Beneficio b) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setNome(b.getNome());
                    existing.setDescricao(b.getDescricao());
                    existing.setValor(b.getValor());
                    existing.setAtivo(b.getAtivo());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Benefício não encontrado: " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // delegate to EJB; it already has transactional semantics
        ejbService.transfer(fromId, toId, amount);
        // refresh snapshots so that Spring Data context sees changes
        repository.findById(fromId);
        repository.findById(toId);
    }
}
