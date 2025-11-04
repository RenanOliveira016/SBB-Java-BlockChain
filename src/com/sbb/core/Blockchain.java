package com.sbb.core;

import java.util.ArrayList;
import java.util.List;
import java.security.NoSuchAlgorithmException;

/**
 * A classe Blockchain atua como o gerente da cadeia, sendo responsável por
 * manter a ordem sequencial dos blocos e garantir que a cadeia inteira seja
 * íntegra.
 * Implementa as operações de Create (adição de bloco) e Read (validação da cadeia).
 * * @author Renan
 * @version 1.0
 */
public class Blockchain {

    /** A lista sequencial que armazena todos os objetos Block na ordem cronológica. */
    private List<Block> chain;

    /** O valor inteiro que define a exigência da Prova de Trabalho (ex: 4 zeros). */
    private int difficulty;

    /**
     * Construtor da Blockchain.
     * Inicializa a lista e define a dificuldade.
     * Cria e minera o Bloco Gênese (RF01) para iniciar a cadeia.
     * * @param difficulty O nível de dificuldade para a mineração (número de zeros iniciais exigidos).
     */
    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        this.chain = new ArrayList<>();

        // Inicializa a cadeia adicionando o Bloco Gênese
        try {
            addGenesisBlock();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro Crítico: Algoritmo de Hashing não encontrado.");
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para criar e adicionar o Bloco Gênese (o primeiro bloco da cadeia).
     * @throws NoSuchAlgorithmException Se o algoritmo SHA-256 não estiver disponível.
     */
    private void addGenesisBlock() throws NoSuchAlgorithmException {
        // O Bloco Gênese tem previousHash fixo ("0") e dados simbólicos.
        Transaction transaction = new Transaction(0, "System", "Genesis");
        Block genesis = new Block("0", transaction);
        genesis.mineBlock(difficulty);

        // Adiciona diretamente à cadeia (somente o Bloco Gênese)
        chain.add(genesis);
        System.out.println("Bloco Gênese Minerado e Adicionado!");
    }

    /**
     * Adiciona um novo bloco à cadeia (RF05).
     * O bloco anterior é buscado, o novo bloco é ligado criptograficamente e minerado.
     * * @param newBlock O novo Block a ser adicionado.
     * @throws NoSuchAlgorithmException Se o algoritmo SHA-256 falhar durante a mineração.
     */
    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {

        // --- PROCESSO PADRÃO DA BLOCKCHAIN ---
        Block lastBlock = chain.get(chain.size() - 1);

        // 2. Define o link criptográfico
        newBlock.setPreviousHash(lastBlock.getHash());

        // 3. Minera o novo bloco (PoW)
        newBlock.mineBlock(difficulty);

        // 4. Adiciona o bloco validado à lista
        chain.add(newBlock);
        System.out.println("Novo Bloco Minerado e Adicionado");
    }

    /**
     * Verifica a integridade da cadeia de blocos (RF06).
     * Executa a auditoria de cada bloco a partir do índice 1 (pós-Gênese).
     * * @return {@code true} se a cadeia for válida e íntegra; {@code false} se encontrar qualquer adulteração.
     * @throws NoSuchAlgorithmException Se o algoritmo SHA-256 não estiver disponível.
     */
    public boolean isChainValid() throws NoSuchAlgorithmException {
        Block currentBlock;
        Block previousBlock;

        // Define o target (a string de zeros) para checagem da PoW
        String target = new String(new char[difficulty]).replace('\0', '0');

        // Loop começa do índice 1 (o primeiro bloco após o Gênese)
        for (int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i - 1);

            // 1. Verifica a Prova de Integridade do Bloco Atual (O hash armazenado é o hash recalculado?)
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Cadeia Inválida: O Hash do Bloco #" + i + " foi alterado.");
                return false;
            }

            // 2. Verifica a Ligação Criptográfica (O previousHash aponta para o bloco anterior?)
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Cadeia Inválida: O link de previousHash no Bloco #" + i + " está quebrado.");
                return false;
            }

            // 3. Verifica a Prova de Trabalho (O hash atende ao critério de dificuldade?)
            if (!currentBlock.getHash().startsWith(target)) {
                System.out.println("Cadeia Inválida: O Bloco #" + i + " não foi minerado corretamente.");
                return false;
            }
        }

        return true;
    }

    /**
     * @return A lista de blocos que compõe a cadeia.
     */
    public List<Block> getChain() {
        return chain;
    }
}