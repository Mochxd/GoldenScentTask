package testCases.GoldenScentApiTests;

import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Test execution class for batch processing
 * Demonstrates programmatic test execution and report generation
 */
public class TestExecution {
    
    @Test(description = "Run all tests and open Allure report")
    public void runTestsAndOpenReport() {
        try {
            System.out.println("========================================");
            System.out.println("    Running Golden Scent API Tests");
            System.out.println("========================================");
            
            // Get the project root directory
            String projectRoot = System.getProperty("user.dir");
            String batFile = projectRoot + File.separator + "runTestsAndReport.bat";
            
            // Check if batch file exists
            File file = new File(batFile);
            if (!file.exists()) {
                System.err.println("ERROR: Batch file not found: " + batFile);
                return;
            }
            
            System.out.println("Executing: " + batFile);
            System.out.println();
            
            // Execute the batch file
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile);
            processBuilder.directory(new File(projectRoot));
            
            Process process = processBuilder.start();
            
            // Read output in real-time
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // Wait for process to complete
            int exitCode = process.waitFor();
            
            System.out.println();
            System.out.println("========================================");
            System.out.println("    Test Execution Complete");
            System.out.println("    Exit Code: " + exitCode);
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("Error executing batch file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test(description = "Open Allure report only")
    public void openAllureReport() {
        try {
            System.out.println("Opening Allure report...");
            
            String projectRoot = System.getProperty("user.dir");
            String batFile = projectRoot + File.separator + "reportGeneration.bat";
            
            File file = new File(batFile);
            if (!file.exists()) {
                System.err.println("ERROR: Batch file not found: " + batFile);
                return;
            }
            
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile);
            processBuilder.directory(new File(projectRoot));
            
            Process process = processBuilder.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
        } catch (Exception e) {
            System.err.println("Error opening Allure report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
