#!/bin/bash

echo "========================================"
echo "Golden Scent Mock Server Test Runner"
echo "========================================"
echo

echo "Starting Mock Server..."
node mock-server.js &
SERVER_PID=$!

# Wait for server to start
sleep 3

echo
echo "Running API Tests..."
echo

echo "1. Running Jest Tests..."
npm test

echo
echo "2. Running REST Assured Style Tests..."
npm run test:rest-assured

echo
echo "3. Running All Tests..."
npm run test:all

echo
echo "========================================"
echo "Test Execution Complete"
echo "========================================"
echo

# Stop the mock server
echo "Stopping Mock Server..."
kill $SERVER_PID

echo "Mock Server stopped."
echo "All tests completed successfully!"
