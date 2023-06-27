# APIs de Adaptadores elétricos

Repositório de projeto com APIs para cadastro de pessoas, casas e eletrodomésticos, visando a calcular o consumo mensal de energia.

O link no github é https://github.com/vsantsal/adaptadores-eletricos-api.

## Modelagem básica

Considerando a descrição básica do conjunto de APIs, consideraremos que, para cada Endereço, associamos M Pessoas e N Eletrodomésticos.

![Diagrama de Classes](https://github.com/vsantsal/adaptadores-eletricos-api/blob/main/docs/V1_uml_aparelhos_domesticos.drawio.png)

## Escopo inicial

Implementaremos inicialmente apenas as APIs de cadastro das entidades do domínio do problema.

Ainda não faremos a associação entre as entidades.

Criaremos testes de integração para os controllers para confirmar os principais comportamentos.

Configuraremos *workflow* no Actions para executar os testes em integrações de código no ramo principal (*main*).

### API de Cadastro de Endereços

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

### API de Cadastro de Eletrodomésticos

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
