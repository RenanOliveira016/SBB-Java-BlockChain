package com.sbb.app;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.sbb.core.Block;
import com.sbb.core.Blockchain;
import com.sbb.core.Transaction; 


/**
 * Classe principal para inicializar, testar e demonstrar o Simulador Básico de Blockchain (SBB).
 * Esta classe é o ponto de entrada da aplicação e contém os cenários de teste de Sucesso e Falha (RF07).
 * @author Renan
 * @version 1.0
 */
public class Main {

    /**
     * O método principal que inicia a simulação da Blockchain.
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        
        // 1. Configuração Inicial
        int dificuldade_simulacao = 4;
        System.out.println("==================================================");
        System.out.println("INICIANDO SIMULADOR BÁSICO DE BLOCKCHAIN (SBB)");
        System.out.println("Dificuldade de Mineração: " + dificuldade_simulacao + " zeros");
        System.out.println("==================================================");

        // Inicializa a Blockchain (cria o Bloco Gênese no construtor)
        Blockchain blockchain = new Blockchain(dificuldade_simulacao);
        
        // Criação das Transações Imutáveis
        Transaction t1 = new Transaction(200, "Renan", "Kaue");
        Transaction t2 = new Transaction(1, "Kaue", "Renan");
        Transaction t3 = new Transaction(9700, "Matheus", "Veiga");
        
        try {
            // 2. ADIÇÃO DE BLOCOS VÁLIDOS

            // Bloco 1
            Block bloco1 = new Block("", t1); 
            blockchain.addBlock(bloco1);

            // Bloco 2
            Block bloco2 = new Block("", t2);
            blockchain.addBlock(bloco2);
            
            // Bloco 3
            Block bloco3 = new Block("", t3);
            blockchain.addBlock(bloco3);

            // 3. CENÁRIO DE SUCESSO: VALIDAÇÃO INICIAL
            System.out.println("\n--- 1. TESTE DE VALIDAÇÃO DE SUCESSO ---");
            System.out.println("A Cadeia é Válida? " + blockchain.isChainValid());
            System.out.println("------------------------------------------");

            
            // 4. CENÁRIO DE FALHA: SIMULAÇÃO DE ADULTERAÇÃO (RF07)
            
            System.out.println("\n--- 2. TESTE DE IMUTABILIDADE (ADULTERAÇÃO) ---");

            // Obtém a lista de blocos
            List<Block> chain = blockchain.getChain();

            // Descomente e complete esta seção para rodar o Teste de Fraude (RF07)
            /*
            Block blocoAdulterado = chain.get(2);
            System.out.println("Adulterando Bloco #" + 2 + "...");
            
            // Necessário adicionar o setHash para simular a fraude sem quebrar a imutabilidade da Transaction
            blocoAdulterado.setHash("HASH-FALSO-INJETADO"); 
            */

            // Tenta validar a cadeia após a adulteração
            System.out.println("\nVerificando a Cadeia após Adulteração de Dados...");
            System.out.println("A Cadeia é Válida? " + blockchain.isChainValid());
            
            System.out.println("------------------------------------------");


            // 5. IMPRESSÃO DOS BLOCOS (Para visualização)
            System.out.println("\n============ ESTRUTURA FINAL DA CADEIA ============");
            int index = 0;
            for (Block block : chain) {
                System.out.println("\n--- Bloco #" + index++ + " ---");
                // Exibe os dados da Transação de forma legível
                System.out.println("ID Transação: " + block.getTransaction().getTransactionId());
                System.out.println("Valor: " + block.getTransaction().getAmount());
                System.out.println("Hash: " + block.getHash());
                System.out.println("PreviousHash: " + block.getPreviousHash());
            }
            System.out.println("===================================================");
            

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro Crítico de Segurança: SHA-256 não encontrado.");
            e.printStackTrace();
        }
    }
}