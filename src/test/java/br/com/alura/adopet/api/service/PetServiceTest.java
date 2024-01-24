package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Abrigo abrigo;

    @Mock
    private CadastroPetDto dto;

    @Test
    @DisplayName("Deveria retornar os pets disponíveis para adoção")
    void buscarPetsCenario01 () {
        // ACT
        petService.buscarPetsDisponiveis();

        //ASSERT
        BDDMockito.then(petRepository).should().findAllByAdotadoFalse();
    }

    @Test
    @DisplayName("Deveria salvar o pet no banco de dados")
    void cadastrarPetsCenario02 () {
        // ACT
        petService.cadastrarPet(abrigo, dto);

        //ASSERT
        BDDMockito.then(petRepository).should().save(new Pet(dto, abrigo));
    }

}