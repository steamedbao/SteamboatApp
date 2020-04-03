package com.SE.steamedboat.Entity;

import java.util.ArrayList;

public class Transaction {
    private String transactionCurrency;
    private float transactionAmount;
    private int payer;
    private float homeWorth;
    private float exchangeRate;

    public Transaction(float transactionAmount, int payer, float exchangeRate) {
        this.transactionAmount = transactionAmount;
        this.payer = payer;
        this.exchangeRate = exchangeRate;
    }
    public void findHomeWorth(){}
    public float sumAmountPaid(){
        return transactionAmount;
    }
    public ArrayList<Float> sumAmountIncurred(){
        ArrayList<Float> amountIncurred = new ArrayList<Float>();
        return amountIncurred;
    }
    public String getTransactionCurrency() {
        return transactionCurrency;
    }
    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }
    public float getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public int getPayer() {
        return payer;
    }
    public void setPayer(int payer) {
        this.payer = payer;
    }
    public float getHomeWorth() {
        return homeWorth;
    }
    public void setHomeWorth(float homeWorth) {
        this.homeWorth = homeWorth;
    }
    public float getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
