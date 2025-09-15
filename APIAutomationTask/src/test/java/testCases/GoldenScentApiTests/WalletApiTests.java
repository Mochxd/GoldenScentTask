package testCases.GoldenScentApiTests;

import Base.BaseTest;
import Responses.UseWalletResponse;
import Responses.WalletTransactionsResponse;
import Responses.RefundTriggerResponse;
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

@Feature("Golden Scent Wallet API")
public class WalletApiTests extends BaseTest {

    CommonMethods commonMethods = new CommonMethods();
    UseWalletResponse useWalletResponse = new UseWalletResponse();
    WalletTransactionsResponse walletTransactionsResponse = new WalletTransactionsResponse();
    RefundTriggerResponse refundTriggerResponse = new RefundTriggerResponse();
    String testDataPath = "src/main/resources/testData/";
    String useWalletRequestBody = "UseWalletRequest.json";
    String refundTriggerRequestBody = "RefundTriggerRequest.json";
    String createOrderRequestBody = "CreateOrderRequest.json";

    @Test(priority = 1, description = "Get wallet balance - Valid user")
    @Story("User should be able to retrieve their wallet balance")
    @Description("Test GET /wallet/balance endpoint with valid user authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void getWalletBalance_ValidUser() throws IOException {
        // Send Get Wallet Balance Request
        Response response = commonMethods.getWalletBalance();

        // Verify using APIVerification class
        ApiVerification.verifyWalletResponse(response);
        ApiVerification.verifyResponseTime(response, 2000);
    }

    @Test(priority = 2, description = "Get wallet balance - Invalid token")
    @Story("User should not be able to access wallet balance with invalid token")
    @Description("Test GET /wallet/balance endpoint with invalid authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void getWalletBalance_InvalidToken() throws IOException {
        // Send request with invalid token
        Response response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer invalid_token")
                .header("User-ID", "user_12345")
                .log().all()
                .when()
                .get(JavaUtilities.getWalletBalanceEndPoint());

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 401);
    }

    @Test(priority = 3, description = "Use wallet balance during checkout - Valid amount")
    @Story("User should be able to use wallet balance during checkout")
    @Description("Test POST /checkout/use-wallet endpoint with valid wallet amount")
    @Severity(SeverityLevel.CRITICAL)
    public void useWalletBalance_ValidAmount() throws IOException {
        // First create an order
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Use wallet
        String useWalletJsonPath = testDataPath + useWalletRequestBody;
        String useWalletJsonBody = JavaUtilities.jsonReader(useWalletJsonPath);
        JsonPath fileJsonPath = new JsonPath(useWalletJsonBody);

        Response response = commonMethods.useWallet(useWalletJsonBody);
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        // Set Response Data
        useWalletResponse.setOrderId(jsonPath.getString("data.orderId"));
        useWalletResponse.setWalletAmountUsed(jsonPath.getDouble("data.walletAmountUsed"));
        useWalletResponse.setRemainingBalance(jsonPath.getDouble("data.remainingBalance"));
        useWalletResponse.setUpdatedOrderTotal(jsonPath.getDouble("data.updatedOrderTotal"));
        useWalletResponse.setCurrency(jsonPath.getString("data.currency"));
        useWalletResponse.setTransactionId(jsonPath.getString("data.transactionId"));
        useWalletResponse.setPaymentType(jsonPath.getString("data.paymentType"));

        // Verify using APIVerification class
        ApiVerification.verifySuccessResponse(response);
        ApiVerification.verifyFieldPresence(response, "data.orderId");
        ApiVerification.verifyFieldPresence(response, "data.walletAmountUsed");
        ApiVerification.verifyFieldPresence(response, "data.transactionId");
        ApiVerification.verifyFieldValue(response, "data.orderId", fileJsonPath.getString("orderId"));
        ApiVerification.verifyResponseTime(response, 3000);
    }

    @Test(priority = 4, description = "Use wallet balance during checkout - Insufficient balance")
    @Story("User should not be able to use more wallet balance than available")
    @Description("Test POST /checkout/use-wallet endpoint with insufficient wallet balance")
    @Severity(SeverityLevel.NORMAL)
    public void useWalletBalance_InsufficientBalance() throws IOException {
        // Create order first
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Use excessive wallet amount
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "walletAmount": 1000.00,
                    "orderTotal": 299.00,
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.useWallet(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }

    @Test(priority = 5, description = "Use wallet balance during checkout - Below minimum threshold")
    @Story("User should not be able to use wallet below minimum threshold")
    @Description("Test POST /checkout/use-wallet endpoint with amount below minimum threshold")
    @Severity(SeverityLevel.NORMAL)
    public void useWalletBalance_BelowMinimumThreshold() throws IOException {
        // Create order first
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Use wallet amount below minimum threshold
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "walletAmount": 5.00,
                    "orderTotal": 299.00,
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.useWallet(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }

    @Test(priority = 6, description = "Use wallet balance during checkout - Exceed order total")
    @Story("User should not be able to use wallet amount exceeding order total")
    @Description("Test POST /checkout/use-wallet endpoint with wallet amount exceeding order total")
    @Severity(SeverityLevel.NORMAL)
    public void useWalletBalance_ExceedOrderTotal() throws IOException {
        // Create order first
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Use wallet amount exceeding order total
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "walletAmount": 500.00,
                    "orderTotal": 299.00,
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.useWallet(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }

    @Test(priority = 7, description = "Get wallet transactions - Valid user")
    @Story("User should be able to retrieve their wallet transaction history")
    @Description("Test GET /wallet/transactions endpoint with valid user authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void getWalletTransactions_ValidUser() throws IOException {
        // Send Get Wallet Transactions Request
        Response response = commonMethods.getWalletTransactions();

        // Verify using APIVerification class
        ApiVerification.verifySuccessResponse(response);
        ApiVerification.verifyFieldPresence(response, "data.userId");
        ApiVerification.verifyFieldPresence(response, "data.transactions");
        ApiVerification.verifyFieldPresence(response, "data.pagination");
        ApiVerification.verifyFieldPresence(response, "data.currency");
        ApiVerification.verifyResponseTime(response, 2000);
    }

    @Test(priority = 8, description = "Get wallet transactions - With filters")
    @Story("User should be able to filter wallet transactions")
    @Description("Test GET /wallet/transactions endpoint with date and type filters")
    @Severity(SeverityLevel.NORMAL)
    public void getWalletTransactions_WithFilters() throws IOException {
        // Send Get Wallet Transactions Request with filters
        Response response = commonMethods.getWalletTransactionsWithFilters("refund", 5, 0);

        // Verify using APIVerification class
        ApiVerification.verifySuccessResponse(response);
        ApiVerification.verifyFieldPresence(response, "data.transactions");
        ApiVerification.verifyFieldPresence(response, "data.pagination");
    }

    @Test(priority = 9, description = "Trigger refund to wallet - Valid refund")
    @Story("System should be able to process refunds to wallet")
    @Description("Test POST /refund/trigger endpoint for valid refund to wallet")
    @Severity(SeverityLevel.CRITICAL)
    public void triggerRefundToWallet_ValidRefund() throws IOException {
        // First create an order
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Trigger refund
        String refundTriggerJsonPath = testDataPath + refundTriggerRequestBody;
        String refundTriggerJsonBody = JavaUtilities.jsonReader(refundTriggerJsonPath);
        JsonPath fileJsonPath = new JsonPath(refundTriggerJsonBody);

        Response response = commonMethods.triggerRefund(refundTriggerJsonBody);
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        // Set Response Data
        refundTriggerResponse.setRefundId(jsonPath.getString("data.refundId"));
        refundTriggerResponse.setOrderId(jsonPath.getString("data.orderId"));
        refundTriggerResponse.setRefundAmount(jsonPath.getDouble("data.refundAmount"));
        refundTriggerResponse.setRefundType(jsonPath.getString("data.refundType"));
        refundTriggerResponse.setStatus(jsonPath.getString("data.status"));
        refundTriggerResponse.setCurrency(jsonPath.getString("data.currency"));
        refundTriggerResponse.setProcessedAt(jsonPath.getString("data.processedAt"));

        // Verify using APIVerification class
        ApiVerification.verifySuccessResponse(response);
        ApiVerification.verifyFieldPresence(response, "data.refundId");
        ApiVerification.verifyFieldPresence(response, "data.status");
        ApiVerification.verifyFieldValue(response, "data.orderId", fileJsonPath.getString("orderId"));
        ApiVerification.verifyResponseTime(response, 5000);
    }

    @Test(priority = 10, description = "Trigger refund to wallet - Invalid order")
    @Story("System should not process refunds for invalid orders")
    @Description("Test POST /refund/trigger endpoint with invalid order ID")
    @Severity(SeverityLevel.NORMAL)
    public void triggerRefundToWallet_InvalidOrder() throws IOException {
        // Trigger refund for non-existent order
        String requestBody = """
                {
                    "orderId": "invalid_order_123",
                    "refundAmount": 75.50,
                    "refundReason": "Customer requested return",
                    "refundType": "wallet",
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.triggerRefund(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 404);
    }

    @Test(priority = 11, description = "Trigger refund to wallet - Exceed order amount")
    @Story("System should not process refunds exceeding order amount")
    @Description("Test POST /refund/trigger endpoint with refund amount exceeding order total")
    @Severity(SeverityLevel.NORMAL)
    public void triggerRefundToWallet_ExceedOrderAmount() throws IOException {
        // Create order first
        String createOrderJsonPath = testDataPath + createOrderRequestBody;
        String createOrderJsonBody = JavaUtilities.jsonReader(createOrderJsonPath);
        commonMethods.createOrder(createOrderJsonBody);

        // Trigger refund exceeding order amount
        String requestBody = """
                {
                    "orderId": "order_12345",
                    "refundAmount": 1000.00,
                    "refundReason": "Customer requested return",
                    "refundType": "wallet",
                    "currency": "SAR",
                    "region": "KSA"
                }
                """;

        Response response = commonMethods.triggerRefund(requestBody);

        // Verify error response
        ApiVerification.verifyErrorResponse(response, 400);
    }

    @Test(priority = 12, description = "Health check - Server status")
    @Story("System should provide health check endpoint")
    @Description("Test GET /health endpoint for server status")
    @Severity(SeverityLevel.NORMAL)
    public void healthCheck_ServerStatus() throws IOException {
        // Send Health Check Request
        Response response = commonMethods.healthCheck();

        // Verify using APIVerification class
        ApiVerification.verifySuccessResponse(response);
        ApiVerification.verifyFieldPresence(response, "data.status");
        ApiVerification.verifyResponseTime(response, 1000);
    }
}
