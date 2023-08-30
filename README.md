APIs de Adaptadores elétricos
=============================

<!-- TOC -->
* [APIs de Adaptadores elétricos](#apis-de-adaptadores-elétricos)
* [👓 Introdução](#-introdução)
* [🧑‍🔬 Modelagem básica](#-modelagem-básica)
* [🔬 Escopo](#-escopo)
* [📖 APIs](#-apis)
  * [APIS  de autenticação](#apis--de-autenticação)
  * [API de Cadastro de Endereços](#api-de-cadastro-de-endereços)
  * [API de Cadastro de Eletrodomésticos](#api-de-cadastro-de-eletrodomésticos)
  * [API de Cadastro de Pessoas](#api-de-cadastro-de-pessoas)
* [🗓️ Resumo Desenvolvimento](#-resumo-desenvolvimento)
  * [Primeira fase](#primeira-fase)
  * [Segunda fase](#segunda-fase)
  * [⚠️ Pontos de atenção](#-pontos-de-atenção)
<!-- TOC -->

# 👓 Introdução

![status_desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em%20Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=java)

![framework_back](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![server_ci](https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

![example workflow](https://github.com/vsantsal/adaptadores-eletricos-api/actions/workflows/maven.yml/badge.svg)
![Coverage](.github/badges/jacoco.svg)

Repositório de projeto com APIs para cadastro de pessoas, casas e eletrodomésticos, visando a calcular o consumo mensal de energia.

O link no github é https://github.com/vsantsal/adaptadores-eletricos-api.

# 🧑‍🔬 Modelagem básica

Considerando os novos requisitos para a segunda fase, apresentamos a seguinte modelagem para as entidades da aplicação na segunda fase.

![Diagrama de Entidade Relacionamento](https://github.com/vsantsal/adaptadores-eletricos-api/blob/main/docs/V5_DER_aparelhos_domesticos.png)

# 🔬 Escopo

Implementaremos as APIs de cadastro das entidades do domínio do problema, com os 4 principais verbos HTTP (GET, POST, PUT, DELETE).


Há testes de integração para os controllers de modo a confirmar os principais comportamentos.

Configuramos *workflow* no Actions para executar os testes em integrações de código no ramo principal (*main*), além de permitir seu *bot* a atualizar a *badge* de cobertura de código pelos testes.

# 📖 APIs

Abaixo, descrevemos globalmente as APIs implementadas.

Observar que o projeto se vale do *Swagger* para gerar documentação automaticamente, nos formatos *HTML*, *JSON* e *YAML*, nos *endpoints* padrão (`swagger-ui.html` e `v3/api-docs`).

## APIS  de autenticação

Nossa API Rest deve suportar cadastro e posterior login para usuários, disponíveis nos *endpoints* `auth/registrar` e `auth/login`, respectivamente.

Para o POST em `auth/registrar`, o *body* de cada requisição deve informar JSON no seguinte formato:

```json
{
    "login": "usuario.teste",
    "senha": "123456789",
    "pessoa": {
        "nome": "Fulano de tal",
        "dataNascimento": "1980-01-01",
        "sexo": "MASCULINO"
    }
}

```

Em caso de cadastro bem sucedido, a aplicação retorna resposta com status HTTP usual (200).

Caso haja nova tentativa de cadastro, a aplicação retornará o erro informando, conforme abaixo:

```json
{
    "mensagem": "Usuário já cadastrado"
}
```

Para o POST em `auth/login`, o *body* de cada requisição deve informar JSON no seguinte formato:

```json
{
    "login": "usuario.teste",
    "senha": "123456789"
}
```

Em caso de login inválido, a aplicação retorna o status 403 (sem mensagem).

Em caso de login bem sucedido, a aplicação retornará token JWT que o cliente deverá informar a cada nova solicitação.

## API de Cadastro de Endereços

Nossa API Rest deve suportar a manutenção do cadastro de endereços, sobre os quais se calculará o consumo (mensal) de energia.

O enpdpoint será baseado em `/enderecos`, suportando os métodos HTTP POST, GET, UPDATE, DELETE.

Para o POST, o *body* de cada requisição deve informar JSON no seguinte formato:

```json 
{
  "rua": "Rua Nascimento Silva",
  "numero": 107,
  "bairro": "Ipanema",
  "cidade": "Rio de Janeiro",
  "estado": "RJ"
}
```

Em caso de sucesso, a aplicação deve informar a *location* do recurso criado. O endereço cadastrado automaticamente estará associado ao usuário logado.

Se falha nos dados passados pelos clientes, deve informar o erro.

Por exemplo, caso cliente passe número de rua negativo, o sistema devolverá:

```json
[
    {
        "campo": "numero",
        "mensagem": "número de rua deve ser positivo"
    }
]
```

Para o DELETE, deve-se passar o id do endereço a remover no endpoint (por exemplo, `enderecos/73`). A aplicação marcará a associação entre usuário e o endereço como inativa e retornará o STATUS CODE 204. Um usuário logado somente poderá excluir endereços em que residam.

Para o UPDATE, deve-se passar  o id do endereco a atualizar no endpoint (por exemplo, `enderecos/73`) e os novos valores para os campos no corpo da requisição, conforme abaixo:

```json 
{
  "rua": "Rua Nascimento Silva",
  "numero": 207,
  "bairro": "Ipanema",
  "cidade": "Rio de Janeiro",
  "estado": "RJ"
}
```

A aplicação fará as atualizações dos campos e retornará o STATUS CODE 200, em caso de sucesso. 

Um usuário logado somente poderá atualizar endereços em que residam.

O GET no endpoint pode ser realizado complementando com ID ou não.

Se ID for informado, retornará o endereço buscado.

Sem ID, todos endereços associados ao usuário serão listado. Pode-se ainda pesquisar pelos campos `rua`, `numero`, `bairro`, `cidade` e `estado`.


## API de Cadastro de Eletrodomésticos

Nossa API Rest deve suportar a manutenção de eletrodomésticos.

O enpdpoint será baseado em `/eletrodomesticos`, suportando os métodos HTTP POST, GET, UPDATE, DELETE.

Para o cadastro, o *body* de cada requisição deve informar JSON no seguinte formato:

```json 
{
  "nome": "Aparelho de som",
  "modelo": "XPTO",
  "marca": "ABC",
  "potencia": 220,
  "idEndereco": 1
}
```

Em caso de sucesso, a aplicação deve informar a *location* do recurso criado.

Importante observar que o eletrodoméstico cadastrado é automaticamente associado ao usuário logado.

Se falha nos dados passados pelos clientes, deve informar o erro.

Por exemplo, se houver tentativa de cadastro de aparelho com potência negativa, conforme corpo da requisição abaixo:

```json
{
  "nome": "Aparelho de som",
  "modelo": "XPTO",
  "marca": "ABC",
  "potencia": -220,
  "idEndereco": 1
}
```
A aplicação retornará a mensagem de erro abaixo (respota com status HTTP 400):

```json
[
    {
        "campo": "potencia",
        "mensagem": "potencia deve ser número inteiro positivo"
    }
]
```

Para o DELETE, deve-se passar o id do eletrodoméstico a remover no endpoint (por exemplo, `eletrodomesticos/101`). A aplicação marcará a associação entre usuário e o eletrodoméstico como inativa e retornará o STATUS CODE 204. Um usuário logado somente poderá excluir eletrodomésticos que possua.

Para o UPDATE, deve-se passar  o id do eletrodoméstico a atualizar no endpoint (por exemplo, `eletrodomesticos/101`) e os novos valores para os campos no corpo da requisição, conforme abaixo:

```json 
{
  "nome": "Aparelho de som",
  "modelo": "XPTO",
  "marca": "ABC",
  "potencia": 110,
  "idEndereco": 1
}
```

A aplicação fará as atualizações dos campos e retornará o STATUS CODE 200, em caso de sucesso.

O GET no endpoint pode ser realizado complementando com ID ou não.

Se ID for informado, retornará o eletrodoméstico buscado.

Sem ID, todos eletrodomésticos associados ao usuário serão listado. Pode-se ainda pesquisar pelos campos `nome`, `marca`, `modelo`, `potencia` e `idEndereco`.

## API de Cadastro de Pessoas

Nossa API Rest deve suportar a manutenção de pessoas.

O enpdpoint será baseado em `/pessoas`, suportando os métodos HTTP POST, GET, UPDATE, DELETE.

Para o cadastro, o *body* de cada requisição deve informar JSON no seguinte formato:

```json 
{
  "nome": "Fulano de tal",
  "dataNascimento": "1980-01-01",
  "sexo": "MASCULINO",
  "parentesco": "FILHO"
}
```

Em caso de sucesso, a aplicação deve informar a *location* do recurso criado.

Importante observar que o parentesco informado é relativo ao usuário logado.

Se falha nos dados passados pelos clientes, deve informar o erro.

Por exemplo, caso cliente passe sexo e parentesco de pessoas incoerentes, a aplicação informará a resposta abaixo:

```json
{
    "mensagem": "Sexo e Parentesco informados incompatíveis"
}
```

Para o DELETE, deve-se passar o id da pessoa a remover no endpoint (por exemplo, `pessoas/42`). A aplicação promoverá a exclusão e retornar o STATUS CODE 204. Um usuário logado somente poderá excluir pessoa com a qual possua parentesco.

Para o UPDATE, deve-se passar  o id da pessoa a atualizar no endpoint (por exemplo, `pessoas/42`) e os novos valores para os campos no corpo da requisição, conforme abaixo:

```json 
{
  "nome": "Fulano de tal 2",
  "dataNascimento": "1980-01-01",
  "sexo": "MASCULINO",
  "parentesco": "PAI"
}
```

A aplicação fará as atualizações dos campos e retornará o STATUS CODE 200, em caso de sucesso. Um usuário logado somente poderá atualizar pessoa com a qual possua parentesco.

O GET no endpoint pode ser realizado complementando com ID ou não.

Se ID for informado, retornará a pessoa buscada. 

Sem ID, todas as pessoas com parentesco serão apresentadas. Pode-se utilizar ainda `nome`, `sexo`, `dataNascimento`e `parentesco` com parâmetros de pesquisa. 

# 🐳 Contêineres

Disponibilizamos imagem para que usuários possam rodar localmente a aplicação.

Nessa primeira versão, apenas em modo de "testes", isto é, utilizando banco de dados em memória.

Como entrega futura, ficamos de adicionar o *pull* de imagem de banco de dados de "produção" (MySQL), e sua comunicação com a aplicação.

Para rodar, basta executar:

`docker-compose up --build`

Interrompe-se o contêiner por meio do comando:

`docker-compose down`

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

* Implementação (entre a primeira e a segunda) de métrica de cobertura de código pelos testes, com habilitação do *github-actions bot* para gerar *badge*;
* Para cadastro de usuários e login na aplicação, adicionamos dependências [*Spring Security*](https://spring.io/projects/spring-security) e [*auth0/java-jwt*](https://github.com/auth0/java-jwt), baseados principalmente no curso [Spring Boot 3: aplique boas práticas e proteja uma API Rest](https://www.alura.com.br/curso-online-spring-boot-aplique-boas-praticas-proteja-api-rest) da Alura e no tutorial [Autenticação e Autorização com Spring Security e JWT Tokens](https://www.youtube.com/watch?v=5w-YCcOjPD0), de Fernanda Kipper;
* Naturalmente, foi necessário atualizar os testes para considerar a nova dependência de segurança do projeto, por meio de anotações `@ActiveProfiles`, `@SpringBootTest`, `@AutoConfigureMockMvc`, além do método `.with(user())`;
* Adicionamos dependência `h2` para execução dos testes no *Github Actions*;
* Para criarmos o relacionamento de parentes entre pessoas, do tipo M:N, nos baseamos fortemente neste [tutorial do Baldeung](https://www.baeldung.com/jpa-many-to-many);
* Para criarmos *custom queries* que atualizassem a base no repositório da entidade associativa ParentescoPessoas, consultamos este [tutorial do Baldeung](https://www.baeldung.com/spring-data-jpa-modifying-annotation);
* Haja vista a criação de consultas personalizadas, fizemos também teste de repositório para validar nossa implementação;
* Retiramos uso de *mocks* para *repositories* - para garantir corretos *set up* e *tear down* entre execuções, adicionamos a *annotation* `@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)` às classes de controllers e repositories;
* Incluímos documentação dinâmica por meio de *swagger*, adicionando a dependência [SpringDoc](https://springdoc.org/)
* Incluímos `Dockerfile` e `docker-compose.yaml` para disponibilizar imagem de modo a se rodar a aplicação em modo de testes (com banco de dados em memória)

## ⚠️ Pontos de atenção

* Pendente de avaliação ainda a escabilidade da solução atual, especialmente no tocante à implementação dos relacionamentos N:N e 1:N;
* Pendente de disponibilizarmos imagem com banco de dados MySQL para se rodar a aplicação;
* Após a pendência anterior, adicionarmos no pipiline de CI/CD a construção e publicação da imagem.