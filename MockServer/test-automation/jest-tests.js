const request = require('supertest');
const app = require('../mock-server');

describe('Golden Scent API Tests - Jest', () => {
    const baseUrl = 'http://localhost:3000';
    const validHeaders = {
        'Authorization': 'Bearer valid_auth_token_12345',
        'User-ID': 'user_12345',
        'Content-Type': 'application/json'
    };
    const guestHeaders = {
        'User-ID': 'guest_67890'
    };

    beforeAll(async () => {
        // Create test order
        await request(app)
            .post('/orders')
            .send({
                orderId: 'order_12345',
                total: 299.00,
                currency: 'SAR'
            });
    });

    describe('Authentication Tests', () => {
        test('Valid user should access loyalty balance', async () => {
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.userId).toBe('user_12345');
        });

        test('Invalid token should be rejected', async () => {
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set({
                    'Authorization': 'Bearer invalid_token',
                    'User-ID': 'user_12345'
                });

            expect(response.status).toBe(401);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Invalid authentication token');
        });

        test('Guest user should be denied loyalty access', async () => {
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(guestHeaders);

            expect(response.status).toBe(403);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Guest users do not have access');
        });
    });

    describe('Loyalty Points Tests', () => {
        test('Apply points with valid request', async () => {
            const requestBody = {
                orderId: 'order_12345',
                pointsToUse: 100,
                orderTotal: 299.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/apply-points')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.pointsApplied).toBe(100);
            expect(response.body.data.discountAmount).toBe(10.00);
            expect(response.body.data.updatedOrderTotal).toBe(289.00);
        });

        test('Apply points with insufficient balance', async () => {
            const requestBody = {
                orderId: 'order_12345',
                pointsToUse: 10000,
                orderTotal: 299.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/apply-points')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(400);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Insufficient loyalty points');
        });

        test('Apply points exceeding maximum percentage', async () => {
            const requestBody = {
                orderId: 'order_12345',
                pointsToUse: 5000,
                orderTotal: 100.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/apply-points')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(400);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('exceed maximum allowed percentage');
        });
    });

    describe('Wallet Tests', () => {
        test('Use wallet with valid request', async () => {
            const requestBody = {
                orderId: 'order_12345',
                walletAmount: 50.00,
                orderTotal: 299.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/use-wallet')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.walletAmountUsed).toBe(50.00);
            expect(response.body.data.updatedOrderTotal).toBe(249.00);
            expect(response.body.data.transactionId).toBeDefined();
        });

        test('Use wallet with insufficient balance', async () => {
            const requestBody = {
                orderId: 'order_12345',
                walletAmount: 1000.00,
                orderTotal: 299.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/use-wallet')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(400);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Insufficient wallet balance');
        });

        test('Use wallet below minimum threshold', async () => {
            const requestBody = {
                orderId: 'order_12345',
                walletAmount: 5.00,
                orderTotal: 299.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/use-wallet')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(400);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('below minimum threshold');
        });
    });

    describe('Transaction History Tests', () => {
        test('Get wallet transactions', async () => {
            const response = await request(app)
                .get('/wallet/transactions')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(Array.isArray(response.body.data.transactions)).toBe(true);
            expect(response.body.data.pagination.limit).toBe(10);
            expect(response.body.data.pagination.total).toBeGreaterThan(0);
        });

        test('Get wallet transactions with filters', async () => {
            const response = await request(app)
                .get('/wallet/transactions?type=refund&limit=5')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.pagination.limit).toBe(5);
        });
    });

    describe('Refund Tests', () => {
        test('Trigger refund with valid request', async () => {
            const requestBody = {
                orderId: 'order_12345',
                refundAmount: 75.50,
                refundReason: 'Customer requested return',
                refundType: 'wallet',
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/refund/trigger')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.refundId).toBeDefined();
            expect(response.body.data.status).toBe('processed');
            expect(response.body.data.refundAmount).toBe(75.50);
        });

        test('Trigger refund exceeding order amount', async () => {
            const requestBody = {
                orderId: 'order_12345',
                refundAmount: 1000.00,
                refundReason: 'Customer requested return',
                refundType: 'wallet',
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/refund/trigger')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(400);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('cannot exceed order total');
        });
    });

    describe('Performance Tests', () => {
        test('Loyalty balance response time', async () => {
            const startTime = Date.now();
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(validHeaders);
            const endTime = Date.now();

            expect(response.status).toBe(200);
            expect(endTime - startTime).toBeLessThan(2000);
        });

        test('Apply points response time', async () => {
            const startTime = Date.now();
            const response = await request(app)
                .post('/checkout/apply-points')
                .set(validHeaders)
                .send({
                    orderId: 'order_12345',
                    pointsToUse: 50,
                    orderTotal: 299.00,
                    currency: 'SAR',
                    region: 'KSA'
                });
            const endTime = Date.now();

            expect(response.status).toBe(200);
            expect(endTime - startTime).toBeLessThan(3000);
        });

        test('Use wallet response time', async () => {
            const startTime = Date.now();
            const response = await request(app)
                .post('/checkout/use-wallet')
                .set(validHeaders)
                .send({
                    orderId: 'order_12345',
                    walletAmount: 25.00,
                    orderTotal: 299.00,
                    currency: 'SAR',
                    region: 'KSA'
                });
            const endTime = Date.now();

            expect(response.status).toBe(200);
            expect(endTime - startTime).toBeLessThan(3000);
        });
    });

    describe('Data Validation Tests', () => {
        test('Loyalty balance data structure', async () => {
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.data).toHaveProperty('userId');
            expect(response.body.data).toHaveProperty('availablePoints');
            expect(response.body.data).toHaveProperty('totalEarned');
            expect(response.body.data).toHaveProperty('totalRedeemed');
            expect(response.body.data).toHaveProperty('currency');
            expect(response.body.data).toHaveProperty('region');
        });

        test('Transaction data structure', async () => {
            const response = await request(app)
                .get('/wallet/transactions')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.data).toHaveProperty('transactions');
            expect(response.body.data).toHaveProperty('pagination');
            expect(response.body.data.pagination).toHaveProperty('limit');
            expect(response.body.data.pagination).toHaveProperty('offset');
            expect(response.body.data.pagination).toHaveProperty('total');
        });
    });

    describe('Error Handling Tests', () => {
        test('Missing required fields', async () => {
            const response = await request(app)
                .post('/checkout/apply-points')
                .set(validHeaders)
                .send({});

            expect(response.status).toBe(400);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Missing required fields');
        });

        test('Invalid endpoint', async () => {
            const response = await request(app)
                .get('/invalid-endpoint')
                .set(validHeaders);

            expect(response.status).toBe(404);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Endpoint not found');
        });
    });

    describe('Health Check', () => {
        test('Server health check', async () => {
            const response = await request(app)
                .get('/health');

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.status).toBe('healthy');
        });
    });
});
