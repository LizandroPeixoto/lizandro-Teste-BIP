package com.example.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BeneficioEjbServiceTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private BeneficioEjbService service;

    @BeforeAll
    static void initFactory() {
        emf = Persistence.createEntityManagerFactory("testPU");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        service = new BeneficioEjbService();
        // manual injection
        TestUtils.injectEntityManager(service, em);

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Beneficio").executeUpdate();
        Beneficio a = new Beneficio();
        a.setNome("A");
        a.setValor(BigDecimal.valueOf(100));
        em.persist(a);
        Beneficio b = new Beneficio();
        b.setNome("B");
        b.setValor(BigDecimal.valueOf(50));
        em.persist(b);
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @Test
    void transferSuccessful() {
        em.getTransaction().begin();
        service.transfer(1L, 2L, BigDecimal.valueOf(30));
        em.getTransaction().commit();

        Beneficio from = em.find(Beneficio.class, 1L);
        Beneficio to = em.find(Beneficio.class, 2L);
        assertEquals(BigDecimal.valueOf(70), from.getValor());
        assertEquals(BigDecimal.valueOf(80), to.getValor());
    }

    @Test
    void transferInsufficientFunds() {
        em.getTransaction().begin();
        assertThrows(IllegalStateException.class, () -> service.transfer(2L, 1L, BigDecimal.valueOf(100)));
        em.getTransaction().rollback();
        Beneficio from = em.find(Beneficio.class, 2L);
        assertEquals(BigDecimal.valueOf(50), from.getValor());
    }

    @Test
    void transferNegativeAmount() {
        em.getTransaction().begin();
        assertThrows(IllegalArgumentException.class, () -> service.transfer(1L, 2L, BigDecimal.valueOf(-10)));
        em.getTransaction().rollback();
    }
}
