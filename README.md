# Atividade Prática: Prototype

Sistema de pedidos de uma distribuidora de alimentos, implementado em Java com o padrão de projeto **Prototype**. Os pedidos-modelo são cadastrados em um catálogo e os novos pedidos são gerados por **cópia profunda** desses modelos: alterar um pedido gerado não modifica o modelo nem os demais pedidos.

## Identificação

| | |
|---|---|
| **Aluna** | Gabriela Pedroso dos Santos Pontes |
| **Professor** | Leandro Fabian Almeida Escobar |
| **Matéria** | Padrões de Projetos |

## Estrutura do projeto

```
pedidos/
    Prototype.java            interface genérica Prototype<T> (RF01)
    ItemPedido.java           linha do pedido, com construtor de cópia (RF02, RF03)
    Pedido.java               pedido, ids sequenciais, clonar() e clonarPorSerializacao() (RF04 a RF07, RF12)
    CatalogoDePedidos.java    catálogo de modelos: registrar() e criar() (RF08, RF09)
    PedidosDemo.java          cenário de teste, critérios de aceitação e desempenho (RF10 a RF12)
```

## Como compilar e executar

A partir da pasta que contém o diretório `pedidos`:

```
javac -encoding UTF-8 -d out pedidos/*.java
java -cp out pedidos.PedidosDemo
```

Para salvar a saída em um arquivo de texto:

```
java -cp out pedidos.PedidosDemo > saida.txt
```

Requer apenas o JDK (sem bibliotecas externas).

## Decisões de implementação

- **Cópia profunda manual (RF06):** `clonar()` cria um novo `Pedido`, copia cliente e endereço e monta uma nova lista com novos `ItemPedido`, usando o construtor de cópia. Não é usado `Cloneable` nem `Object.clone()`.
- **Id e data da cópia (RF05 e RF07):** a cópia passa pelo construtor de `Pedido`, então recebe um novo id sequencial e a data do momento da cópia. O id e a data do original não são copiados.
- **Dinheiro (RT03):** todos os valores monetários usam `BigDecimal`; `valorTotal()` devolve o total com duas casas decimais.
- **Catálogo (RF08 e RF09):** `criar()` sempre devolve uma cópia do modelo e lança `IllegalArgumentException`, informando a chave, quando ela não está registrada.
- **Cópia por serialização (RF12):** `Pedido` e `ItemPedido` implementam `Serializable`. `clonarPorSerializacao()` copia o objeto por meio de streams de bytes e, depois, atribui novo id e nova data, respeitando RF06 e RF07.

## Saída esperada

> Os tempos da última seção variam conforme a máquina; os demais valores devem ser idênticos.

```
Pedido #1 │ (modelo) │ - │ itens=3 │ total=R$ 315,00
Pedido #2 │ Mercado Central │ Rua das Flores, 120 │ itens=3 │ total=R$ 315,00
Pedido #3 │ Padaria Boa Massa │ Av. Paraná, 800 │ itens=3 │ total=R$ 504,00
Pedido #4 │ Restaurante do Porto │ Rua do Cais, 45 │ itens=4 │ total=R$ 387,00

CA-01 ids diferentes: OK
CA-02 total do modelo continua 315,00: OK
CA-03 totais A = 315,00 | B = 504,00 | C = 387,00: OK
CA-04 modelo continua com 3 itens: OK
CA-05 nenhum item das cópias é do modelo: OK
CA-06 criar("inexistente") lança IllegalArgumentException: OK
CA-07 cliente do modelo continua "(modelo)": OK
RF07 cópias criadas com a data de hoje (AAAA-MM-DD): OK
RF12 cópia por serialização é profunda, com novo id e data de hoje: OK

Tempo de 100000 cópias:
clonar(): 28 ms
clonarPorSerializacao(): 2548 ms
```

A linha de RF07 mostra a data em que o programa é executado. Na cópia por serialização, o tempo é bem maior que o da cópia manual, o que era esperado.
