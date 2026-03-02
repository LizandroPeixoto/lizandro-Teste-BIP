package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Stateless
@Component
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Transferência entre dois benefícios.
     *
     * Faz locking pessimista nas duas entidades, valida existência e saldo suficiente.
     * A própria EJB container garantirá rollback em caso de exceção runtime.
     */
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("IDs de origem e destino são obrigatórios");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de transferência deve ser positivo");
        }

        // obtém as entidades com lock pessimista para evitar lost update
        Beneficio from = em.find(Beneficio.class, fromId, jakarta.persistence.LockModeType.PESSIMISTIC_WRITE);
        Beneficio to   = em.find(Beneficio.class, toId, jakarta.persistence.LockModeType.PESSIMISTIC_WRITE);

        if (from == null) {
            throw new jakarta.persistence.EntityNotFoundException("Benefício de origem não encontrado: " + fromId);
        }
        if (to == null) {
            throw new jakarta.persistence.EntityNotFoundException("Benefício de destino não encontrado: " + toId);
        }
        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente no benefício de origem");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
