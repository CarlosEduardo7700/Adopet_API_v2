package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Abrigo abrigo;

    @Mock
    private CadastroAbrigoDto dto;

    @Test
    @DisplayName("Deveria listar todos os abrigos cadastrados")
    void listarAbrigosCenario01 () {
        // ARRANGE

        // ACT
        abrigoService.listar();

        // ASSERT
        BDDMockito.then(abrigoRepository).should().findAll();
    }

    @Test
    @DisplayName("Deveria retornar uma excessão caso o abrigo já esteja cadastrado")
    void AbrigoJaCadastradoCenario02 () {
        // ARRANGE
        BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(true);

        // ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.cadastrar(dto));
    }

    @Test
    @DisplayName("Deveria salvar no banco de dados caso o abrigo não esteja cadastrado ainda")
    void AbrigoNaoCadastradoCenario03 () {
        // ARRANGE
        BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(false);

        // ACT
        abrigoService.cadastrar(dto);

        // ASSERT
        BDDMockito.then(abrigoRepository).should().save(new Abrigo(dto));
    }

    @Test
    @DisplayName("Deveria recuperar os pets dos abrigos pelo nome")
    void listarPeloNomeCenario04 () {
        // ARRANGE
        String nome = "Nome do Pet";
        BDDMockito.given(abrigoRepository.findByNome(nome)).willReturn(Optional.of(abrigo));

        // ACT
        abrigoService.listarPetsDoAbrigo(nome);

        // ASSERT
        BDDMockito.then(petRepository).should().findByAbrigo(abrigo);

    }

    @Test
    @DisplayName("Deveria recuperar os pets dos abrigos pelo id")
    void listarPeloIdCenario05 () {
        // ARRANGE
        String id = "10";
        BDDMockito.given(abrigoRepository.findById(Long.parseLong(id))).willReturn(Optional.of(abrigo));

        // ACT
        abrigoService.listarPetsDoAbrigo(id);

        // ASSERT
        BDDMockito.then(petRepository).should().findByAbrigo(abrigo);

    }

}