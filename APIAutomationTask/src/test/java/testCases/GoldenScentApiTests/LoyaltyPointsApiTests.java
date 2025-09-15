package testCases.GoldenScentApiTests;

import Base.BaseTest;
import Responses.LoyaltyBalanceResponse;
import Responses.ApplyPointsResponse;
import apiVerification.ApiVerification;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import testCases.CommonMethods;
import utils.JavaUtilities;

import java.io.IOException;

@Feature("Golden Scent Loyalty Points API")
public class LoyaltyPointsApiTests extends BaseTest {

    CommonMethods commonMethods = new CommonMethods();
    LoyaltyBalanceResponse loyaltyBalanceResponse = new LoyaltyBalanceResponse();
    ApplyPointsResponse applyPointsResponse = new ApplyPointsResponse();
    String testDataPath = "src/main/resources/testData/";
    String applyPointsRequestBody = "ApplyPointsRequest.json";
    String createOrderRequestBody = "CreateOrderRequest.json";

    @Test(priority = 1, description = "Get user loyalty balance - Valid user")
    @Story("User should be able to retrieve their loyalty points balance")
    @Description("Test GET /user/loyalty-balance endpoint with valid user authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserLoyaltyBalance_ValidUser() throws IOException {
        // Send Get Loyalty Balance Request
        Response response = commonMethods.getLoyaltyBalance();
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        // Parse response data with null safety
        if (jsonPath.get("data") != null) {
            loyaltyBalanceResponse.setUserId(jsonPath.getString("data.userId"));
            loyaltyBalanceResponse.setAvailablePoints(jsonPath.getInt("data.availablePoints"));
            loyaltyBalanceResponse.setTotalEarned(jsonPath.getInt("data.totalEarned"));
            loyaltyBalanceResponse.setTotalRedeemed(jsonPath.getInt("data.totalRedeemed"));
            loyaltyBalanceResponse.setExpiredPoints(jsonPath.getInt("data.expiredPoints"));
            loyaltyBalanceResponse.setLastUpdated(jsonPath.getString("data.lastUpdated"));
            loyaltyBalanceResponse.setPointsExpiringSoon(jsonPath.getInt("data.pointsExpiringSoon"));
            loyaltyBalanceResponse.setCurrency(jsonPath.getString("data.currency"));
            loyaltyBalanceResponse.setRegion(jsonPath.getString("data.region"));
        }

        // Verify using APIVerification class
        ApiVerification.verifyLoyaltyBalanceResponse(response);
        ApiVerification.verifyResponseTime(response, 2000);
    }

    @Test(priority = 2, description = "Get user loyalty balance - Invalid token")
    @Story("User should not be able to access loyalty balance with invalid token")
    @Description("Test GET /user/loyalty-balance endpoint with invalid authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserLoyaltyBalance_InvalidToken() throws IOException {
        // Send request with invalid token
        Response response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer invalid_token")
                .header("User-ID", "user_12345")
                .log().all()
                .when()
                .get(JavaUtilities.getLoyaltyBalanceEndPoint());

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 401);
    }

    @Test(priority = 3, description = "Get user loyalty balance - Guest user")
    @Story("Guest users should not have access to loyalty points")
    @Description("Test GET /user/loyalty-balance endpoint for guest user")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserLoyaltyBalance_GuestUser() throws IOException {
        // Send request as guest user (with valid Authorization header to pass initial validation)
        Response response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer valid_auth_token_12345")
                .header("User-ID", "guest_67890")
                .log().all()
                .when()
                .get(JavaUtilities.getLoyaltyBalanceEndPoint());

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 403);
    }

    @Test(priority = 4, description = "Apply loyalty points during checkout - Valid points")
    @Story("User should be able to apply loyalty points during checkout")
    @Description("Test POST /checkout/apply-points endpoint with valid points amount")
    @Severity(SeverityLevel.CRITICAL)
    public void applyLoyaltyPoints_ValidPoints() throws IOException {
        // First create an order
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Apply points
        String applyPointsJsonPath = testDataPath + applyPointsRequestBody;
        String applyPointsJsonBody = JavaUtilities.jsonReader(applyPointsJsonPath);
        JsonPath fileJsonPath = new JsonPath(applyPointsJsonBody);

        Response response = commonMethods.applyPoints(applyPointsJsonBody);
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        // Parse response data with null safety
        if (jsonPath.get("data") != null) {
            applyPointsResponse.setOrderId(jsonPath.getString("data.orderId"));
            applyPointsResponse.setPointsApplied(jsonPath.getInt("data.pointsApplied"));
            applyPointsResponse.setDiscountAmount(jsonPath.getDouble("data.discountAmount"));
            applyPointsResponse.setRemainingBalance(jsonPath.getInt("data.remainingBalance"));
            applyPointsResponse.setUpdatedOrderTotal(jsonPath.getDouble("data.updatedOrderTotal"));
            applyPointsResponse.setCurrency(jsonPath.getString("data.currency"));
            applyPointsResponse.setRegion(jsonPath.getString("data.region"));
        }

        // Verify using APIVerification class
        ApiVerification.verifySuccessResponse(response);
        ApiVerification.verifyFieldPresence(response, "data.orderId");
        ApiVerification.verifyFieldPresence(response, "data.pointsApplied");
        ApiVerification.verifyFieldPresence(response, "data.discountAmount");
        ApiVerification.verifyFieldValue(response, "data.orderId", fileJsonPath.getString("orderId"));
        ApiVerification.verifyResponseTime(response, 3000);
    }

    @Test(priority = 5, description = "Apply loyalty points during checkout - Insufficient points")
    @Story("User should not be able to apply more points than available")
    @Description("Test POST /checkout/apply-points endpoint with insufficient points")
    @Severity(SeverityLevel.CRITICAL)
    public void applyLoyaltyPoints_InsufficientPoints() throws IOException {
        // Create order first
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Apply excessive points
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "pointsToUse": 10000,
                    "orderTotal": 299.00,
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.applyPoints(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }

    @Test(priority = 6, description = "Apply loyalty points during checkout - Exceed maximum percentage")
    @Story("User should not be able to apply points exceeding 100% of order total")
    @Description("Test POST /checkout/apply-points endpoint exceeding 100% of order total")
    @Severity(SeverityLevel.CRITICAL)
    public void applyLoyaltyPoints_ExceedMaxPercentage() throws IOException {
        // Create order first
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Apply points exceeding maximum percentage
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "pointsToUse": 5000,
                    "orderTotal": 100.00,
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.applyPoints(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }

    @Test(priority = 7, description = "Apply loyalty points during checkout - Invalid order")
    @Story("User should not be able to apply points to invalid order")
    @Description("Test POST /checkout/apply-points endpoint with invalid order ID")
    @Severity(SeverityLevel.NORMAL)
    public void applyLoyaltyPoints_InvalidOrder() throws IOException {
        // Apply points to non-existent order
        String requestBody = """
                {
                    "orderId": "invalid_order_123",
                    "pointsToUse": 100,
                    "orderTotal": 299.00,
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.applyPoints(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 404);
    }

    @Test(priority = 8, description = "Apply loyalty points during checkout - Missing required fields")
    @Story("API should validate required fields in request")
    @Description("Test POST /checkout/apply-points endpoint with missing required fields")
    @Severity(SeverityLevel.NORMAL)
    public void applyLoyaltyPoints_MissingRequiredFields() throws IOException {
        // Apply points with missing required fields
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "pointsToUse": 100
                }
                """;

        Response response = commonMethods.applyPoints(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }
}
