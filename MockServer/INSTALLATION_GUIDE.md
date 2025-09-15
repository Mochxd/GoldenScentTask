# Golden Scent Mock Server - Installation Guide

## 🚀 Quick Start

### Prerequisites
- **Node.js** (v14 or higher) - [Download here](https://nodejs.org/)
- **npm** (comes with Node.js) or **yarn**
- **Git** (optional, for cloning)

### Step 1: Install Dependencies
```bash
cd MockServer
npm install
```

### Step 2: Start the Mock Server
```bash
npm start
```

The server will start on `http://localhost:3000`

### Step 3: Run Tests
```bash
# Run all tests
npm test

# Run specific test suites
npm run test:jest
npm run test:rest-assured

# Run with coverage
npm run test:coverage
```

## 📋 Alternative Installation Methods

### Method 1: Using Yarn
```bash
cd MockServer
yarn install
yarn start
yarn test
```

### Method 2: Using Docker (if available)
```bash
# Create Dockerfile
echo "FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD [\"npm\", \"start\"]" > Dockerfile

# Build and run
docker build -t golden-scent-mock .
docker run -p 3000:3000 golden-scent-mock
```

### Method 3: Using PM2 (Production)
```bash
npm install -g pm2
pm2 start mock-server.js --name "golden-scent-mock"
pm2 status
pm2 logs golden-scent-mock
```

## 🧪 Test Execution Options

### Option 1: Automated Test Runner
```bash
# Windows
run-tests.bat

# Linux/Mac
chmod +x run-tests.sh
./run-tests.sh
```

### Option 2: Manual Test Execution
```bash
# Terminal 1 - Start Mock Server
npm start

# Terminal 2 - Run Tests
npm test
```

### Option 3: Postman Collection
1. Import `test-automation/postman-collection.json` into Postman
2. Set environment variables:
   - `baseUrl`: `http://localhost:3000`
   - `validToken`: `Bearer valid_auth_token_12345`
   - `userId`: `user_12345`
   - `orderId`: `order_12345`
3. Run the collection

## 🔧 Configuration

### Environment Variables
Create a `.env` file:
```env
PORT=3000
NODE_ENV=development
LOG_LEVEL=info
```

### Custom Configuration
Edit `mock-server.js` to modify:
- Port number
- Mock data
- Response times
- Error scenarios

## 📊 Test Results

### Expected Test Results
- **Total Test Cases**: 14+ scenarios
- **Expected Pass Rate**: 100%
- **Response Time**: < 5 seconds
- **Coverage**: All endpoints and error scenarios

### Test Reports
- **Jest Coverage**: `coverage/lcov-report/index.html`
- **Console Output**: Detailed test results
- **Postman**: Test results in Postman Runner

## 🐛 Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Kill process using port 3000
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:3000 | xargs kill -9
```

#### 2. Node.js Version Issues
```bash
# Check Node.js version
node --version

# Update Node.js if needed
# Download from https://nodejs.org/
```

#### 3. Permission Issues (Linux/Mac)
```bash
# Fix permissions
chmod +x run-tests.sh
chmod +x mock-server.js
```

#### 4. Dependencies Issues
```bash
# Clear cache and reinstall
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

### Debug Mode
```bash
# Run with debug logs
DEBUG=* npm start

# Run tests with verbose output
npm test -- --verbose
```

## 📱 API Testing Tools

### 1. Postman
- Import collection from `test-automation/postman-collection.json`
- Set up environment variables
- Run collection or individual requests

### 2. cURL Commands
```bash
# Health check
curl http://localhost:3000/health

# Get loyalty balance
curl -H "Authorization: Bearer valid_auth_token_12345" \
     -H "User-ID: user_12345" \
     http://localhost:3000/user/loyalty-balance

# Apply points
curl -X POST http://localhost:3000/checkout/apply-points \
     -H "Authorization: Bearer valid_auth_token_12345" \
     -H "User-ID: user_12345" \
     -H "Content-Type: application/json" \
     -d '{"orderId":"order_12345","pointsToUse":100,"orderTotal":299.00,"currency":"SAR","region":"KSA"}'
```

### 3. REST Client (VS Code)
Create `test-requests.http`:
```http
### Health Check
GET http://localhost:3000/health

### Get Loyalty Balance
GET http://localhost:3000/user/loyalty-balance
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345

### Apply Points
POST http://localhost:3000/checkout/apply-points
Authorization: Bearer valid_auth_token_12345
User-ID: user_12345
Content-Type: application/json

{
  "orderId": "order_12345",
  "pointsToUse": 100,
  "orderTotal": 299.00,
  "currency": "SAR",
  "region": "KSA"
}
```

## 🚀 Production Deployment

### Using PM2
```bash
# Install PM2 globally
npm install -g pm2

# Start application
pm2 start mock-server.js --name "golden-scent-mock"

# Save PM2 configuration
pm2 save
pm2 startup

# Monitor
pm2 monit
pm2 logs golden-scent-mock
```

### Using Docker
```bash
# Build image
docker build -t golden-scent-mock .

# Run container
docker run -d -p 3000:3000 --name golden-scent-mock golden-scent-mock

# Check logs
docker logs golden-scent-mock
```

## 📚 Additional Resources

- **API Documentation**: `README.md`
- **Test Scenarios**: `test-scenarios.md`
- **Mock Server Code**: `mock-server.js`
- **Test Automation**: `test-automation/` directory

## 🤝 Support

For issues or questions:
1. Check the troubleshooting section above
2. Review the test scenarios documentation
3. Check the mock server logs
4. Verify all dependencies are installed correctly

## 📝 Notes

- The mock server runs on port 3000 by default
- All test data is stored in memory and resets on server restart
- The server includes CORS support for cross-origin requests
- All timestamps are in ISO 8601 format
- Currency calculations assume 1 point = 0.1 SAR
