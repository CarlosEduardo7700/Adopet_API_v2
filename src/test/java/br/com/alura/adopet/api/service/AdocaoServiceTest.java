package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Mock
    private AprovacaoAdocaoDto aprovacaoDto;

    @Mock
    private ReprovacaoAdocaoDto reprovacaoDto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Spy
    private Adocao adocao;

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

    @Test
    @DisplayName("Deveria confirmar o envio do email")
    void enviarEmailCenario03() {
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

        BDDMockito.then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " +adocao.getPet().getAbrigo().getNome() +"!\n\nUma solicitação de adoção foi registrada hoje para o pet: " +adocao.getPet().getNome() +". \nFavor avaliar para aprovação ou reprovação."
        );
    }

    @Test
    @DisplayName("Deveria aprovar uma adoção")
    void aprovarAdocaoCenario04() {
        // ARRANGE
        BDDMockito.given(repository.getReferenceById(aprovacaoDto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        BDDMockito.given(abrigo.getEmail()).willReturn("exemplo@email.com");

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(tutor.getNome()).willReturn("Nome do Tutor");
        BDDMockito.given(pet.getNome()).willReturn("Nome do Pet");
        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());
        BDDMockito.given(abrigo.getNome()).willReturn("Nome do Abrigo");

        // ACT
        service.aprovar(aprovacaoDto);

        // ASSERT
        BDDMockito.then(adocao).should().marcarComoAprovada();
        Assertions.assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
    }

    @Test
    @DisplayName("Deveria reprovar uma adoção")
    void reprovarAdocaoCenario05() {
        // ARRANGE
        BDDMockito.given(repository.getReferenceById(reprovacaoDto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        BDDMockito.given(abrigo.getEmail()).willReturn("exemplo@email.com");

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(tutor.getNome()).willReturn("Nome do Tutor");
        BDDMockito.given(pet.getNome()).willReturn("Nome do Pet");
        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());
        BDDMockito.given(abrigo.getNome()).willReturn("Nome do Abrigo");

        // ACT
        service.reprovar(reprovacaoDto);

        // ASSERT
        BDDMockito.then(adocao).should().marcarComoReprovada(reprovacaoDto.justificativa());
        Assertions.assertEquals(StatusAdocao.REPROVADO, adocao.getStatus());
    }

    @Test
    @DisplayName("Deveria enviar o email de aprovação da adoção")
    void enviarEmailAprovacaoCenario06() {
        // ARRANGE
        BDDMockito.given(repository.getReferenceById(aprovacaoDto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        BDDMockito.given(abrigo.getEmail()).willReturn("exemplo@email.com");

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(tutor.getNome()).willReturn("Nome do Tutor");
        BDDMockito.given(pet.getNome()).willReturn("Nome do Pet");
        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());
        BDDMockito.given(abrigo.getNome()).willReturn("Nome do Abrigo");

        // ACT
        service.aprovar(aprovacaoDto);

        // ASSERT
        BDDMockito.then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Adoção aprovada",
                "Parabéns " +adocao.getTutor().getNome() +"!\n\nSua adoção do pet " +adocao.getPet().getNome() +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi aprovada.\nFavor entrar em contato com o abrigo " +adocao.getPet().getAbrigo().getNome() +" para agendar a busca do seu pet."
        );
    }

    @Test
    @DisplayName("Deveria enviar o email de reprovação da adoção")
    void enviarEmailReprovacaoCenario07() {
        // ARRANGE
        BDDMockito.given(repository.getReferenceById(reprovacaoDto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        BDDMockito.given(abrigo.getEmail()).willReturn("exemplo@email.com");

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(tutor.getNome()).willReturn("Nome do Tutor");
        BDDMockito.given(pet.getNome()).willReturn("Nome do Pet");
        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());
        BDDMockito.given(abrigo.getNome()).willReturn("Nome do Abrigo");

        // ACT
        service.reprovar(reprovacaoDto);

        // ASSERT
        BDDMockito.then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " +adocao.getTutor().getNome() +"!\n\nInfelizmente sua adoção do pet " +adocao.getPet().getNome() +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi reprovada pelo abrigo " +adocao.getPet().getAbrigo().getNome() +" com a seguinte justificativa: " +adocao.getJustificativaStatus()
        );
    }



}