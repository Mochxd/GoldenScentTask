const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Mock data
let loyaltyPoints = {
    userId: "user_12345",
    availablePoints: 1500,
    totalEarned: 2500,
    totalRedeemed: 1000,
    expiredPoints: 0,
    lastUpdated: "2024-12-15T10:30:00Z",
    pointsExpiringSoon: 200,
    currency: "SAR",
    region: "KSA"
};

let walletBalance = {
    userId: "user_12345",
    availableBalance: 250.50,
    totalDeposited: 500.00,
    totalSpent: 249.50,
    lastTransactionDate: "2024-12-15T09:15:00Z",
    currency: "SAR",
    region: "KSA",
    minimumThreshold: 10.00
};

let transactions = [
    {
        transactionId: "txn_001",
        userId: "user_12345",
        type: "earned",
        amount: 50.00,
        points: 100,
        description: "Points earned from purchase",
        date: "2024-12-15T09:15:00Z",
        currency: "SAR"
    },
    {
        transactionId: "txn_002",
        userId: "user_12345",
        type: "redeemed",
        amount: -25.00,
        points: -50,
        description: "Points redeemed for discount",
        date: "2024-12-14T14:30:00Z",
        currency: "SAR"
    },
    {
        transactionId: "txn_003",
        userId: "user_12345",
        type: "refund",
        amount: 75.00,
        points: 0,
        description: "Refund to wallet",
        date: "2024-12-13T16:45:00Z",
        currency: "SAR"
    }
];

let orders = new Map();
let refunds = new Map();

// Helper function to generate response
const generateResponse = (success, data, message = null) => {
    return {
        success,
        data,
        message,
        timestamp: new Date().toISOString()
    };
};

// Helper function to validate user
const validateUser = (req) => {
    const userId = req.headers['user-id'] || req.body.userId;
    const authToken = req.headers['authorization'];
    
    if (!userId || !authToken) {
        return { valid: false, error: "Missing user ID or authorization token" };
    }
    
    if (authToken !== "Bearer valid_auth_token_12345") {
        return { valid: false, error: "Invalid authentication token" };
    }
    
    return { valid: true, userId };
};

// API Endpoints

// GET /user/loyalty-balance
app.get('/user/loyalty-balance', (req, res) => {
    const validation = validateUser(req);
    if (!validation.valid) {
        return res.status(401).json(generateResponse(false, null, validation.error));
    }
    
    // Simulate different responses based on user
    const userId = validation.userId;
    if (userId === "guest_67890") {
        return res.status(403).json(generateResponse(false, null, "Guest users do not have access to loyalty points"));
    }
    
    // Update last updated timestamp
    loyaltyPoints.lastUpdated = new Date().toISOString();
    
    res.status(200).json(generateResponse(true, loyaltyPoints));
});

// POST /checkout/apply-points
app.post('/checkout/apply-points', (req, res) => {
    const validation = validateUser(req);
    if (!validation.valid) {
        return res.status(401).json(generateResponse(false, null, validation.error));
    }
    
    const { orderId, pointsToUse, orderTotal, currency = "SAR", region = "KSA" } = req.body;
    
    // Validation
    if (!orderId || !pointsToUse || !orderTotal) {
        return res.status(400).json(generateResponse(false, null, "Missing required fields"));
    }
    
    // Check if order exists
    if (!orders.has(orderId)) {
        return res.status(404).json(generateResponse(false, null, "Order not found"));
    }
    
    // Check if user has enough points
    if (pointsToUse > loyaltyPoints.availablePoints) {
        return res.status(400).json(generateResponse(false, null, "Insufficient loyalty points"));
    }
    
    // Check if points exceed maximum percentage (100% of order total)
    const maxPoints = Math.floor(orderTotal * 10); // 1 point = 0.1 SAR
    if (pointsToUse > maxPoints) {
        return res.status(400).json(generateResponse(false, null, "Points exceed maximum allowed percentage"));
    }
    
    // Check for expired points
    if (req.body.useExpiredPoints && loyaltyPoints.expiredPoints > 0) {
        return res.status(400).json(generateResponse(false, null, "Cannot use expired loyalty points"));
    }
    
    // Calculate discount amount (1 point = 0.1 SAR)
    const discountAmount = pointsToUse * 0.1;
    const updatedOrderTotal = orderTotal - discountAmount;
    
    // Update loyalty points
    loyaltyPoints.availablePoints -= pointsToUse;
    loyaltyPoints.totalRedeemed += pointsToUse;
    loyaltyPoints.lastUpdated = new Date().toISOString();
    
    const response = {
        orderId,
        pointsApplied: pointsToUse,
        discountAmount,
        remainingBalance: loyaltyPoints.availablePoints,
        updatedOrderTotal,
        currency,
        region
    };
    
    res.status(200).json(generateResponse(true, response));
});

// POST /checkout/use-wallet
app.post('/checkout/use-wallet', (req, res) => {
    const validation = validateUser(req);
    if (!validation.valid) {
        return res.status(401).json(generateResponse(false, null, validation.error));
    }
    
    const { orderId, walletAmount, orderTotal, currency = "SAR", region = "KSA", paymentType = "wallet" } = req.body;
    
    // Validation
    if (!orderId || !walletAmount || !orderTotal) {
        return res.status(400).json(generateResponse(false, null, "Missing required fields"));
    }
    
    // Check if order exists
    if (!orders.has(orderId)) {
        return res.status(404).json(generateResponse(false, null, "Order not found"));
    }
    
    // Check if user has enough wallet balance
    if (walletAmount > walletBalance.availableBalance) {
        return res.status(400).json(generateResponse(false, null, "Insufficient wallet balance"));
    }
    
    // Check minimum threshold
    if (walletAmount < walletBalance.minimumThreshold) {
        return res.status(400).json(generateResponse(false, null, "Wallet amount below minimum threshold"));
    }
    
    // Check if wallet amount exceeds order total
    if (walletAmount > orderTotal) {
        return res.status(400).json(generateResponse(false, null, "Wallet amount cannot exceed order total"));
    }
    
    // Update wallet balance
    walletBalance.availableBalance -= walletAmount;
    walletBalance.totalSpent += walletAmount;
    walletBalance.lastTransactionDate = new Date().toISOString();
    
    const response = {
        orderId,
        walletAmountUsed: walletAmount,
        remainingBalance: walletBalance.availableBalance,
        updatedOrderTotal: orderTotal - walletAmount,
        currency,
        transactionId: `txn_${Date.now()}`,
        paymentType
    };
    
    res.status(200).json(generateResponse(true, response));
});

// GET /wallet/balance
app.get('/wallet/balance', (req, res) => {
    const validation = validateUser(req);
    if (!validation.valid) {
        return res.status(401).json(generateResponse(false, null, validation.error));
    }
    
    // Simulate different responses based on user
    const userId = validation.userId;
    if (userId === "guest_67890") {
        return res.status(403).json(generateResponse(false, null, "Guest users do not have access to wallet balance"));
    }
    
    // Update last transaction date
    walletBalance.lastTransactionDate = new Date().toISOString();
    
    res.status(200).json(generateResponse(true, walletBalance));
});

// GET /wallet/transactions
app.get('/wallet/transactions', (req, res) => {
    const validation = validateUser(req);
    if (!validation.valid) {
        return res.status(401).json(generateResponse(false, null, validation.error));
    }
    
    const { limit = 10, offset = 0, type, startDate, endDate } = req.query;
    
    let filteredTransactions = [...transactions];
    
    // Apply filters
    if (type) {
        filteredTransactions = filteredTransactions.filter(t => t.type === type);
    }
    
    if (startDate) {
        filteredTransactions = filteredTransactions.filter(t => new Date(t.date) >= new Date(startDate));
    }
    
    if (endDate) {
        filteredTransactions = filteredTransactions.filter(t => new Date(t.date) <= new Date(endDate));
    }
    
    // Apply pagination
    const paginatedTransactions = filteredTransactions.slice(offset, offset + parseInt(limit));
    
    const response = {
        userId: validation.userId,
        transactions: paginatedTransactions,
        pagination: {
            limit: parseInt(limit),
            offset: parseInt(offset),
            total: filteredTransactions.length
        },
        currency: "SAR"
    };
    
    res.status(200).json(generateResponse(true, response));
});

// POST /refund/trigger
app.post('/refund/trigger', (req, res) => {
    const validation = validateUser(req);
    if (!validation.valid) {
        return res.status(401).json(generateResponse(false, null, validation.error));
    }
    
    const { orderId, refundAmount, refundReason, refundType = "wallet", currency = "SAR", region = "KSA" } = req.body;
    
    // Validation
    if (!orderId || !refundAmount || !refundReason) {
        return res.status(400).json(generateResponse(false, null, "Missing required fields"));
    }
    
    // Check if order exists
    if (!orders.has(orderId)) {
        return res.status(404).json(generateResponse(false, null, "Order not found"));
    }
    
    // Check if refund amount exceeds order total
    const order = orders.get(orderId);
    if (refundAmount > order.total) {
        return res.status(400).json(generateResponse(false, null, "Refund amount cannot exceed order total"));
    }
    
    // Generate refund ID
    const refundId = `refund_${Date.now()}`;
    
    // Update wallet balance if refund type is wallet
    if (refundType === "wallet") {
        walletBalance.availableBalance += refundAmount;
        walletBalance.totalDeposited += refundAmount;
        walletBalance.lastTransactionDate = new Date().toISOString();
        
        // Add transaction record
        transactions.unshift({
            transactionId: `txn_${Date.now()}`,
            userId: validation.userId,
            type: "refund",
            amount: refundAmount,
            points: 0,
            description: refundReason,
            date: new Date().toISOString(),
            currency
        });
    }
    
    // Store refund record
    refunds.set(refundId, {
        refundId,
        orderId,
        refundAmount,
        refundReason,
        refundType,
        status: "processed",
        processedAt: new Date().toISOString(),
        currency,
        region
    });
    
    const response = {
        refundId,
        orderId,
        refundAmount,
        refundType,
        status: "processed",
        currency,
        processedAt: new Date().toISOString()
    };
    
    res.status(200).json(generateResponse(true, response));
});

// Helper endpoint to create orders for testing
app.post('/orders', (req, res) => {
    const { orderId, total, currency = "SAR" } = req.body;
    orders.set(orderId, { orderId, total, currency, status: "pending" });
    res.status(201).json(generateResponse(true, { orderId, total, currency }));
});

// Health check endpoint
app.get('/health', (req, res) => {
    res.status(200).json(generateResponse(true, { status: "healthy", timestamp: new Date().toISOString() }));
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json(generateResponse(false, null, "Internal server error"));
});

// 404 handler
app.use((req, res) => {
    res.status(404).json(generateResponse(false, null, "Endpoint not found"));
});

// Start server
app.listen(PORT, () => {
    console.log(`🚀 Golden Scent Mock Server running on http://localhost:${PORT}`);
    console.log(`📋 Available endpoints:`);
    console.log(`   GET  /user/loyalty-balance`);
    console.log(`   POST /checkout/apply-points`);
    console.log(`   POST /checkout/use-wallet`);
    console.log(`   GET  /wallet/transactions`);
    console.log(`   POST /refund/trigger`);
    console.log(`   POST /orders (helper for testing)`);
    console.log(`   GET  /health`);
    console.log(`\n🔑 Test with headers:`);
    console.log(`   Authorization: Bearer valid_auth_token_12345`);
    console.log(`   User-ID: user_12345`);
});

module.exports = app;
