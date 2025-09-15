# Golden Scent API Test Automation Framework

A comprehensive API testing solution for Golden Scent's Loyalty Points and Wallet system, featuring a complete mock server implementation and professional test automation framework.

## 🎯 Project Overview

This project provides a complete end-to-end testing solution for Golden Scent's e-commerce loyalty and wallet system:

- **Mock Server**: Node.js/Express server simulating real API behavior with comprehensive test scenarios
- **Test Automation**: Java framework with RestAssured for professional API testing
- **Reporting**: Allure reports with detailed test execution results and analytics
- **Test Management**: TestNG for organized test execution and management
- **Multi-Environment**: Support for different test environments and configurations

## 📋 What We Should Do - Action Plan

### ✅ **Phase 1: Environment Setup & Validation**
1. **Prerequisites Installation**
   - Install Java 21+ and verify installation
   - Install Maven 3.9+ and verify installation  
   - Install Node.js 18+ and npm
   - Install Allure Commandline for reporting

2. **Project Validation**
   - Verify all dependencies are properly configured
   - Test mock server startup and health checks
   - Validate test framework compilation and execution
   - Confirm Allure reporting functionality

### ✅ **Phase 2: Test Execution & Analysis**
3. **Mock Server Testing**
   - Start the Node.js mock server (`cd MockServer && npm start`)
   - Verify all 7 API endpoints are responding correctly
   - Test authentication and error scenarios
   - Validate mock data consistency

4. **API Test Suite Execution**
   - Run complete Java test suite using TestNG
   - Execute both positive and negative test scenarios
   - Generate and analyze Allure reports
   - Verify test coverage across all API endpoints

### ✅ **Phase 3: Documentation & Reporting**
5. **Test Results Analysis**
   - Review test execution results and pass/fail rates
   - Analyze API response times and performance metrics
   - Document any issues or improvements needed
   - Generate comprehensive test reports

6. **Project Documentation**
   - Update README with complete setup instructions
   - Document test scenarios and coverage
   - Create troubleshooting guides
   - Prepare project handover documentation

## 🏗️ Project Architecture

```
GoldenScentTask/
├── 📁 APIAutomationTask/           # Java Test Framework
│   ├── 📄 pom.xml                  # Maven dependencies & configuration
│   ├── 📄 TestNG.xml              # TestNG test suite configuration
│   ├── 📁 src/main/java/          # Framework utilities & configuration
│   │   ├── 📁 apiConfig/          # HTTP headers & request configuration
│   │   └── 📁 utils/              # Properties loader & Java utilities
│   ├── 📁 src/main/resources/     # Configuration & test data
│   │   ├── 📄 config.properties   # API endpoints configuration
│   │   └── 📁 testData/           # JSON test data files
│   └── 📁 src/test/java/          # Test implementation
│       ├── 📁 Base/               # RestAssured base test class
│       ├── 📁 apiVerification/    # Assertion utilities
│       ├── 📁 listeners/          # TestNG event listeners
│       ├── 📁 Responses/          # Response POJO classes
│       └── 📁 testCases/          # Test case implementations
│           └── 📁 GoldenScentApiTests/
│               ├── 📄 LoyaltyPointsApiTests.java
│               └── 📄 WalletApiTests.java
├── 📁 MockServer/                 # Node.js Mock API Server
│   ├── 📄 package.json            # Node.js dependencies
│   ├── 📄 mock-server.js          # Main server implementation
│   ├── 📄 test-scenarios.md       # Comprehensive test scenarios
│   └── 📁 test-automation/        # Additional test implementations
├── 📄 pom.xml                     # Parent Maven POM
├── 📄 run-maven.bat              # Maven execution script
└── 📄 test-api-module.bat        # API module testing script
```

## 🔧 Prerequisites & Setup

### Required Software
- **Java 21+** - For test framework execution
- **Maven 3.9+** - For dependency management and build
- **Node.js 18+** - For mock server
- **Allure Commandline** - For test reporting

### Quick Setup Commands
```bash
# 1. Verify installations
java -version    # Should show Java 21+
mvn -version     # Should show Maven 3.9+
node -v          # Should show Node.js 18+
npm -v           # Should show npm version

# 2. Install Allure (Windows with Scoop)
scoop install allure

# 3. Clone and navigate to project
git clone <repository-url>
cd GoldenScentTask
```

## 🚀 Execution Workflow

### Step 1: Start Mock Server
```bash
cd MockServer
npm install          # Install dependencies (first time only)
npm start            # Start mock server on http://localhost:3000
```

**Expected Output:**
```
🚀 Golden Scent Mock Server running on http://localhost:3000
📋 Available endpoints:
   GET  /user/loyalty-balance
   POST /checkout/apply-points
   POST /checkout/use-wallet
   GET  /wallet/balance
   GET  /wallet/transactions
   POST /refund/trigger
   POST /orders (helper for testing)
   GET  /health
```

### Step 2: Execute Test Suite
```bash
# Option A: Using batch file (Recommended)
cd APIAutomationTask
runTestNG.bat

# Option B: Using Maven directly
mvn clean test -DsuiteXmlFile=TestNG.xml

# Option C: From root directory
mvn clean test -pl APIAutomationTask
```

### Step 3: Generate Reports
```bash
# Allure report generation (automatic with TestNG listener)
cd APIAutomationTask
reportGeneration.bat

# Or manually
mvn allure:report
mvn allure:serve
```

## 📊 Test Coverage Analysis

### API Endpoints Covered
| Endpoint | Method | Test Scenarios | Status |
|----------|--------|----------------|---------|
| `/user/loyalty-balance` | GET | Valid user, Invalid token, Guest user | ✅ Complete |
| `/checkout/apply-points` | POST | Valid request, Insufficient points, Invalid order | ✅ Complete |
| `/checkout/use-wallet` | POST | Valid request, Insufficient balance, Below minimum | ✅ Complete |
| `/wallet/balance` | GET | Valid user, Invalid token | ✅ Complete |
| `/wallet/transactions` | GET | Valid request, Filtered results | ✅ Complete |
| `/refund/trigger` | POST | Valid request, Exceed order amount | ✅ Complete |
| `/health` | GET | Health check | ✅ Complete |

### Test Scenarios (14 Total)
- **Positive Tests**: 6 scenarios (valid user operations)
- **Negative Tests**: 8 scenarios (error handling, validation)
- **Authentication Tests**: 3 scenarios (valid/invalid/guest users)
- **Business Logic Tests**: 11 scenarios (loyalty points, wallet operations)

## 🛠️ Key Features

### Mock Server Capabilities
- **Realistic API Simulation**: Complete loyalty points and wallet system
- **State Management**: Maintains user balances and transaction history
- **Error Simulation**: Comprehensive error scenarios for testing
- **Authentication**: Token-based authentication with multiple user types
- **Data Persistence**: In-memory state management across requests

### Test Framework Features
- **RestAssured Integration**: Professional API testing with fluent syntax
- **TestNG Framework**: Organized test execution with priorities and groups
- **Allure Reporting**: Beautiful HTML reports with detailed analytics
- **POJO Classes**: Type-safe response handling with Jackson/Gson
- **Configuration Management**: Externalized configuration and test data
- **Assertion Utilities**: Reusable verification methods

### Test Data Management
- **JSON Test Data**: Externalized test data in structured JSON files
- **Configuration Properties**: Environment-specific endpoint configuration
- **Dynamic Test Data**: Runtime test data generation and manipulation

## 📈 Expected Results

### Test Execution Metrics
- **Total Test Cases**: 14 comprehensive scenarios
- **Expected Pass Rate**: 100% (all tests should pass with mock server)
- **Response Time**: < 3 seconds for all API calls
- **Coverage**: 100% endpoint coverage with positive and negative scenarios

### Report Features
- **Test Summary**: Pass/Fail/Skip statistics
- **Timeline View**: Test execution timeline and duration
- **API Interactions**: Complete request/response logging
- **Error Analysis**: Detailed failure analysis and debugging info
- **Categories**: Test categorization by severity and feature

## 🔍 Troubleshooting

### Common Issues & Solutions

#### Mock Server Issues
```bash
# Port already in use
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Dependencies not installed
cd MockServer
npm install
```

#### Test Execution Issues
```bash
# Java version mismatch
java -version  # Should be Java 21+

# Maven build issues
mvn clean install -DskipTests
mvn clean test -pl APIAutomationTask
```

#### Allure Report Issues
```bash
# Allure not installed
scoop install allure  # Windows
# Or download from https://github.com/allure-framework/allure2/releases

# Report generation fails
mvn allure:clean
mvn allure:report
```

## 📝 Next Steps & Improvements

### Immediate Actions Required
1. **Execute Complete Test Suite**: Run all tests and verify 100% pass rate
2. **Validate Mock Server**: Ensure all endpoints respond correctly
3. **Generate Reports**: Create comprehensive Allure reports
4. **Document Results**: Record test execution results and any issues

### Potential Enhancements
1. **CI/CD Integration**: Add GitHub Actions or Jenkins pipeline
2. **Performance Testing**: Add load testing scenarios
3. **Database Integration**: Connect to real database for data persistence
4. **API Documentation**: Generate OpenAPI/Swagger documentation
5. **Test Data Management**: Implement test data factories and builders

### Project Handover Checklist
- [ ] All tests passing with mock server
- [ ] Allure reports generated and accessible
- [ ] Documentation updated and complete
- [ ] Setup instructions verified
- [ ] Troubleshooting guide tested
- [ ] Project structure documented

## 📞 Support & Contact

For technical support or questions:
- **Email**: mohameddmostafa98@gmail.com
- **Project Issues**: Check troubleshooting section first
- **Documentation**: Refer to individual module READMEs

