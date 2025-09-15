# Golden Scent Mock Server

A comprehensive mock server for testing Golden Scent's Loyalty Points and Wallet system APIs.

## 🚀 Quick Start

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn

### Installation
```bash
cd MockServer
npm install
```

### Running the Server
```bash
npm start
```

The server will start on `http://localhost:3000`

### Development Mode
```bash
npm run dev
```

## 📋 API Endpoints

### 1. GET /user/loyalty-balance
Get user's loyalty points balance

**Headers:**
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
```

**Response:**
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
  },
  "timestamp": "2024-12-15T10:30:00Z"
}
```

### 2. POST /checkout/apply-points
Apply loyalty points during checkout

**Headers:**
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
Content-Type: application/json
```

**Request Body:**
```json
{
  "orderId": "order_12345",
  "pointsToUse": 100,
  "orderTotal": 299.00,
  "currency": "SAR",
  "region": "KSA"
}
```

**Response:**
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
  },
  "timestamp": "2024-12-15T10:30:00Z"
}
```

### 3. POST /checkout/use-wallet
Use wallet balance during checkout

**Headers:**
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
Content-Type: application/json
```

**Request Body:**
```json
{
  "orderId": "order_12345",
  "walletAmount": 50.00,
  "orderTotal": 299.00,
  "currency": "SAR",
  "region": "KSA",
  "paymentType": "wallet"
}
```

**Response:**
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
  },
  "timestamp": "2024-12-15T10:30:00Z"
}
```

### 4. GET /wallet/transactions
Get wallet transaction history

**Headers:**
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
```

**Query Parameters:**
- `limit` (optional): Number of transactions to return (default: 10)
- `offset` (optional): Number of transactions to skip (default: 0)
- `type` (optional): Filter by transaction type (earned, redeemed, refund)
- `startDate` (optional): Filter transactions from this date
- `endDate` (optional): Filter transactions to this date

**Example:**
```
GET /wallet/transactions?limit=5&type=refund&startDate=2024-12-01
```

**Response:**
```json
{
  "success": true,
  "data": {
    "userId": "user_12345",
    "transactions": [
      {
        "transactionId": "txn_001",
        "userId": "user_12345",
        "type": "refund",
        "amount": 75.00,
        "points": 0,
        "description": "Refund to wallet",
        "date": "2024-12-13T16:45:00Z",
        "currency": "SAR"
      }
    ],
    "pagination": {
      "limit": 5,
      "offset": 0,
      "total": 1
    },
    "currency": "SAR"
  },
  "timestamp": "2024-12-15T10:30:00Z"
}
```

### 5. POST /refund/trigger
Trigger a refund to wallet

**Headers:**
```
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
Content-Type: application/json
```

**Request Body:**
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

**Response:**
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
  },
  "timestamp": "2024-12-15T10:30:00Z"
}
```

## 🧪 Testing

### Health Check
```
GET http://localhost:3000/health
```

### Create Test Order
```
POST http://localhost:3000/orders
Content-Type: application/json

{
  "orderId": "order_12345",
  "total": 299.00,
  "currency": "SAR"
}
```

## 🔧 Configuration

### Test Users
- **Valid User**: `user_12345` with token `valid_auth_token_12345`
- **Guest User**: `guest_67890` (will get 403 for loyalty points)
- **Invalid Token**: Any other token (will get 401)

### Test Data
- **Loyalty Points**: 1500 available points
- **Wallet Balance**: 250.50 SAR
- **Minimum Threshold**: 10.00 SAR
- **Currency**: SAR (Saudi Riyal)
- **Region**: KSA (Saudi Arabia)

## 🚨 Error Scenarios

The mock server simulates various error scenarios:

### Authentication Errors
- Missing headers: 401 Unauthorized
- Invalid token: 401 Unauthorized
- Guest user accessing loyalty points: 403 Forbidden

### Validation Errors
- Missing required fields: 400 Bad Request
- Insufficient points/balance: 400 Bad Request
- Order not found: 404 Not Found
- Amount exceeds limits: 400 Bad Request

### Business Logic Errors
- Points exceed maximum percentage: 400 Bad Request
- Wallet below minimum threshold: 400 Bad Request
- Refund exceeds order total: 400 Bad Request

## 📊 Monitoring

The server logs all requests and responses to the console for debugging purposes.

## 🔄 State Management

The mock server maintains state across requests:
- Loyalty points are updated when redeemed
- Wallet balance is updated when used
- Transactions are added to the history
- Orders and refunds are tracked

## 🛠️ Customization

You can modify the mock data in `mock-server.js`:
- Change initial loyalty points
- Adjust wallet balance
- Modify transaction history
- Update currency and region settings

## 📝 Notes

- This is a mock server for testing purposes only
- Data is stored in memory and will reset when the server restarts
- All timestamps are in ISO 8601 format
- Currency calculations assume 1 point = 0.1 SAR
