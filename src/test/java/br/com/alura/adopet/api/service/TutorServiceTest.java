package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private CadastroTutorDto dto;

    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Mock
    private Tutor tutor;

    @Test
    @DisplayName("Deveria retornar uma excessão caso o tutor já esteja cadastrado")
    void TutorJaCadastradoCenario01 () {
        // ARRANGE
        BDDMockito.given(tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(true);

        // ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> tutorService.cadastrar(dto));
    }

    @Test
    @DisplayName("Deveria salvar no banco de dados caso o tutor não esteja cadastrado ainda")
    void TutorNaoCadastradoCenario02 () {
        // ARRANGE
        BDDMockito.given(tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(false);

        // ACT
        tutorService.cadastrar(dto);

        // ASSERT
        BDDMockito.then(tutorRepository).should().save(new Tutor(dto));
    }

    @Test
    @DisplayName("Deveria atualizar os dados do tutor no banco de dados")
    void atualizarDadosCenario03 () {
        // ARRANGE
        BDDMockito.given(tutorRepository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);

        // ACT
        tutorService.atualizar(atualizacaoTutorDto);

        // ASSERT
        BDDMockito.then(tutor).should().atualizarDados(atualizacaoTutorDto);
    }

}