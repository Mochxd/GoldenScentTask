package Responses;

public class LoyaltyBalanceResponse {
    private String userId;
    private int availablePoints;
    private int totalEarned;
    private int totalRedeemed;
    private int expiredPoints;
    private String lastUpdated;
    private int pointsExpiringSoon;
    private String currency;
    private String region;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public int getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(int totalEarned) {
        this.totalEarned = totalEarned;
    }

    public int getTotalRedeemed() {
        return totalRedeemed;
    }

    public void setTotalRedeemed(int totalRedeemed) {
        this.totalRedeemed = totalRedeemed;
    }

    public int getExpiredPoints() {
        return expiredPoints;
    }

    public void setExpiredPoints(int expiredPoints) {
        this.expiredPoints = expiredPoints;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getPointsExpiringSoon() {
        return pointsExpiringSoon;
    }

    public void setPointsExpiringSoon(int pointsExpiringSoon) {
        this.pointsExpiringSoon = pointsExpiringSoon;
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
