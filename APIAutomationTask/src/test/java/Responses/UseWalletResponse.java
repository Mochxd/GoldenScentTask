package Responses;

public class UseWalletResponse {
    private String orderId;
    private double walletAmountUsed;
    private double remainingBalance;
    private double updatedOrderTotal;
    private String currency;
    private String transactionId;
    private String paymentType;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getWalletAmountUsed() {
        return walletAmountUsed;
    }

    public void setWalletAmountUsed(double walletAmountUsed) {
        this.walletAmountUsed = walletAmountUsed;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public double getUpdatedOrderTotal() {
        return updatedOrderTotal;
    }

    public void setUpdatedOrderTotal(double updatedOrderTotal) {
        this.updatedOrderTotal = updatedOrderTotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
