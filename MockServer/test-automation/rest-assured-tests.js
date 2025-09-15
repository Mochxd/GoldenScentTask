const request = require('supertest');
const app = require('../mock-server');

describe('Golden Scent API Tests - REST Assured Style', () => {
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

    describe('GET /user/loyalty-balance', () => {
        test('TC001: Get Loyalty Balance - Valid User', async () => {
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.userId).toBe('user_12345');
            expect(response.body.data.availablePoints).toBeGreaterThan(0);
            expect(response.body.data.currency).toBe('SAR');
            expect(response.body.data.region).toBe('KSA');
        });

        test('TC002: Get Loyalty Balance - Invalid Token', async () => {
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

        test('TC003: Get Loyalty Balance - Guest User', async () => {
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(guestHeaders);

            expect(response.status).toBe(403);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Guest users do not have access');
        });
    });

    describe('POST /checkout/apply-points', () => {
        test('TC004: Apply Points - Valid Request', async () => {
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

        test('TC005: Apply Points - Insufficient Points', async () => {
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

        test('TC006: Apply Points - Exceed Maximum Percentage', async () => {
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

        test('TC007: Apply Points - Invalid Order', async () => {
            const requestBody = {
                orderId: 'invalid_order_123',
                pointsToUse: 100,
                orderTotal: 299.00,
                currency: 'SAR',
                region: 'KSA'
            };

            const response = await request(app)
                .post('/checkout/apply-points')
                .set(validHeaders)
                .send(requestBody);

            expect(response.status).toBe(404);
            expect(response.body.success).toBe(false);
            expect(response.body.message).toContain('Order not found');
        });
    });

    describe('POST /checkout/use-wallet', () => {
        test('TC008: Use Wallet - Valid Request', async () => {
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

        test('TC009: Use Wallet - Insufficient Balance', async () => {
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

        test('TC010: Use Wallet - Below Minimum Threshold', async () => {
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

    describe('GET /wallet/transactions', () => {
        test('TC011: Get Transactions - Valid Request', async () => {
            const response = await request(app)
                .get('/wallet/transactions')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(Array.isArray(response.body.data.transactions)).toBe(true);
            expect(response.body.data.pagination.limit).toBe(10);
            expect(response.body.data.pagination.total).toBeGreaterThan(0);
        });

        test('TC012: Get Transactions - With Filters', async () => {
            const response = await request(app)
                .get('/wallet/transactions?type=refund&limit=5')
                .set(validHeaders);

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.pagination.limit).toBe(5);
        });
    });

    describe('POST /refund/trigger', () => {
        test('TC013: Trigger Refund - Valid Request', async () => {
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

        test('TC014: Trigger Refund - Exceed Order Amount', async () => {
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
        test('Response Time - Loyalty Balance', async () => {
            const startTime = Date.now();
            const response = await request(app)
                .get('/user/loyalty-balance')
                .set(validHeaders);
            const endTime = Date.now();

            expect(response.status).toBe(200);
            expect(endTime - startTime).toBeLessThan(2000); // 2 seconds
        });

        test('Response Time - Apply Points', async () => {
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
            expect(endTime - startTime).toBeLessThan(3000); // 3 seconds
        });
    });

    describe('Health Check', () => {
        test('Server Health Check', async () => {
            const response = await request(app)
                .get('/health');

            expect(response.status).toBe(200);
            expect(response.body.success).toBe(true);
            expect(response.body.data.status).toBe('healthy');
        });
    });
});
