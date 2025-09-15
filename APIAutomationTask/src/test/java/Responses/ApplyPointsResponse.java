package Responses;

public class ApplyPointsResponse {
    private String orderId;
    private int pointsApplied;
    private double discountAmount;
    private int remainingBalance;
    private double updatedOrderTotal;
    private String currency;
    private String region;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPointsApplied() {
        return pointsApplied;
    }

    public void setPointsApplied(int pointsApplied) {
        this.pointsApplied = pointsApplied;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(int remainingBalance) {
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
