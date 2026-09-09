// =============================================================================
// SISTEMA DE GESTÃO DE BIBLIOTECA MUNICIPAL — UnISCED
// Ficheiro  : SistemaBiblioteca.java
// Linguagem : Java 17 (JDK 17)
// Autores   : [Nome do Aluno / Grupo]
// Data      : 2026
// Descricao : Sistema de Gestao de Biblioteca Municipal em Consola (CLI).
//             Utiliza um array estatico de objetos Livro[] (capacidade 100)
//             para simular armazenamento em memoria, sem base de dados
//             externa nem bibliotecas de terceiros.
// =============================================================================

import java.util.Scanner;

/**
 * Classe principal — ponto de entrada da aplicacao.
 */
public class SistemaBiblioteca {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.iniciar();
    }
}

// =============================================================================
// CLASSE: Livro
// Representa uma obra no acervo bibliografico.
// =============================================================================

/**
 * Entidade Livro — modela cada titulo catalogado na biblioteca.
 * Contem metadados do livro e contadores de gestao de emprestimos.
 */
class Livro {

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------

    /** Identificador unico gerado automaticamente pelo sistema. */
    private int id;

    /** Titulo da obra. */
    private String titulo;

    /** Nome do(s) autor(es). */
    private String autor;

    /** Ano de publicacao da obra. */
    private int anoPublicacao;

    /** Quantidade total de exemplares disponiveis no acervo. */
    private int quantidade;

    /**
     * Total de emprestimos efectuados para este titulo (contador acumulado).
     * Nunca e decrementado nas devolucoes — serve apenas para estatisticas.
     */
    private int totalEmprestimos;

    /** Nome do ultimo requisitante que efectuou emprestimo deste livro. */
    private String ultimoRequisitante;

    // -------------------------------------------------------------------------
    // CONSTRUTOR
    // -------------------------------------------------------------------------

    /**
     * Cria um novo Livro com os dados fornecidos.
     *
     * @param id            ID unico gerado pelo sistema.
     * @param titulo        Titulo da obra.
     * @param autor         Autor(es) da obra.
     * @param anoPublicacao Ano de publicacao.
     * @param quantidade    Numero de exemplares disponiveis inicialmente.
     */
    public Livro(int id, String titulo, String autor, int anoPublicacao, int quantidade) {
        this.id               = id;
        this.titulo           = titulo;
        this.autor            = autor;
        this.anoPublicacao    = anoPublicacao;
        this.quantidade       = quantidade;
        this.totalEmprestimos = 0;           // Inicia sem emprestimos
        this.ultimoRequisitante = "N/A";     // Nenhum requisitante ainda
    }

    // -------------------------------------------------------------------------
    // GETTERS E SETTERS
    // -------------------------------------------------------------------------

    public int getId()                     { return id; }
    public String getTitulo()              { return titulo; }
    public String getAutor()               { return autor; }
    public int getAnoPublicacao()          { return anoPublicacao; }
    public int getQuantidade()             { return quantidade; }
    public int getTotalEmprestimos()       { return totalEmprestimos; }
    public String getUltimoRequisitante()  { return ultimoRequisitante; }

    /**
     * Decrementa a quantidade disponivel quando um exemplar e emprestado.
     * Incrementa o contador acumulado de emprestimos.
     *
     * @param requisitante Nome da pessoa que efectua o emprestimo.
     */
    public void registarEmprestimo(String requisitante) {
        this.quantidade--;
        this.totalEmprestimos++;
        this.ultimoRequisitante = requisitante;
    }

    /**
     * Incrementa a quantidade disponivel quando um exemplar e devolvido.
     */
    public void registarDevolucao() {
        this.quantidade++;
    }

    /**
     * Representacao textual formatada do livro para listagem em tabela.
     *
     * @return String formatada com os dados do livro.
     */
    @Override
    public String toString() {
        return String.format("| %-4d | %-35s | %-20s | %-4d | %-5d | %-11d | %-20s |",
                id, titulo, autor, anoPublicacao, quantidade,
                totalEmprestimos, ultimoRequisitante);
    }
}

// =============================================================================
// CLASSE: Biblioteca
// Contem toda a logica de negocio do sistema.
// =============================================================================

/**
 * Nucleo operacional do sistema de biblioteca.
 * Gere o array estatico de livros e implementa todos os fluxos do menu.
 */
class Biblioteca {

    // -------------------------------------------------------------------------
    // CONSTANTES E ESTRUTURA DE DADOS
    // -------------------------------------------------------------------------

    /** Capacidade maxima do acervo (array estatico). */
    private static final int CAPACIDADE_MAXIMA = 100;

    /** Array estatico de livros — estrutura de dados principal em memoria. */
    private Livro[] acervo;

    /**
     * Contador do numero actual de livros registados.
     * Tambem funciona como base para o proximo ID.
     */
    private int totalLivros;

    /** Objecto Scanner partilhado por toda a classe para leitura de entradas. */
    private Scanner scanner;

    // -------------------------------------------------------------------------
    // CONSTRUTOR
    // -------------------------------------------------------------------------

    /**
     * Inicializa a estrutura de dados e o leitor de entradas.
     */
    public Biblioteca() {
        this.acervo      = new Livro[CAPACIDADE_MAXIMA];
        this.totalLivros = 0;
        this.scanner     = new Scanner(System.in);
    }

    // =========================================================================
    // METODO PRINCIPAL: iniciar()
    // Loop do menu principal.
    // =========================================================================

    /**
     * Apresenta o menu principal em loop ate o utilizador escolher sair.
     */
    public void iniciar() {
        exibirCabecalho();

        int opcao = -1;

        do {
            exibirMenuPrincipal();
            opcao = lerInteiroSeguro("Opcao: ", 0, 5);

            switch (opcao) {
                case 1 -> registarLivro();
                case 2 -> menuConsultas();
                case 3 -> efectuarEmprestimo();
                case 4 -> efectuarDevolucao();
                case 5 -> exibirEstatisticas();
                case 0 -> System.out.println("\n  Ate breve! Sistema encerrado com sucesso.\n");
                default -> System.out.println("  [ERRO] Opcao invalida.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // =========================================================================
    // INTERFACE — Metodos de apresentacao visual
    // =========================================================================

    /** Exibe o cabecalho inicial da aplicacao. */
    private void exibirCabecalho() {
        System.out.println("==============================================================");
        System.out.println("   SISTEMA DE GESTAO DE BIBLIOTECA MUNICIPAL --- UnISCED");
        System.out.println("                Versao 1.0  |  Java 17");
        System.out.println("==============================================================");
    }

    /** Exibe o menu principal. */
    private void exibirMenuPrincipal() {
        System.out.println("\n+------------------------------------+");
        System.out.println("|          MENU PRINCIPAL            |");
        System.out.println("+------------------------------------+");
        System.out.println("|  1. Registar Livro                 |");
        System.out.println("|  2. Consultar Catalogo             |");
        System.out.println("|  3. Efectuar Emprestimo            |");
        System.out.println("|  4. Registar Devolucao             |");
        System.out.println("|  5. Ver Estatisticas               |");
        System.out.println("|  0. Sair                           |");
        System.out.println("+------------------------------------+");
    }

    /** Imprime uma linha separadora horizontal para tabelas. */
    private void imprimirSeparadorTabela() {
        System.out.println("+------+-------------------------------------+----------------------+------+-------+-------------+----------------------+");
    }

    /** Imprime o cabecalho da tabela de listagem de livros. */
    private void imprimirCabecalhoTabela() {
        imprimirSeparadorTabela();
        System.out.printf("| %-4s | %-35s | %-20s | %-4s | %-5s | %-11s | %-20s |%n",
                "ID", "TITULO", "AUTOR", "ANO", "QTD", "EMPRESTIMOS", "ULTIMO REQUIS.");
        imprimirSeparadorTabela();
    }

    // =========================================================================
    // FUNCIONALIDADE 1 — Registo de Livros
    // =========================================================================

    /**
     * Regista um novo livro no acervo.
     * O ID e atribuido automaticamente (incremental).
     * Valida capacidade maxima, ano de publicacao e quantidade minima.
     */
    private void registarLivro() {
        System.out.println("\n--- REGISTAR NOVO LIVRO -------------------------------------------");

        // Verificar se o acervo ainda tem espaco disponivel
        if (totalLivros >= CAPACIDADE_MAXIMA) {
            System.out.println("  [AVISO] Acervo lotado! Capacidade maxima de "
                    + CAPACIDADE_MAXIMA + " livros atingida.");
            return;
        }

        // Atribuicao automatica de ID incremental
        int novoId = totalLivros + 1;
        System.out.println("  ID atribuido automaticamente: " + novoId);

        // Leitura do titulo (campo obrigatorio)
        String titulo;
        do {
            System.out.print("  Titulo do livro : ");
            titulo = scanner.nextLine().trim();
            if (titulo.isEmpty()) {
                System.out.println("  [ERRO] O titulo nao pode estar vazio.");
            }
        } while (titulo.isEmpty());

        // Leitura do autor (campo obrigatorio)
        String autor;
        do {
            System.out.print("  Autor           : ");
            autor = scanner.nextLine().trim();
            if (autor.isEmpty()) {
                System.out.println("  [ERRO] O nome do autor nao pode estar vazio.");
            }
        } while (autor.isEmpty());

        // Leitura do ano de publicacao com validacao de intervalo
        // Considera anos validos entre 868 (primeiro livro impresso) e o ano actual
        int anoAtual = java.time.Year.now().getValue();
        int anoPublicacao = lerInteiroSeguro("  Ano de publicacao (868-" + anoAtual + "): ", 868, anoAtual);

        // Leitura da quantidade com validacao (minimo 1 exemplar)
        int quantidade = lerInteiroSeguro("  Quantidade de exemplares (min. 1): ", 1, Integer.MAX_VALUE);

        // Criacao e insercao do novo livro no array
        acervo[totalLivros] = new Livro(novoId, titulo, autor, anoPublicacao, quantidade);
        totalLivros++;

        System.out.println("\n  [OK] Livro \"" + titulo + "\" registado com sucesso! (ID: " + novoId + ")");
    }

    // =========================================================================
    // FUNCIONALIDADE 2 — Consulta de Catalogo
    // =========================================================================

    /**
     * Sub-menu de consultas: listagem completa ou pesquisa por titulo/autor.
     */
    private void menuConsultas() {
        System.out.println("\n--- CONSULTAR CATALOGO --------------------------------------------");

        if (acervoVazio()) return;

        System.out.println("  1. Listar todos os livros");
        System.out.println("  2. Pesquisar por titulo ou autor");
        System.out.println("  0. Voltar ao menu principal");

        int opcao = lerInteiroSeguro("  Opcao: ", 0, 2);

        switch (opcao) {
            case 1 -> listarTodosLivros();
            case 2 -> pesquisarLivros();
            case 0 -> { /* Voltar */ }
        }
    }

    /**
     * Lista todos os livros do acervo em formato de tabela formatada.
     */
    private void listarTodosLivros() {
        System.out.println("\n  CATALOGO COMPLETO (" + totalLivros + " titulo(s) registado(s)):\n");
        imprimirCabecalhoTabela();

        for (int i = 0; i < totalLivros; i++) {
            System.out.println(acervo[i].toString());
        }

        imprimirSeparadorTabela();
    }

    /**
     * Pesquisa livros por titulo ou autor (insensivel a maiusculas/minusculas).
     * Usa String.toLowerCase() para normalizar a comparacao.
     * Exibe todos os resultados que contenham o termo pesquisado.
     */
    private void pesquisarLivros() {
        System.out.print("\n  Termo de pesquisa (titulo ou autor): ");
        String termo = scanner.nextLine().trim().toLowerCase();

        if (termo.isEmpty()) {
            System.out.println("  [ERRO] O termo de pesquisa nao pode estar vazio.");
            return;
        }

        boolean encontrou = false;
        System.out.println("\n  RESULTADOS DA PESQUISA por \"" + termo + "\":\n");
        imprimirCabecalhoTabela();

        for (int i = 0; i < totalLivros; i++) {
            // Comparacao insensivel a maiusculas/minusculas (case-insensitive)
            boolean tituloCorresponde = acervo[i].getTitulo().toLowerCase().contains(termo);
            boolean autorCorresponde  = acervo[i].getAutor().toLowerCase().contains(termo);

            if (tituloCorresponde || autorCorresponde) {
                System.out.println(acervo[i].toString());
                encontrou = true;
            }
        }

        imprimirSeparadorTabela();

        if (!encontrou) {
            System.out.println("  Nenhum resultado encontrado para \"" + termo + "\".");
        }
    }

    // =========================================================================
    // FUNCIONALIDADE 3 — Gestao de Emprestimos
    // =========================================================================

    /**
     * Efectua o emprestimo de um livro.
     * Valida:
     *   - Existencia do ID no acervo.
     *   - Disponibilidade de stock (quantidade > 0).
     * Regista o nome do requisitante e actualiza contadores.
     */
    private void efectuarEmprestimo() {
        System.out.println("\n--- EFECTUAR EMPRESTIMO -------------------------------------------");

        if (acervoVazio()) return;

        // Solicitar o ID do livro a emprestar
        int id = lerInteiroSeguro("  ID do livro a emprestar: ", 1, Integer.MAX_VALUE);
        Livro livro = buscarLivroPorId(id);

        // Verificar se o livro existe no acervo
        if (livro == null) {
            System.out.println("  [ERRO] Livro com ID " + id + " nao encontrado no acervo.");
            return;
        }

        System.out.println("  Livro encontrado: \"" + livro.getTitulo() + "\" --- " + livro.getAutor());

        // Verificar disponibilidade de stock
        if (livro.getQuantidade() <= 0) {
            System.out.println("  [AVISO] Nao ha exemplares disponiveis para emprestimo.");
            System.out.println("          Total de emprestimos deste titulo: " + livro.getTotalEmprestimos());
            return;
        }

        // Leitura do nome do requisitante (campo obrigatorio)
        String requisitante;
        do {
            System.out.print("  Nome do requisitante: ");
            requisitante = scanner.nextLine().trim();
            if (requisitante.isEmpty()) {
                System.out.println("  [ERRO] O nome do requisitante nao pode estar vazio.");
            }
        } while (requisitante.isEmpty());

        // Processar o emprestimo: decremento de stock + incremento de contador
        livro.registarEmprestimo(requisitante);

        System.out.println("\n  [OK] Emprestimo efectuado com sucesso!");
        System.out.printf ("    Livro    : %s%n", livro.getTitulo());
        System.out.printf ("    Requis.  : %s%n", requisitante);
        System.out.printf ("    Stock    : %d exemplar(es) restante(s)%n", livro.getQuantidade());
    }

    // =========================================================================
    // FUNCIONALIDADE 4 — Devolucao de Livros
    // =========================================================================

    /**
     * Regista a devolucao de um livro emprestado.
     * Valida:
     *   - Existencia do ID no acervo.
     *   - Se o livro ja foi alguma vez emprestado (totalEmprestimos > 0).
     *
     * NOTA DE OPTIMIZACAO:
     *   Para detectar se ha emprestimos activos sem um campo extra,
     *   verificamos se totalEmprestimos > 0 como indicador de actividade.
     *   Uma abordagem mais robusta usaria um campo "quantidadeInicial"
     *   ou um contador "emprestimosActivos" separado.
     */
    private void efectuarDevolucao() {
        System.out.println("\n--- REGISTAR DEVOLUCAO --------------------------------------------");

        if (acervoVazio()) return;

        // Solicitar o ID do livro a devolver
        int id = lerInteiroSeguro("  ID do livro a devolver: ", 1, Integer.MAX_VALUE);
        Livro livro = buscarLivroPorId(id);

        // Verificar se o livro existe no acervo
        if (livro == null) {
            System.out.println("  [ERRO] Livro com ID " + id + " nao encontrado no acervo.");
            return;
        }

        System.out.println("  Livro encontrado: \"" + livro.getTitulo() + "\" --- " + livro.getAutor());

        // Verificar se ha emprestimos registados
        if (livro.getTotalEmprestimos() == 0) {
            System.out.println("  [AVISO] Este livro nunca foi emprestado. Nada a devolver.");
            return;
        }

        // Confirmacao de devolucao
        System.out.print("  Confirmar devolucao? (s/n): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();

        if (!confirmacao.equals("s")) {
            System.out.println("  Devolucao cancelada.");
            return;
        }

        // Processar a devolucao: reposicao de stock
        livro.registarDevolucao();

        System.out.println("\n  [OK] Devolucao registada com sucesso!");
        System.out.printf ("    Livro    : %s%n", livro.getTitulo());
        System.out.printf ("    Stock    : %d exemplar(es) disponivel(eis)%n", livro.getQuantidade());
    }

    // =========================================================================
    // FUNCIONALIDADE 5 — Estatisticas
    // =========================================================================

    /**
     * Apresenta estatisticas globais do sistema:
     *   1. Total de titulos registados.
     *   2. Volume global de emprestimos (soma acumulada de todos os livros).
     *   3. Livro mais requisitado (com maior totalEmprestimos).
     *
     * CORRECCAO LOGICA APLICADA:
     *   - O volume global e calculado por soma iterativa (nao por campo separado)
     *     para evitar inconsistencias caso uma variavel global fosse esquecida.
     *   - A pesquisa do livro mais requisitado usa comparacao estrita '>'
     *     para que, em caso de empate, o primeiro encontrado seja mantido.
     */
    private void exibirEstatisticas() {
        System.out.println("\n--- ESTATISTICAS DO SISTEMA ---------------------------------------");

        if (acervoVazio()) return;

        // --- 1. Total de titulos ---
        System.out.printf("%n  %-35s: %d%n", "Total de titulos no acervo", totalLivros);

        // --- 2. Volume global de emprestimos (soma iterativa correcta) ---
        int volumeGlobal = 0;
        for (int i = 0; i < totalLivros; i++) {
            volumeGlobal += acervo[i].getTotalEmprestimos();
        }
        System.out.printf("  %-35s: %d%n", "Volume global de emprestimos", volumeGlobal);

        // --- 3. Livro mais requisitado ---
        // Assume o primeiro como maximo inicial
        int indiceMaisRequisitado = 0;

        for (int i = 1; i < totalLivros; i++) {
            // CORRECCAO: '>' garante que em empate mantemos o primeiro encontrado
            if (acervo[i].getTotalEmprestimos() > acervo[indiceMaisRequisitado].getTotalEmprestimos()) {
                indiceMaisRequisitado = i;
            }
        }

        Livro maisRequisitado = acervo[indiceMaisRequisitado];

        System.out.println("\n  +-- LIVRO MAIS REQUISITADO ------------------------------------+");
        System.out.printf("  |  Titulo       : %-40s|%n", maisRequisitado.getTitulo());
        System.out.printf("  |  Autor        : %-40s|%n", maisRequisitado.getAutor());
        System.out.printf("  |  Emprestimos  : %-40d|%n", maisRequisitado.getTotalEmprestimos());
        System.out.printf("  |  Stock actual : %-40d|%n", maisRequisitado.getQuantidade());
        System.out.println("  +--------------------------------------------------------------+");

        // Caso especial: nenhum livro foi emprestado ainda
        if (volumeGlobal == 0) {
            System.out.println("\n  [INFO] Ainda nao foram registados emprestimos no sistema.");
        }
    }

    // =========================================================================
    // METODOS AUXILIARES PRIVADOS
    // =========================================================================

    /**
     * Verifica se o acervo esta vazio e emite mensagem informativa.
     *
     * @return true se nao existem livros registados; false caso contrario.
     */
    private boolean acervoVazio() {
        if (totalLivros == 0) {
            System.out.println("  [INFO] O acervo esta vazio. Registe livros primeiro (opcao 1).");
            return true;
        }
        return false;
    }

    /**
     * Pesquisa um livro no acervo pelo seu ID (pesquisa linear O(n)).
     * Retorna null se o ID nao existir.
     *
     * @param id Identificador unico do livro.
     * @return O objecto Livro correspondente, ou null se nao encontrado.
     */
    private Livro buscarLivroPorId(int id) {
        for (int i = 0; i < totalLivros; i++) {
            if (acervo[i].getId() == id) {
                return acervo[i];
            }
        }
        return null; // ID nao encontrado
    }

    /**
     * Leitura segura de um inteiro dentro de um intervalo [min, max].
     *
     * PROBLEMA RESOLVIDO:
     *   O Scanner lanca InputMismatchException quando o utilizador digita texto
     *   onde se espera um numero. Esta solucao usa hasNextInt() para validar
     *   ANTES de consumir, e nextLine() para limpar o buffer do teclado em
     *   caso de entrada invalida, evitando loops infinitos.
     *
     * @param mensagem Mensagem de prompt a apresentar ao utilizador.
     * @param min      Valor minimo aceite (inclusive).
     * @param max      Valor maximo aceite (inclusive).
     * @return Inteiro valido dentro do intervalo [min, max].
     */
    private int lerInteiroSeguro(String mensagem, int min, int max) {
        int valor = -1;
        boolean entradaValida = false;

        do {
            System.out.print(mensagem);

            if (scanner.hasNextInt()) {
                // Entrada e um inteiro — ler e validar intervalo
                valor = scanner.nextInt();
                scanner.nextLine(); // CRITICO: limpa o '\n' residual do buffer

                if (valor >= min && valor <= max) {
                    entradaValida = true;
                } else {
                    System.out.println("  [ERRO] Valor fora do intervalo permitido ["
                            + min + ", " + (max == Integer.MAX_VALUE ? "maximo" : max) + "]. Tente novamente.");
                }
            } else {
                // Entrada nao e um inteiro — limpar buffer e pedir novamente
                String entradaInvalida = scanner.nextLine();
                System.out.println("  [ERRO] Entrada invalida (\"" + entradaInvalida
                        + "\"). Digite apenas numeros inteiros.");
            }

        } while (!entradaValida);

        return valor;
    }
}
