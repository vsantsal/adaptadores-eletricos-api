# APIs de Adaptadores elétricos

Repositório de projeto com APIs para cadastro de pessoas, casas e eletrodomésticos, visando a calcular o consumo mensal de energia.

O link no github é https://github.com/vsantsal/adaptadores-eletricos-api.

## Modelagem básica

Considerando a descrição básica do conjunto de APIs, consideraremos que, para cada Endereço, associamos M Pessoas e N Eletrodomésticos.

![Diagrama de Classes](https://github.com/vsantsal/adaptadores-eletricos-api/blob/main/docs/V1_uml_aparelhos_domesticos.drawio.png)

## Escopo inicial

Implementaremos inicialmente apenas as APIs de cadastro das entidades do domínio do problema.

Utilizaremos uma abordagem TDD, criando os testes de integração para os controllers antes de efetivamente criar o código deles.

A primeira API a ser construida será a de cadastro de endereços.

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
