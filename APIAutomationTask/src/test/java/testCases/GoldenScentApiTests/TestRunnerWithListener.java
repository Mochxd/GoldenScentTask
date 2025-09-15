package testCases.GoldenScentApiTests;

import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import listeners.TestNGListener;

@Listeners(TestNGListener.class)
@Feature("Test Runner with Listener")
public class TestRunnerWithListener {
    
    @Test(description = "Run all Golden Scent API tests with listener")
    @Story("Execute complete test suite with enhanced logging")
    @Description("This test demonstrates the TestNG listener functionality")
    @Severity(SeverityLevel.CRITICAL)
    public void runAllTestsWithListener() {
        System.out.println("This test will trigger the TestNG listener");
        System.out.println("The listener will automatically open Allure report after execution");
        
        // Demonstration test for listener functionality
        // Actual API tests are executed via TestNG XML configuration
        System.out.println("Test completed - listener will handle report generation");
    }
}
