@echo off
echo Testing APIAutomationTask module independently...
echo.

cd APIAutomationTask

echo 1. Checking Maven can find the module...
call mvn validate

echo.
echo 2. Compiling the module...
call mvn compile

echo.
echo 3. Running tests...
call mvn test

echo.
echo 4. Creating JAR...
call mvn package

echo.
echo APIAutomationTask module test completed!
cd ..
pause
