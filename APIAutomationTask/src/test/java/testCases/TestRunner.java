package testCases;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Programmatic test runner for Golden Scent API tests
 * Provides methods to execute tests and generate reports programmatically
 */
public class TestRunner {
    
    public static void runTestsAndOpenReport() {
        try {
            System.out.println("Starting test execution and report generation...");
            
            // Get the project root directory
            String projectRoot = System.getProperty("user.dir");
            String batFile = projectRoot + File.separator + "runTestsAndReport.bat";
            
            // Check if batch file exists
            File file = new File(batFile);
            if (!file.exists()) {
                System.err.println("Batch file not found: " + batFile);
                return;
            }
            
            // Execute the batch file
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile);
            processBuilder.directory(new File(projectRoot));
            
            Process process = processBuilder.start();
            
            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // Wait for process to complete
            int exitCode = process.waitFor();
            System.out.println("Process completed with exit code: " + exitCode);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void openReportOnly() {
        try {
            System.out.println("Opening Allure report...");
            
            String projectRoot = System.getProperty("user.dir");
            String batFile = projectRoot + File.separator + "reportGeneration.bat";
            
            File file = new File(batFile);
            if (!file.exists()) {
                System.err.println("Batch file not found: " + batFile);
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
            e.printStackTrace();
        }
    }
}
