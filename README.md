# Sistema de Gestão de Biblioteca Municipal

Projeto académico em Java desenvolvido para a disciplina de Introdução a Algoritmos e Programação na **UnISCED**.

## 🚀 Funcionalidades
* **Registo de Obras:** Cadastro de livros com ID automático, título, autor, ano e quantidade em stock.
* **Consulta e Pesquisa:** Listagem geral do catálogo e busca por termos (*case-insensitive*).
* **Empréstimo e Devolução:** Controlo de exemplares com validação de disponibilidade e histórico de requisições.
* **Estatísticas:** Exibição do total de títulos, volume de empréstimos e obra mais requisitada.
* **Validação de Dados:** Leitura segura de entradas numéricas contra falhas de execução (`lerInteiroSeguro`).

## 🛠️ Tecnologias
* **Linguagem:** Java 17
* **Paradigma:** Orientação a Objetos (POO)
* **Estrutura de Dados:** Arrays estáticos

## 💻 Como Executar
1. Compile o ficheiro fonte:
   ```bash
   javac -d bin src/SistemaBiblioteca.java
