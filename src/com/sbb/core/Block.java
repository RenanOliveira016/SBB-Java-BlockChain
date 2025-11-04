package com.sbb.core;

import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A classe Block é o coração da Blockchain, representando uma unidade de registro
 * imutável. Ela armazena os dados transacionais e as informações criptográficas
 * que garantem a integridade da cadeia.
 * * Este bloco é projetado para aceitar uma Transação estruturada,
 * em preparação para futura integração com análise de Machine Learning.
 * * @author Renan
 * @version 1.0
 */
public class Block {

    /** Os dados estruturados da transação contidos neste bloco. */
    private Transaction transaction;
    
    /** O exato momento em que o bloco foi criado/minerado (carimbo oficial do bloco). */
    private long timeStamp;
    
    /** O hash do bloco anterior na cadeia (link criptográfico crucial para imutabilidade). */
    private String previousHash;
    
    /** O hash calculado do bloco atual (Prova de Integridade). */
    private String hash;
    
    /** Contador que é iterado durante a Prova de Trabalho (PoW) para encontrar o hash válido. */
    private int nonce;

    /**
     * Construtor do Bloco. Inicializa os dados e define o previousHash.
     * O hash inicial é definido como "0" (ou "1") para garantir que o método
     * mineBlock() inicie a Prova de Trabalho.
     * * @param previousHash O hash do bloco anterior (ex: "0" para o Bloco Gênese).
     * @param transaction O objeto Transação imutável contendo os detalhes da transferência.
     */
    public Block(String previousHash, Transaction transaction) {
        this.transaction = transaction;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime(); // Captura o TimeStamp do Bloco

        // Valor inicial para forçar o loop de mineração a começar.
        this.hash = "1";
    }

    /**
     * Calcula o Hash SHA-256 do bloco.
     * O Hash é gerado a partir da concatenação de todos os atributos chave.
     * A ordem da concatenação é estritamente definida para garantir a determinismo.
     * * Ordem de Input (Concatenação Criptográfica):
     * nonce + previousHash + Block TimeStamp + Transaction TimeStamp + Amount + Sender + Recipient
     * * @return O hash SHA-256 (64 caracteres hexadecimais) ou null em caso de erro.
     */
    public String calculateHash() {

        String input = Integer.toString(nonce) + previousHash + Long.toString(timeStamp) +
                Long.toString(transaction.getTimeStamp()) + Double.toString(transaction.getAmount()) +
                transaction.getSender() + transaction.getRecipient();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            
            // Converte bytes para String Hexadecimal de 64 caracteres
            for (int i = 0; i < byteHash.length; i++) {
                final String hex = Integer.toHexString(0xff & byteHash[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Este bloco é vital para o Java lidar com a exceção verificada
            return null;
        }
    }

    /**
     * Implementa a Prova de Trabalho (PoW) para minerar o bloco.
     * O método itera o nonce e recalcula o hash até que a dificuldade seja atingida.
     * * @param difficulty O número de zeros exigidos no início do hash (ex: 4 para "0000").
     */
    public void mineBlock(int difficulty) {

        // Cria a string alvo (target) de zeros
        String target = new String(new char[difficulty]).replace('\0', '0');

        // Loop principal da Prova de Trabalho (PoW)
        while (!this.hash.startsWith(target)) {

            this.nonce++; // Altera o input
            this.hash = calculateHash(); // Recalcula o hash e o atribui (atualiza a condição do while)
        }
    }

    /**
     * @return O hash final do bloco.
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return O hash do bloco anterior.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * @return O objeto Transação contido neste bloco.
     */
    public Transaction getTransaction() {
        return transaction;
    }
    
    /**
     * Seta o hash do bloco anterior. Usado pela classe Blockchain para ligar a cadeia.
     * @param previousHash O hash do bloco anterior válido.
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * *** MÉTODO APENAS PARA TESTES DE ADULTERAÇÃO (RF07) ***
     * Força a alteração do hash do bloco para simular uma fraude.
     * Em produção, este método seria estritamente proibido.
     * @param hash O valor do hash falso a ser injetado.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
}