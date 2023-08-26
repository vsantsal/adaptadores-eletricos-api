package com.example.adaptadoreseletricos.domain.entity.pessoa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ParentescoTest {

    @DisplayName("Teste parentesco inverso")
    @ParameterizedTest
    @CsvSource({
            "MAE, FEMININO, FILHA", "MAE, MASCULINO, FILHO",
            "PAI, FEMININO, FILHA", "PAI, MASCULINO, FILHO",
            "FILHO, FEMININO, MAE", "FILHO, MASCULINO, PAI",
            "FILHA, FEMININO, MAE", "FILHA, MASCULINO, PAI",
            "TIO, FEMININO, SOBRINHA", "TIO, MASCULINO, SOBRINHO",
            "TIA, FEMININO, SOBRINHA", "TIA, MASCULINO, SOBRINHO",
            "SOBRINHO, FEMININO, TIA", "SOBRINHO, MASCULINO, TIO",
            "SOBRINHA, FEMININO, TIA", "SOBRINHA, MASCULINO, TIO",
            "IRMAO, FEMININO, IRMA", "IRMAO, MASCULINO, IRMAO",
            "IRMA, FEMININO, IRMA", "IRMA, MASCULINO, IRMAO",
    })
    public void test_inversao_de_parentesco(
            String nomeParentescoInput,
            String nomeSexo,
            String nomeParentescoInversoEsperado
    ){
        // Arrange
        Parentesco parentescoInput = Parentesco
                .valueOf(nomeParentescoInput);
        Parentesco parentescoInversoEsperado = Parentesco
                .valueOf(nomeParentescoInversoEsperado);
        Sexo sexo = Sexo.valueOf(nomeSexo);

        // Act
        Parentesco parentescoInversoObtido = parentescoInput.getInversaoDeParentesco(sexo);

        // Assert
        assertEquals(parentescoInversoEsperado, parentescoInversoObtido);

    }

    @DisplayName("Teste sexo correspondente parentesco")
    @ParameterizedTest
    @CsvSource({
            "PAI, MASCULINO", "MAE, FEMININO",
            "FILHO, MASCULINO", "FILHA, FEMININO",
            "TIO, MASCULINO", "TIA, FEMININO",
            "SOBRINHO, MASCULINO", "SOBRINHA, FEMININO",
            "IRMAO, MASCULINO", "IRMA, FEMININO",
    })
    public void test_derivacao_de_sexo_de_parentesco(
            String nomeParentesco,
            String nomeSexoEsperado
    ){
        // Arrange
        Parentesco parentesco = Parentesco
                .valueOf(nomeParentesco);
        Sexo sexoEsperado = Sexo.valueOf(nomeSexoEsperado);

        // Act
        Sexo sexoObtido = parentesco.getSexoCorrespondente();

        // Assert
        assertEquals(sexoEsperado, sexoObtido);

    }

}
