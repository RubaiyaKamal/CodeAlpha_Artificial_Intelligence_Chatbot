@echo off
:: Always run from the folder that contains this batch file
cd /d "%~dp0"

echo =========================================
echo   Java AI Chatbot - Build and Run
echo =========================================
echo Working directory: %CD%
echo.

:: ─── Maven path ───────────────────────────────────────────────────────────────
where mvn >nul 2>nul
if %errorlevel% == 0 (
    echo [Maven found] Building with Maven...
    mvn clean package -q
    if %errorlevel% neq 0 (
        echo BUILD FAILED. Check pom.xml and source files.
        pause
        exit /b 1
    )
    echo Build OK. Starting JavaBot...
    echo  - Desktop GUI opens automatically
    echo  - Web UI: check console output for the URL
    echo.
    java -jar target\ai-chatbot.jar
    pause
    exit /b 0
)

:: ─── javac path ───────────────────────────────────────────────────────────────
echo [Maven not found] Compiling with javac...
echo.

if not exist "out\com\chatbot\model"  mkdir "out\com\chatbot\model"
if not exist "out\com\chatbot\gui"    mkdir "out\com\chatbot\gui"
if not exist "out\com\chatbot\server" mkdir "out\com\chatbot\server"
if not exist "out\web"                mkdir "out\web"

:: Copy web resources
echo Copying web resources...
xcopy /q /y "src\main\resources\web\*" "out\web\" >nul 2>nul
if %errorlevel% neq 0 (
    echo WARNING: Could not copy web resources. Web UI may not work.
)

:: Compile
echo Compiling Java sources...
javac -encoding UTF-8 -d out ^
    src\main\java\com\chatbot\model\Intent.java ^
    src\main\java\com\chatbot\NLPProcessor.java ^
    src\main\java\com\chatbot\TFIDFVectorizer.java ^
    src\main\java\com\chatbot\IntentClassifier.java ^
    src\main\java\com\chatbot\TrainingData.java ^
    src\main\java\com\chatbot\ChatEngine.java ^
    src\main\java\com\chatbot\server\ChatApiHandler.java ^
    src\main\java\com\chatbot\server\StaticFileHandler.java ^
    src\main\java\com\chatbot\server\ChatServer.java ^
    src\main\java\com\chatbot\gui\ChatWindow.java ^
    src\main\java\com\chatbot\Main.java

if %errorlevel% neq 0 (
    echo.
    echo COMPILATION FAILED. Make sure JDK 11 or newer is installed.
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
)

echo Compilation OK.
echo.
echo Starting JavaBot...
echo  - Desktop GUI will open automatically
echo  - The console will show the web UI URL (http://localhost:PORT)
echo  - If the browser does not open, paste that URL into your browser
echo.

java -cp out com.chatbot.Main

pause
