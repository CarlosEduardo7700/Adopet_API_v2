package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.service.AdocaoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AdocaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdocaoService service;

    @Test
    @DisplayName("Deveria retornar o código HTTP 400")
    void codigo400Cenario01() throws Exception {
        // ARRANGE
        String body = "{}";

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/adocoes")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());

    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 200")
    void codigo200Cenario02() throws Exception {
        // ARRANGE
        String body = """
                {
                        "idPet": 1,
                        "idTutor": 1,
                        "motivo": "Motivo qualquer"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/adocoes")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());

    }

}