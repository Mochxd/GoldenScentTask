package apiVerification;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class ApiVerification {

    public static void verifyStatusCode(Response response, int expectedCode) {
        System.out.println("Expected Status Code: "+ expectedCode);
        System.out.println("Actual Status Code: "+ response.getStatusCode());
        int actualCode = response.getStatusCode();
        Assert.assertEquals(actualCode, expectedCode, "Expected status code: " + expectedCode + " but found: " + actualCode);
    }

    public static void verifyContentType(Response response, String expectedType) {
        String actualType = response.getContentType();
        Assert.assertEquals(actualType, expectedType, "Expected content type: " + expectedType + " but found: " + actualType);
    }

    public static void verifyFieldPresence(Response response, String field) {
        boolean fieldPresent = response.jsonPath().get(field) != null;
        Assert.assertTrue(fieldPresent, "Field '" + field + "' is not present in the response.");
    }

    public static void verifyFieldValue(Response response, String field, String expected) {
        String actualValue = response.jsonPath().getString(field);
        Assert.assertEquals(actualValue, expected, "Expected value for field '" + field + "' is: " + expected + ", but found: " + actualValue);
    }

    public static void verifyResponseTime(Response response, long maxResponseTime) {
        long actualTime = response.getTime();
        Assert.assertTrue(actualTime <= maxResponseTime, "Response time is too high! Expected <= " + maxResponseTime + "ms but found: " + actualTime + "ms");
    }

    public static void verifySuccessResponse(Response response) {
        verifyStatusCode(response, 200);
        verifyFieldPresence(response, "success");
        verifyFieldValue(response, "success", "true");
    }
    
    public static void verifyErrorResponse(Response response, int expectedStatusCode) {
        verifyStatusCode(response, expectedStatusCode);
        verifyFieldPresence(response, "success");
        verifyFieldValue(response, "success", "false");
        verifyFieldPresence(response, "message");
    }
    
    public static void verifyLoyaltyBalanceResponse(Response response) {
        verifySuccessResponse(response);
        verifyFieldPresence(response, "data.userId");
        verifyFieldPresence(response, "data.availablePoints");
        verifyFieldPresence(response, "data.currency");
        verifyFieldPresence(response, "data.region");
    }
    
    public static void verifyWalletResponse(Response response) {
        verifySuccessResponse(response);
        verifyFieldPresence(response, "data.userId");
        verifyFieldPresence(response, "data.availableBalance");
        verifyFieldPresence(response, "data.currency");
        verifyFieldPresence(response, "data.region");
    }
}
