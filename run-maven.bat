@echo off
echo Running Maven commands for Golden Scent API Automation...
echo.

echo Choose an option:
echo 1. Run from root (multi-module)
echo 2. Run APIAutomationTask only
echo 3. Install parent POM first, then run
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" goto :root
if "%choice%"=="2" goto :api_only
if "%choice%"=="3" goto :install_parent
goto :end

:root
echo.
echo Running from root directory...
call mvn clean compile test package
goto :end

:api_only
echo.
echo Running APIAutomationTask only...
cd APIAutomationTask
call mvn clean compile test package
cd ..
goto :end

:install_parent
echo.
echo Installing parent POM first...
call mvn clean install -N
echo.
echo Now running full build...
call mvn clean compile test package
goto :end

:end
echo.
echo Maven execution completed!
pause
