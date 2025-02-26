@echo off
setlocal

rem Maven Wrapper for Windows

set MAVEN_HOME=%~dp0\.mvn\wrapper
set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m

if not exist "%MAVEN_HOME%\mvnw.cmd" (
    echo "Maven Wrapper not found. Please run mvnw from the root directory."
    exit /b 1
)

call "%MAVEN_HOME%\mvnw" %* 

endlocal