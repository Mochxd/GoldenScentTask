package testCases;

import Base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.JavaUtilities;

import java.io.IOException;

/**
 * Common API methods for Golden Scent API testing
 * Contains reusable methods for making API calls with proper authentication
 */
public class CommonMethods extends BaseTest {
    
    // Loyalty Points API Methods
    public Response getLoyaltyBalance() throws IOException {
        System.out.println("GET Loyalty Balance");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .log().all()
                .when()
                .get(JavaUtilities.getLoyaltyBalanceEndPoint());
    }
    
    public Response applyPoints(String jsonBody) throws IOException {
        System.out.println("POST Apply Points");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .body(jsonBody)
                .log().all()
                .when()
                .post(JavaUtilities.getApplyPointsEndPoint());
    }
    
    // Wallet API Methods
    public Response getWalletBalance() throws IOException {
        System.out.println("GET Wallet Balance");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .log().all()
                .when()
                .get(JavaUtilities.getWalletBalanceEndPoint());
    }
    
    public Response useWallet(String jsonBody) throws IOException {
        System.out.println("POST Use Wallet");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .body(jsonBody)
                .log().all()
                .when()
                .post(JavaUtilities.getUseWalletEndPoint());
    }
    
    public Response getWalletTransactions() throws IOException {
        System.out.println("GET Wallet Transactions");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .log().all()
                .when()
                .get(JavaUtilities.getWalletTransactionsEndPoint());
    }
    
    public Response getWalletTransactionsWithFilters(String type, int limit, int offset) throws IOException {
        System.out.println("GET Wallet Transactions with Filters");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .queryParam("type", type)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .log().all()
                .when()
                .get(JavaUtilities.getWalletTransactionsEndPoint());
    }
    
    // Refund API Methods
    public Response triggerRefund(String jsonBody) throws IOException {
        System.out.println("POST Trigger Refund");
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "user_12345")
                .body(jsonBody)
                .log().all()
                .when()
                .post(JavaUtilities.getRefundTriggerEndPoint());
    }
    
    // Helper Methods
    public Response createOrder(String jsonBody) throws IOException {
        System.out.println("POST Create Order");
        return RestAssured.given()
                .spec(requestSpec)
                .body(jsonBody)
                .log().all()
                .when()
                .post(JavaUtilities.getOrdersEndPoint());
    }
    
    public Response healthCheck() throws IOException {
        System.out.println("GET Health Check");
        return RestAssured.given()
                .spec(requestSpec)
                .log().all()
                .when()
                .get(JavaUtilities.getHealthEndPoint());
    }
}

