package com.sbb.core;

import java.util.UUID;
import java.util.Date;

/**
 * Representa uma transação individual que será empacotada em um Bloco.
 * Esta classe é imutável (não possui setters) para garantir a integridade dos dados, 
 * um princípio fundamental da Blockchain.
 * * @author Renan
 * @version 1.0
 */
public class Transaction {

    /** Identificador único da transação, gerado automaticamente usando UUID. */
    private String transactionId;
    
    /** O valor transferido na transação. */
    private double amount;
    
    /** O endereço ou identificador do remetente dos fundos. */
    private String sender;
    
    /** O endereço ou identificador do destinatário dos fundos. */
    private String recipient;
    
    /** O carimbo de tempo (timestamp) exato da criação desta transação (em milissegundos). */
    private long timeStamp;

    /**
     * @return O identificador único da transação (UUID).
     */
    public String getTransactionId(){
        return transactionId;
    }

    /**
     * @return O valor da transação.
     */
    public double getAmount(){
        return amount;
    }

    /**
     * @return O remetente da transação.
     */
    public String getSender(){
        return sender;
    }

    /**
     * @return O destinatário da transação.
     */
    public String getRecipient(){
        return recipient;
    }

    /**
     * @return O carimbo de tempo (timestamp) da criação da transação.
     */
    public long getTimeStamp(){
        return timeStamp;
    }

    /**
     * Construtor para criar uma transação imutável.
     * O timeStamp e o transactionId são gerados no momento da construção.
     * * @param amount O valor a ser transferido.
     * @param sender O remetente.
     * @param recipient O destinatário.
     */
    public Transaction (double amount, String sender, String recipient){
        this.amount = amount;
        this.recipient = recipient;
        this.sender = sender;
        this.timeStamp = new Date().getTime();
        this.transactionId = UUID.randomUUID().toString();
    }
}