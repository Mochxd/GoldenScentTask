package listeners;

import org.testng.*;
import io.restassured.response.Response;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * TestNG Listener for Golden Scent API Test Framework
 * Handles test execution events and automatic report generation
 */
public class TestNGListener implements ITestListener, IExecutionListener, IAlterSuiteListener {

    @Override
    public void onExecutionStart() {
        System.out.println("========================================");
        System.out.println("    Golden Scent API Test Framework");
        System.out.println("========================================");
        System.out.println("Starting test execution...");
        System.out.println();
    }

    @Override
    public void onExecutionFinish() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("    Test Execution Complete");
        System.out.println("========================================");
        System.out.println("Generating Allure Report...");
        
        try {
            System.out.println("Opening Allure Report...");
            Runtime.getRuntime().exec("reportGeneration.bat");
            System.out.println("Allure report opened successfully!");
        } catch (IOException e) {
            System.err.println("Unable to open Allure Report: " + e.getMessage());
            System.err.println("Please run 'reportGeneration.bat' manually to view the report.");
        }
        
        System.out.println("========================================");
        System.out.println("    End of Execution");
        System.out.println("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println();
        System.out.println(" Starting Test: " + result.getName());
        System.out.println("   Class: " + result.getTestClass().getName());
        System.out.println("   Method: " + result.getMethod().getMethodName());
        System.out.println("   Description: " + result.getMethod().getDescription());
        System.out.println("----------------------------------------");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test PASSED: " + result.getName());
        System.out.println("   Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        System.out.println("----------------------------------------");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ Test FAILED: " + result.getName());
        System.out.println("   Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        
        // Log exception details for debugging
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            System.err.println("   Error: " + throwable.getMessage());
            System.err.println("   Exception: " + throwable.getClass().getSimpleName());
        }
        
        // Capture API response details for debugging
        try {
            Object currentClass = result.getInstance();
            Field[] fields = result.getTestClass().getRealClass().getDeclaredFields();
            
            for (Field field : fields) {
                if (field.getType() == Response.class) {
                    field.setAccessible(true);
                    Response response = (Response) field.get(currentClass);
                    if (response != null) {
                        System.err.println("   API Response Status: " + response.getStatusCode());
                        System.err.println("   API Response Body: " + response.getBody().asString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            System.err.println("   Unable to capture API response details");
        }
        
        System.out.println("----------------------------------------");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭️  Test SKIPPED: " + result.getName());
        System.out.println("----------------------------------------");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("⚠️  Test FAILED but within success percentage: " + result.getName());
        System.out.println("----------------------------------------");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println();
        System.out.println(" Starting Test Suite: " + context.getName());
        System.out.println("   Total Tests: " + context.getAllTestMethods().length);
        System.out.println("========================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println();
        System.out.println(" Test Suite Results: " + context.getName());
        System.out.println("   Passed: " + context.getPassedTests().size());
        System.out.println("   Failed: " + context.getFailedTests().size());
        System.out.println("   Skipped: " + context.getSkippedTests().size());
        System.out.println("   Total: " + context.getAllTestMethods().length);
        System.out.println("========================================");
    }
}
