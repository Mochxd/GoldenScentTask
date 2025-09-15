# Golden Scent API Test Automation Framework

A comprehensive API testing framework for Golden Scent's Loyalty Points and Wallet system, featuring a Node.js mock server and Java-based test automation using RestAssured, TestNG, and Allure reporting.

## Overview

This project demonstrates a complete API testing solution with:
- Mock server simulation of real API behavior
- Automated test execution with comprehensive coverage
- Professional reporting and test management
- Modular and maintainable test architecture

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Mock Server Setup](#mock-server-setup)
- [Test Automation Setup](#test-automation-setup)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Test Scenarios](#test-scenarios)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

## 🎯 Project Overview

This project provides a complete testing solution for Golden Scent's e-commerce loyalty and wallet system:

- **Mock Server**: Node.js/Express server simulating real API behavior
- **Test Automation**: Java framework with RestAssured for API testing
- **Reporting**: Allure reports with detailed test execution results
- **Test Management**: TestNG for test organization and execution

## 🏗️ Architecture

```
GoldenScentTask/
├── MockServer/                    # Node.js Mock API Server
│   ├── mock-server.js            # Main server implementation
│   ├── package.json              # Dependencies
│   └── README.md                 # Server documentation
└── APIAutomationTask/            # Java Test Framework
    ├── src/main/java/            # Framework utilities
    ├── src/test/java/            # Test implementations
    ├── src/main/resources/       # Configuration & test data
    ├── testng.xml               # TestNG configuration
    └── pom.xml                  # Maven dependencies
```

## 🔧 Prerequisites

### For Mock Server
- Node.js 18+
- npm

### For Test Automation
- Java 21
- Maven 3.9+
- Allure Commandline

## 🚀 Mock Server Setup

### 1. Install Dependencies
```bash
cd MockServer
npm install
```

### 2. Start the Server
```bash
node mock-server.js
```

The server will start on `http://localhost:3000` with the following output:
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

### 3. Verify Server
```bash
curl http://localhost:3000/health
```

## 🧪 Test Automation Setup

### 1. Navigate to Test Framework
```bash
cd APIAutomationTask
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Configuration
Check `src/main/resources/config.properties`:
```properties
baseUrl = http://localhost:3000
loyaltyBalanceEndPoint = /user/loyalty-balance
applyPointsEndPoint = /checkout/apply-points
useWalletEndPoint = /checkout/use-wallet
walletBalanceEndPoint = /wallet/balance
walletTransactionsEndPoint = /wallet/transactions
refundTriggerEndPoint = /refund/trigger
ordersEndPoint = /orders
healthEndPoint = /health
```

## ▶️ Running Tests

> **⚠️ IMPORTANT**: Before running any tests, you must start the mock server first!

### Prerequisites - Start Mock Server
```bash
# Navigate to MockServer directory
cd MockServer

# Install dependencies (if not already done)
npm install

# Start the mock server
npm start
```

The server will start on `http://localhost:3000`. Keep this terminal open while running tests.

### Method 1: Batch File (Recommended)
```bash
# Run all tests and open Allure report
runTestNG.bat
```

### Method 2: Maven Command
```bash
# Run all tests
mvn clean test -DsuiteXmlFile=testng.xml

# Run specific test class
mvn test -Dtest=LoyaltyPointsApiTests

# Run specific test method
mvn test -Dtest=LoyaltyPointsApiTests#getUserLoyaltyBalance_ValidUser
```

### Method 3: IDE Execution
- Right-click on `testng.xml` → Run As → TestNG Suite
- Right-click on test class → Run As → TestNG Test

## 📊 Test Reports

### Allure Report Generation
After test execution, the TestNG listener automatically opens the Allure report:

```bash
# Manual report generation
reportGeneration.bat
```

### Report Features
- **Test Execution Summary**: Pass/Fail/Skip statistics
- **Detailed Test Results**: Step-by-step execution logs
- **API Request/Response**: Complete HTTP interaction details
- **Screenshots**: Error captures (if applicable)
- **Timeline**: Test execution timeline
- **Categories**: Test categorization by severity

## 📁 Project Structure

```
APIAutomationTask/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── apiConfig/
│   │   │   │   └── HeaderConfig.java          # HTTP headers management
│   │   │   └── utils/
│   │   │       ├── JavaUtilities.java         # Configuration utilities
│   │   │       └── PropertiesLoader.java      # Properties file loader
│   │   └── resources/
│   │       ├── config.properties              # API endpoints configuration
│   │       └── testData/                      # JSON test data files
│   │           ├── ApplyPointsRequest.json
│   │           ├── CreateOrderRequest.json
│   │           ├── RefundTriggerRequest.json
│   │           └── UseWalletRequest.json
│   └── test/
│       ├── java/
│       │   ├── Base/
│       │   │   └── BaseTest.java              # RestAssured specifications
│       │   ├── apiVerification/
│       │   │   └── ApiVerification.java       # Assertion utilities
│       │   ├── listeners/
│       │   │   └── TestNGListener.java        # TestNG event handling
│       │   ├── Responses/
│       │   │   ├── ApplyPointsResponse.java   # Response POJOs
│       │   │   ├── LoyaltyBalanceResponse.java
│       │   │   ├── RefundTriggerResponse.java
│       │   │   ├── UseWalletResponse.java
│       │   │   └── WalletTransactionsResponse.java
│       │   └── testCases/
│       │       ├── CommonMethods.java         # API call methods
│       │       ├── TestRunner.java           # Programmatic test execution
│       │       └── GoldenScentApiTests/
│       │           ├── LoyaltyPointsApiTests.java  # Loyalty points tests
│       │           ├── WalletApiTests.java         # Wallet tests
│       │           ├── TestExecution.java          # Batch execution tests
│       │           └── TestRunnerWithListener.java # Listener demo
│       └── resources/
├── testng.xml                                 # TestNG configuration
├── pom.xml                                    # Maven dependencies
├── reportGeneration.bat                       # Allure report launcher
└── runTestNG.bat                             # Test execution script
```

## 🔌 API Endpoints

### Authentication
All protected endpoints require:
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
```

### Loyalty Points API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/user/loyalty-balance` | Get user's loyalty points balance |
| POST | `/checkout/apply-points` | Apply loyalty points during checkout |

### Wallet API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/wallet/balance` | Get user's wallet balance |
| POST | `/checkout/use-wallet` | Use wallet balance during checkout |
| GET | `/wallet/transactions` | Get wallet transaction history |

### Refund API
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/refund/trigger` | Trigger refund to wallet |

### Helper Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/orders` | Create test order |
| GET | `/health` | Health check |

## 🧪 Test Scenarios

### Loyalty Points Tests
- ✅ **Valid User Balance**: Retrieve loyalty points for authenticated user
- ✅ **Apply Points Success**: Apply valid loyalty points to order
- ❌ **Invalid Token**: Test with invalid authentication token
- ❌ **Guest User**: Test guest user access (403 Forbidden)
- ❌ **Insufficient Points**: Test with more points than available
- ❌ **Invalid Order**: Test with non-existent order ID
- ❌ **Exceed Max Percentage**: Test points exceeding order total
- ❌ **Missing Fields**: Test with incomplete request data
- ❌ **Different Region**: Test with unsupported region

### Wallet Tests
- ✅ **Valid User Balance**: Retrieve wallet balance for authenticated user
- ✅ **Use Wallet Success**: Use valid wallet amount for order
- ✅ **Transaction History**: Retrieve wallet transaction history
- ✅ **Filtered Transactions**: Test transaction filtering and pagination
- ✅ **Refund Success**: Process refund to wallet
- ❌ **Invalid Token**: Test with invalid authentication token
- ❌ **Insufficient Balance**: Test with more amount than available
- ❌ **Below Minimum**: Test below minimum threshold
- ❌ **Exceed Order Total**: Test wallet amount exceeding order total
- ❌ **Invalid Order**: Test refund with non-existent order
- ❌ **Exceed Refund**: Test refund exceeding order amount
- ❌ **Different Region**: Test with unsupported region

## ⚙️ Configuration

### Test Data
Test data is stored in JSON files under `src/main/resources/testData/`:

```json
// ApplyPointsRequest.json
{
  "orderId": "order_12345",
  "pointsToUse": 100,
  "orderTotal": 299.00,
  "currency": "SAR",
  "region": "KSA"
}
```

### TestNG Configuration
The `testng.xml` file defines test execution order and includes the TestNG listener:

```xml
<suite name="Golden Scent API Tests" verbose="1">
    <listeners>
        <listener class-name="listeners.TestNGListener"/>
    </listeners>
    <test name="All API Tests">
        <classes>
            <class name="testCases.GoldenScentApiTests.LoyaltyPointsApiTests"/>
            <class name="testCases.GoldenScentApiTests.WalletApiTests"/>
        </classes>
    </test>
</suite>
```

### Maven Dependencies
Key dependencies in `pom.xml`:
- **RestAssured 5.5.1**: API testing framework
- **TestNG 7.11.0**: Test execution framework
- **Allure 2.24.0**: Test reporting
- **Jackson 2.15.3**: JSON processing
- **Gson 2.9.0**: JSON serialization

## 🔧 Troubleshooting

### Common Issues

#### 1. Mock Server Not Running
**Error**: 404 Not Found
**Solution**: Start the mock server from `MockServer` directory:
```bash
cd MockServer
node mock-server.js
```

#### 2. Authentication Errors
**Error**: 401 Unauthorized
**Solution**: Ensure proper headers are set:
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
```

#### 3. Allure Report Not Opening
**Error**: Report generation fails
**Solution**: Install Allure commandline and ensure it's in PATH:
```bash
# Windows (using Scoop)
scoop install allure

# Or download from https://github.com/allure-framework/allure2/releases
```

#### 4. Test Execution Fails
**Error**: Maven build fails
**Solution**: Check Java version and Maven installation:
```bash
java -version  # Should be Java 21
mvn -version   # Should be Maven 3.9+
```

#### 5. Port Already in Use
**Error**: Port 3000 already in use
**Solution**: Kill existing process or change port in mock server:
```bash
# Kill process on port 3000
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

### Debug Mode
Enable detailed logging by setting Maven debug level:
```bash
mvn clean test -DsuiteXmlFile=testng.xml -X
```

## 📈 Test Execution Flow

1. **Start Mock Server**: `node mock-server.js`
2. **Run Tests**: `runTestNG.bat` or `mvn test`
3. **TestNG Listener**: Captures test execution events
4. **Allure Report**: Automatically generated and opened
5. **Analysis**: Review test results and API interactions

## 🎯 Best Practices

- Always start the mock server before running tests
- Use descriptive test method names
- Include both positive and negative test scenarios
- Verify API responses thoroughly
- Use proper assertion messages
- Keep test data in separate JSON files
- Follow the existing code structure for new tests

## 📞 Support

For issues or questions contact me on mohameddmostafa98@gmail.com:
1. Check the troubleshooting section
2. Verify mock server is running
3. Check test logs for detailed error messages
4. Ensure all dependencies are properly installed

---

**Happy Testing! 🚀**
