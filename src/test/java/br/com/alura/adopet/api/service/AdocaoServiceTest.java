package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoPetDisponivel;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoPetDisponivel validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Test
    @DisplayName("Deveria salvar no banco de dados a adoção feita")
    void salvarAdocaoCenario01() {
        // ARRANGE
        this.dto = new SolicitacaoAdocaoDto(10l, 10l, "Um motivo qualquer");
        BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);

        // ACT
        service.solicitar(dto);

        // ASSERT
        BDDMockito.then(repository).should().save(adocaoCaptor.capture());
        Adocao adocao = adocaoCaptor.getValue();
        Assertions.assertEquals(pet, adocao.getPet());
        Assertions.assertEquals(tutor, adocao.getTutor());
        Assertions.assertEquals(dto.motivo(), adocao.getMotivo());
    }

    @Test
    @DisplayName("Deveria validar se adoção pode ser feita")
    void validarAdocaoCenario02() {
        // ARRANGE
        this.dto = new SolicitacaoAdocaoDto(10l, 10l, "Um motivo qualquer");
        BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        validacoes.add(validador1);
        validacoes.add(validador2);

        // ACT
        service.solicitar(dto);

        // ASSERT
        BDDMockito.then(validador1).should().validar(dto);
        BDDMockito.then(validador2).should().validar(dto);
    }

}