package com.textifly.quickmudra.UI.ActivityPage.Model;

public class LoanStatusModel {
    String id,amount,paid_status,disbursed_date,payment_date;

    public LoanStatusModel(String id, String amount, String paid_status, String disbursed_date, String payment_date) {
        this.id = id;
        this.amount = amount;
        this.paid_status = paid_status;
        this.disbursed_date = disbursed_date;
        this.payment_date = payment_date;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public String getDisbursed_date() {
        return disbursed_date;
    }

    public String getPayment_date() {
        return payment_date;
    }
}
