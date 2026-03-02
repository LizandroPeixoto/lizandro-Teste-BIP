package com.example.backend.service;

import com.example.ejb.Beneficio;
import com.example.ejb.BeneficioEjbService;
import com.example.backend.repository.BeneficioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@Import(BeneficioService.class)
class BeneficioServiceTest {

    @MockBean
    private BeneficioEjbService ejbService;

    private BeneficioService service;

    private final BeneficioRepository repository;

    BeneficioServiceTest(BeneficioRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        service = new BeneficioService(repository, ejbService);
        repository.deleteAll();
        Beneficio b = new Beneficio();
        b.setNome("X");
        b.setValor(BigDecimal.valueOf(20));
        repository.save(b);
    }

    @Test
    void createAndFind() {
        Beneficio b = new Beneficio();
        b.setNome("Y");
        b.setValor(BigDecimal.valueOf(10));
        Beneficio saved = service.create(b);
        assertThat(service.findById(saved.getId())).isPresent();
    }

    @Test
    void transferDelegatesToEjb() {
        Mockito.doNothing().when(ejbService).transfer(1L, 2L, BigDecimal.valueOf(5));
        service.transfer(1L, 2L, BigDecimal.valueOf(5));
        Mockito.verify(ejbService).transfer(1L, 2L, BigDecimal.valueOf(5));
    }
}
