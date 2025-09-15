# Golden Scent Automation Project

This is a multi-module Maven project for API automation testing of the Golden Scent application.

## Project Structure

```
GoldenScentTask/
├── pom.xml                          # Parent POM file
├── README.md                        # This file
├── APIAutomationTask/               # API Testing Module
│   ├── pom.xml                     # Child POM for API tests
│   ├── src/
│   │   ├── main/java/              # Main source code
│   │   └── test/java/              # Test source code
│   ├── src/main/resources/         # Configuration and test data
│   └── target/                     # Compiled classes and test results
└── MockServer/                     # Mock server for testing
    ├── package.json
    ├── mock-server.js
    └── test-automation/
```

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Node.js (for MockServer)

## Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd GoldenScentTask
```

### 2. Build the Project
```bash
# Build all modules
mvn clean install

# Build only the API automation module
mvn clean install -pl APIAutomationTask

# Skip tests during build
mvn clean install -DskipTests
```

### 3. Run Tests
```bash
# Run all tests
mvn test

# Run tests from specific module
mvn test -pl APIAutomationTask

# Run tests with Allure reporting
mvn test -Pallure
```

### 4. Generate Allure Reports
```bash
# Generate Allure report
mvn allure:report

# Serve Allure report locally
mvn allure:serve
```

## Modules

### APIAutomationTask
Contains the main API automation test suite with:
- Rest Assured for API testing
- TestNG for test framework
- Allure for reporting
- Jackson for JSON processing

### MockServer
Contains a Node.js mock server for testing scenarios.

## Configuration

The project uses Maven's dependency management to ensure consistent versions across all modules. All dependencies are defined in the parent POM and inherited by child modules.

## Dependencies

- **Rest Assured 5.5.1** - API testing framework
- **TestNG 7.11.0** - Testing framework
- **Allure 2.24.0** - Test reporting
- **Jackson 2.15.3** - JSON processing
- **Gson 2.9.0** - JSON processing

## Running Specific Tests

```bash
# Run specific test class
mvn test -Dtest=TestClassName

# Run specific test method
mvn test -Dtest=TestClassName#testMethodName

# Run tests matching a pattern
mvn test -Dtest="*ApiTests"
```

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests to ensure everything works
4. Submit a pull request

## License

This project is for internal use at Golden Scent.
