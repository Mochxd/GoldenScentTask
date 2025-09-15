@echo off
echo ========================================
echo Golden Scent Mock Server Test Runner
echo ========================================
echo.

echo Starting Mock Server...
start /B node mock-server.js
timeout /t 3 /nobreak > nul

echo.
echo Running API Tests...
echo.

echo 1. Running Jest Tests...
npm test

echo.
echo 2. Running REST Assured Style Tests...
npm run test:rest-assured

echo.
echo 3. Running All Tests...
npm run test:all

echo.
echo ========================================
echo Test Execution Complete
echo ========================================
echo.
echo To stop the mock server, press Ctrl+C
echo.

pause
