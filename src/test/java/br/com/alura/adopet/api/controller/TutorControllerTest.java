package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.service.TutorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService service;

    @Test
    @DisplayName("Deveria retornar um código HTTP 200 após fazer o cadastro")
    void cadastroCod200Cenario01 () throws Exception {
        // ARRANGE
        String body = """
                {
                        "nome": "Nome do Tutor",
                        "telefone": "(90)1234-5678",
                        "email": "exemplo@email.com"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/tutores")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());

    }

    @Test
    @DisplayName("Deveria retornar um código HTTP 400 após não fazer o cadastro")
    void cadastroCod400Cenario02 () throws Exception {
        // ARRANGE
        String body = """
                {
                        "nome": "Nome do Tutor",
                        "telefone": "(90)1234-5678",
                        "email": "email.com"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/tutores")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());

    }

    @Test
    @DisplayName("Deveria retornar um código HTTP 200 após fazer uma atualização")
    void atualizarCod200Cenario03 () throws Exception {
        // ARRANGE
        String body = """
                {
                        "id": 10,
                        "nome": "Nome do Tutor",
                        "telefone": "(12)3456-7890",
                        "email": "exemplo@email.com"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                put("/tutores")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());

    }

    @Test
    @DisplayName("Deveria retornar um código HTTP 400 após não fazer uma atualização")
    void atualizarCod400Cenario04 () throws Exception {
        // ARRANGE
        String body = """
                {
                        "nome": "Nome do Tutor",
                        "telefone": "(12)3456-7890",
                        "email": "email.com"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/tutores")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());

    }

}