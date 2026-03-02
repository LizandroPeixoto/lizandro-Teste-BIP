package com.example.backend;

import com.example.ejb.Beneficio;
import com.example.backend.service.BeneficioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BeneficioControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BeneficioService service;

    @Test
    void listReturnsJson() throws Exception {
        Beneficio b = new Beneficio();
        b.setId(1L);
        b.setNome("Test");
        when(service.listAll()).thenReturn(Collections.singletonList(b));

        mvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMissingReturns404() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/beneficios/1"))
                .andExpect(status().isNotFound());
    }
}
