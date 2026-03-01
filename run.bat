@echo off
echo ============================================
echo    FitForge - Gym Portal Management System
echo ============================================
echo.
echo Checking Java version...
java -version
echo.
echo Starting Spring Boot application...
echo App will be available at: http://localhost:8080
echo.

cd /d "%~dp0"

REM Try Maven wrapper first, then system Maven
if exist "mvnw.cmd" (
    call mvnw.cmd spring-boot:run
) else (
    REM Download Maven if not found — use PowerShell
    powershell -Command "& {
        $mvnUrl = 'https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip'
        $zipPath = [System.IO.Path]::Combine($env:TEMP, 'maven.zip')
        $mvnPath = [System.IO.Path]::Combine($env:USERPROFILE, '.m2', 'maven-dist')
        if (-not (Test-Path $mvnPath)) {
            Write-Host 'Downloading Maven 3.9.6...'
            Invoke-WebRequest -Uri $mvnUrl -OutFile $zipPath
            Expand-Archive -Path $zipPath -DestinationPath $mvnPath -Force
            Write-Host 'Maven downloaded!'
        }
        $mvnExe = Get-ChildItem -Path $mvnPath -Filter 'mvn.cmd' -Recurse | Select-Object -First 1
        Write-Host 'Running: ' $mvnExe.FullName
        & $mvnExe.FullName 'spring-boot:run'
    }"
)

pause
