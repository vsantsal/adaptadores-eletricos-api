# APIs de Adaptadores elétricos

Repositório de projeto com APIs para cadastro de pessoas, casas e eletrodomésticos, visando a calcular o consumo mensal de energia.

O link no github é https://github.com/vsantsal/adaptadores-eletricos-api.

## Modelagem básica

Considerando a descrição básica do conjunto de APIs, consideraremos que, para cada Endereço, associamos M Pessoas e N Eletrodomésticos.

## Escopo inicial

Implementaremos inicialmente apenas as APIs de cadastro das entidades do domínio do problema.

Utilizaremos uma abordagem TDD, criando os testes de integração para os controllers antes de efetivamente criar o código deles.

A primeira API a ser construida sera a de cadastro de endereços.
