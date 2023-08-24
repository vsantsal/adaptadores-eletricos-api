APIs de Adaptadores elétricos
=============================

<!-- TOC -->
* [APIs de Adaptadores elétricos](#apis-de-adaptadores-elétricos)
* [👓 Introdução](#-introdução)
* [🧑‍🔬 Modelagem básica](#-modelagem-básica)
* [🔬 Escopo](#-escopo)
* [📖 APIs](#-apis)
  * [API de Cadastro de Endereços](#api-de-cadastro-de-endereços)
  * [API de Cadastro de Eletrodomésticos](#api-de-cadastro-de-eletrodomésticos)
  * [API de Cadastro de Pessoas](#api-de-cadastro-de-pessoas)
* [🗓️ Resumo Desenvolvimento](#-resumo-desenvolvimento)
  * [Primeira fase](#primeira-fase)
  * [Segunda fase](#segunda-fase)
<!-- TOC -->

# 👓 Introdução

![status_desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em%20Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=java)

![framework_back](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![server_ci](https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe)

![example workflow](https://github.com/vsantsal/adaptadores-eletricos-api/actions/workflows/maven.yml/badge.svg)
![Coverage](.github/badges/jacoco.svg)

Repositório de projeto com APIs para cadastro de pessoas, casas e eletrodomésticos, visando a calcular o consumo mensal de energia.

O link no github é https://github.com/vsantsal/adaptadores-eletricos-api.

# 🧑‍🔬 Modelagem básica

Considerando a descrição básica do conjunto de APIs, consideraremos que, para cada Endereço, associamos M Pessoas e N Eletrodomésticos.

![Diagrama de Classes](https://github.com/vsantsal/adaptadores-eletricos-api/blob/main/docs/V1_uml_aparelhos_domesticos.drawio.png)

# 🔬 Escopo

Implementaremos as APIs de cadastro das entidades do domínio do problema, com os 4 principais verbos HTTP (GET, POST, PUT, DELETE).


Criaremos testes de integração para os controllers para confirmar os principais comportamentos.

Configuraremos *workflow* no Actions para executar os testes em integrações de código no ramo principal (*main*).

# 📖 APIs
## API de Cadastro de Endereços

Nossa API Rest deve suportar a inclusão no cadastro de endereços, sobre os quais se calculará o consumo (mensal) de energia.

O enpdpoint para o cadastro será `/enderecos`, através do método HTTP POST.

O *body* de cada requisição deve informar JSON no seguinte formato:

```json 
{
  "rua": "Rua Nascimento Silva",
  "numero": 107,
  "bairro": "Ipanema",
  "cidade": "Rio de Janeiro",
  "estado": "RJ"
}
```

Em caso de sucesso, a aplicação deve informar a *location* do recurso criado.

Se falha nos dados passados pelos clientes, deve informar o erro.

## API de Cadastro de Eletrodomésticos

Nossa API Rest deve suportar a inclusão no cadastro de eletrodomésticos.

O enpdpoint para o cadastro será `/eletrodomesticos`, através do método HTTP POST.

O *body* de cada requisição deve informar JSON no seguinte formato:

```json 
{
  "nome": "Aparelho de som",
  "modelo": "XPTO",
  "marca": "ABC",
  "potencia": 200
}
```

Em caso de sucesso, a aplicação deve informar a *location* do recurso criado.

Se falha nos dados passados pelos clientes, deve informar o erro.

## API de Cadastro de Pessoas

Nossa API Rest deve suportar a inclusão no cadastro de pessoas.

O enpdpoint para o cadastro será `/pessoas`, através do método HTTP POST.

O *body* de cada requisição deve informar JSON no seguinte formato:

```json 
{
  "nome": "Fulano de tal",
  "dataNascimento": "1980-01-01",
  "sexo": "MASCULINO",
  "parentesco": "FILHO"
}
```

Em caso de sucesso, a aplicação deve informar a *location* do recurso criado.

Se falha nos dados passados pelos clientes, deve informar o erro.

# 🗓️ Resumo Desenvolvimento

## Primeira fase

* O SGBD utilizado é o MySQL, conforme pode ser lido pelas dependências no `pom.xml`. Para versionamento e *migrations* dos scripts de criação de tabela, utilizamos a dependência `flyway`.
* Adotamos testes de integração sobre os *controllers* para verificar se comportamento da solução está de acordo com o esperado. Para tanto, consultamos, dentre outras, as seguintes fontes:
  * https://rieckpil.de/guide-to-testing-with-spring-boot-starter-test/
  * https://www.baeldung.com/spring-boot-testing
* Nos testes, preferimos *mockar* os repositórios, haja vista não termos adicionado nenhuma inteligência a eles, apenas herdamos as implementação da interface *JpaRepository*, em vez dos *services*, que codamos e poderiam ter alguma lógica escrita erroneamente (para tanto, anotamos essas últimas dependências nas classes de testes como `SpyBean`, que, segundo a [documentação](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/SpyBean.html), faz utilizar a implementação real)
* Configuramos *workflow* de execução de testes automáticos quando houver integração aos ramos principais (`develop`e `main`) por meio do *Github Actions*
* Implementamos também métodos `GET` para os *endpoints*, haja vista a criação dos recursos pelo método *POST* em cada um desses endpoints.

## Segunda fase

* Implementação (entre a primeira e a segunda) de métrica de cobertura de código pelos testes, com habilitação do *github-actions bot* para gerar *badge*