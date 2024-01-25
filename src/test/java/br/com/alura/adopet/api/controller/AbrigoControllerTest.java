package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Test
    @DisplayName("Deveria retornar uma lista dos abrigos cadastrados")
    void listarAbrigosCenario01 () throws Exception {
        // ARRANGE

        // ACT
        MockHttpServletResponse response = mvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 200 ao fazer um cadastro")
    void cadastrarCodigo200Cenario02 () throws Exception {
        // ARRANGE
        String body = """
                {
                        "nome": "Nome do Abrigo",
                        "telefone": "(90)1234-5678",
                        "email": "exemplo@email.com"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 400 ao não conseguir fazer um cadastro")
    void cadastrarCodigo400Cenario03 () throws Exception {
        // ARRANGE
        String body = """
                {
                        "nome": "Nome do Abrigo",
                        "telefone": "36",
                        "email": "exemplo@email.com"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 200 ao listar por nome os pets do abrigo")
    void listarPetsPorNomeCodigo200Cenario04 () throws Exception {
        // ARRANGE
        String nome = "Nome do Abrigo";

        // ACT
        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 404 ao não conseguir listar por nome os pets do abrigo")
    void listarPetsPorNomeCodigo404Cenario05 () throws Exception {
        // ARRANGE
        String nome = "Nome do Abrigo";
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(nome)).willThrow(ValidacaoException.class);

        // ACT
        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 200 ao listar por id os pets do abrigo")
    void listarPetsPorIdCodigo200Cenario06 () throws Exception {
        // ARRANGE
        String id = "10";

        // ACT
        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{id}/pets", id)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 404 ao não conseguir listar por id os pets do abrigo")
    void listarPetsPorIdCodigo404Cenario07 () throws Exception {
        // ARRANGE
        String id = "11";
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(id)).willThrow(ValidacaoException.class);

        // ACT
        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{id}/pets", id)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 200 ao cadastrar um pet pelo nome do abrigo")
    void cadastrarPetPorNomeCod200Cenario08 () throws Exception {
        // ARRANGE
        String nome = "Nome do Abrigo";

        String body = """
                {
                    "tipo": "CACHORRO",
                    "nome": "Nome do Pet",
                    "raca": "Pastor Alemão",
                    "idade": "7",
                    "cor" : "Preto e Marrom",
                    "peso": "25.5"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos/{nome}/pets", nome)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 404 ao não conseguir cadastrar um pet pelo nome do abrigo")
    void cadastrarPetPorNomeCod404Cenario09 () throws Exception {
        // ARRANGE
        String nome = "Nome do Abrigo";

        String body = """
                {
                    "tipo": "CACHORRO",
                    "nome": "Nome do Pet",
                    "raca": "Pastor Alemão",
                    "idade": "7",
                    "cor" : "Preto e Marrom",
                    "peso": "25.5"
                }
                """;

        BDDMockito.given(abrigoService.carregarAbrigo(nome)).willThrow(ValidacaoException.class);

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos/{nome}/pets", nome)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 200 ao cadastrar um pet pelo id do abrigo")
    void cadastrarPetPorIdCod200Cenario10 () throws Exception {
        // ARRANGE
        String id = "10";

        String body = """
                {
                    "tipo": "CACHORRO",
                    "nome": "Nome do Pet",
                    "raca": "Pastor Alemão",
                    "idade": "7",
                    "cor" : "Preto e Marrom",
                    "peso": "25.5"
                }
                """;

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos/{id}/pets", id)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar o código HTTP 404 ao não conseguir cadastrar um pet pelo id do abrigo")
    void cadastrarPetPorIdCod404Cenario11 () throws Exception {
        // ARRANGE
        String id = "10";

        String body = """
                {
                    "tipo": "CACHORRO",
                    "nome": "Nome do Pet",
                    "raca": "Pastor Alemão",
                    "idade": "7",
                    "cor" : "Preto e Marrom",
                    "peso": "25.5"
                }
                """;

        BDDMockito.given(abrigoService.carregarAbrigo(id)).willThrow(ValidacaoException.class);

        // ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos/{id}/pets", id)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

}