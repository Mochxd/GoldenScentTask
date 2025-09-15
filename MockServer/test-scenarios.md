# Golden Scent API Test Scenarios

## Test Case Design Structure

Each test case includes:
- **Endpoint**: API endpoint being tested
- **HTTP Method**: GET, POST, etc.
- **Test Scenario**: Description of what is being tested
- **Request Payload**: Sample request data
- **Expected Response**: Status code and response body
- **Validation Logic**: What to assert/validate
- **Test Data**: Required test data setup

---

## 1. GET /user/loyalty-balance

### TC001: Get Loyalty Balance - Valid User
- **Endpoint**: `/user/loyalty-balance`
- **HTTP Method**: GET
- **Test Scenario**: Valid authenticated user should be able to retrieve their loyalty points balance
- **Headers**:
  ```
  Authorization: Bearer valid_auth_token_12345
  User-ID: user_12345
  ```
- **Expected Response**: 200 OK
- **Expected Response Body**:
  ```json
  {
    "success": true,
    "data": {
      "userId": "user_12345",
      "availablePoints": 1500,
      "totalEarned": 2500,
      "totalRedeemed": 1000,
      "expiredPoints": 0,
      "lastUpdated": "2024-12-15T10:30:00Z",
      "pointsExpiringSoon": 200,
      "currency": "SAR",
      "region": "KSA"
    }
  }
  ```
- **Validation Logic**:
  - Status code = 200
  - Response time < 2 seconds
  - `success` = true
  - `data.userId` = "user_12345"
  - `data.availablePoints` is a number > 0
  - `data.currency` = "SAR"
  - `data.region` = "KSA"

### TC002: Get Loyalty Balance - Invalid Token
- **Endpoint**: `/user/loyalty-balance`
- **HTTP Method**: GET
- **Test Scenario**: User with invalid token should be denied access
- **Headers**:
  ```
  Authorization: Bearer invalid_token
  User-ID: user_12345
  ```
- **Expected Response**: 401 Unauthorized
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Invalid authentication token"
  }
  ```
- **Validation Logic**:
  - Status code = 401
  - `success` = false
  - `message` contains "Invalid authentication token"

### TC003: Get Loyalty Balance - Guest User
- **Endpoint**: `/user/loyalty-balance`
- **HTTP Method**: GET
- **Test Scenario**: Guest users should not have access to loyalty points
- **Headers**:
  ```
  User-ID: guest_67890
  ```
- **Expected Response**: 403 Forbidden
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Guest users do not have access to loyalty points"
  }
  ```
- **Validation Logic**:
  - Status code = 403
  - `success` = false
  - `message` contains "Guest users do not have access"

---

## 2. POST /checkout/apply-points

### TC004: Apply Points - Valid Request
- **Endpoint**: `/checkout/apply-points`
- **HTTP Method**: POST
- **Test Scenario**: Valid user should be able to apply loyalty points during checkout
- **Headers**:
  ```
  Authorization: Bearer valid_auth_token_12345
  User-ID: user_12345
  Content-Type: application/json
  ```
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "pointsToUse": 100,
    "orderTotal": 299.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 200 OK
- **Expected Response Body**:
  ```json
  {
    "success": true,
    "data": {
      "orderId": "order_12345",
      "pointsApplied": 100,
      "discountAmount": 10.00,
      "remainingBalance": 1400,
      "updatedOrderTotal": 289.00,
      "currency": "SAR",
      "region": "KSA"
    }
  }
  ```
- **Validation Logic**:
  - Status code = 200
  - Response time < 3 seconds
  - `success` = true
  - `data.pointsApplied` = 100
  - `data.discountAmount` = 10.00 (100 points * 0.1 SAR)
  - `data.updatedOrderTotal` = 289.00 (299.00 - 10.00)

### TC005: Apply Points - Insufficient Points
- **Endpoint**: `/checkout/apply-points`
- **HTTP Method**: POST
- **Test Scenario**: User should not be able to apply more points than available
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "pointsToUse": 10000,
    "orderTotal": 299.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 400 Bad Request
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Insufficient loyalty points"
  }
  ```
- **Validation Logic**:
  - Status code = 400
  - `success` = false
  - `message` contains "Insufficient loyalty points"

### TC006: Apply Points - Exceed Maximum Percentage
- **Endpoint**: `/checkout/apply-points`
- **HTTP Method**: POST
- **Test Scenario**: User should not be able to apply points exceeding 100% of order total
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "pointsToUse": 5000,
    "orderTotal": 100.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 400 Bad Request
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Points exceed maximum allowed percentage"
  }
  ```
- **Validation Logic**:
  - Status code = 400
  - `success` = false
  - `message` contains "exceed maximum allowed percentage"

### TC007: Apply Points - Invalid Order
- **Endpoint**: `/checkout/apply-points`
- **HTTP Method**: POST
- **Test Scenario**: User should not be able to apply points to non-existent order
- **Request Payload**:
  ```json
  {
    "orderId": "invalid_order_123",
    "pointsToUse": 100,
    "orderTotal": 299.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 404 Not Found
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Order not found"
  }
  ```
- **Validation Logic**:
  - Status code = 404
  - `success` = false
  - `message` contains "Order not found"

---

## 3. POST /checkout/use-wallet

### TC008: Use Wallet - Valid Request
- **Endpoint**: `/checkout/use-wallet`
- **HTTP Method**: POST
- **Test Scenario**: Valid user should be able to use wallet balance during checkout
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "walletAmount": 50.00,
    "orderTotal": 299.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 200 OK
- **Expected Response Body**:
  ```json
  {
    "success": true,
    "data": {
      "orderId": "order_12345",
      "walletAmountUsed": 50.00,
      "remainingBalance": 200.50,
      "updatedOrderTotal": 249.00,
      "currency": "SAR",
      "transactionId": "txn_1702641000000",
      "paymentType": "wallet"
    }
  }
  ```
- **Validation Logic**:
  - Status code = 200
  - Response time < 3 seconds
  - `success` = true
  - `data.walletAmountUsed` = 50.00
  - `data.updatedOrderTotal` = 249.00 (299.00 - 50.00)
  - `data.transactionId` is not null

### TC009: Use Wallet - Insufficient Balance
- **Endpoint**: `/checkout/use-wallet`
- **HTTP Method**: POST
- **Test Scenario**: User should not be able to use more wallet balance than available
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "walletAmount": 1000.00,
    "orderTotal": 299.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 400 Bad Request
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Insufficient wallet balance"
  }
  ```
- **Validation Logic**:
  - Status code = 400
  - `success` = false
  - `message` contains "Insufficient wallet balance"

### TC010: Use Wallet - Below Minimum Threshold
- **Endpoint**: `/checkout/use-wallet`
- **HTTP Method**: POST
- **Test Scenario**: User should not be able to use wallet amount below minimum threshold
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "walletAmount": 5.00,
    "orderTotal": 299.00,
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 400 Bad Request
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Wallet amount below minimum threshold"
  }
  ```
- **Validation Logic**:
  - Status code = 400
  - `success` = false
  - `message` contains "below minimum threshold"

---

## 4. GET /wallet/transactions

### TC011: Get Transactions - Valid Request
- **Endpoint**: `/wallet/transactions`
- **HTTP Method**: GET
- **Test Scenario**: Valid user should be able to retrieve wallet transaction history
- **Headers**:
  ```
  Authorization: Bearer valid_auth_token_12345
  User-ID: user_12345
  ```
- **Expected Response**: 200 OK
- **Expected Response Body**:
  ```json
  {
    "success": true,
    "data": {
      "userId": "user_12345",
      "transactions": [
        {
          "transactionId": "txn_001",
          "userId": "user_12345",
          "type": "earned",
          "amount": 50.00,
          "points": 100,
          "description": "Points earned from purchase",
          "date": "2024-12-15T09:15:00Z",
          "currency": "SAR"
        }
      ],
      "pagination": {
        "limit": 10,
        "offset": 0,
        "total": 3
      },
      "currency": "SAR"
    }
  }
  ```
- **Validation Logic**:
  - Status code = 200
  - Response time < 2 seconds
  - `success` = true
  - `data.transactions` is an array
  - `data.pagination.limit` = 10
  - `data.pagination.total` > 0

### TC012: Get Transactions - With Filters
- **Endpoint**: `/wallet/transactions?type=refund&limit=5`
- **HTTP Method**: GET
- **Test Scenario**: User should be able to filter transactions by type and limit results
- **Expected Response**: 200 OK
- **Validation Logic**:
  - Status code = 200
  - `success` = true
  - All transactions have `type` = "refund"
  - `data.pagination.limit` = 5

---

## 5. POST /refund/trigger

### TC013: Trigger Refund - Valid Request
- **Endpoint**: `/refund/trigger`
- **HTTP Method**: POST
- **Test Scenario**: Valid user should be able to trigger refund to wallet
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "refundAmount": 75.50,
    "refundReason": "Customer requested return",
    "refundType": "wallet",
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 200 OK
- **Expected Response Body**:
  ```json
  {
    "success": true,
    "data": {
      "refundId": "refund_1702641000000",
      "orderId": "order_12345",
      "refundAmount": 75.50,
      "refundType": "wallet",
      "status": "processed",
      "currency": "SAR",
      "processedAt": "2024-12-15T10:30:00Z"
    }
  }
  ```
- **Validation Logic**:
  - Status code = 200
  - Response time < 5 seconds
  - `success` = true
  - `data.refundId` is not null
  - `data.status` = "processed"
  - `data.refundAmount` = 75.50

### TC014: Trigger Refund - Exceed Order Amount
- **Endpoint**: `/refund/trigger`
- **HTTP Method**: POST
- **Test Scenario**: User should not be able to refund more than order total
- **Request Payload**:
  ```json
  {
    "orderId": "order_12345",
    "refundAmount": 1000.00,
    "refundReason": "Customer requested return",
    "refundType": "wallet",
    "currency": "SAR",
    "region": "KSA"
  }
  ```
- **Expected Response**: 400 Bad Request
- **Expected Response Body**:
  ```json
  {
    "success": false,
    "data": null,
    "message": "Refund amount cannot exceed order total"
  }
  ```
- **Validation Logic**:
  - Status code = 400
  - `success` = false
  - `message` contains "cannot exceed order total"

---

## Test Data Setup

### Required Test Orders
Before running checkout tests, create test orders:
```bash
POST http://localhost:3000/orders
Content-Type: application/json

{
  "orderId": "order_12345",
  "total": 299.00,
  "currency": "SAR"
}
```

### Test User Credentials
- **Valid User**: `user_12345` with token `valid_auth_token_12345`
- **Guest User**: `guest_67890` (no token required)
- **Invalid Token**: Any other token

### Expected Test Results
- **Total Test Cases**: 14
- **Expected Pass Rate**: 100%
- **Response Time**: < 5 seconds for all requests
- **Coverage**: All endpoints, success and error scenarios

---

## Automation Scripts

See the following files for automated test implementations:
- `test-automation/rest-assured-tests.js` - REST Assured Java tests
- `test-automation/postman-collection.json` - Postman collection
- `test-automation/jest-tests.js` - Jest JavaScript tests
