$url = "https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"
$mvnDir = Join-Path $env:USERPROFILE ".m2\maven-dist"
$zipPath = Join-Path $env:TEMP "apache-maven-3.9.6.zip"
$mvnBin = Join-Path $mvnDir "apache-maven-3.9.6\bin\mvn.cmd"

if (-not (Test-Path $mvnBin)) {
    Write-Host "Downloading Maven 3.9.6 (this may take a minute)..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $url -OutFile $zipPath -UseBasicParsing
    Write-Host "Extracting Maven..." -ForegroundColor Yellow
    Expand-Archive -Path $zipPath -DestinationPath $mvnDir -Force
    Write-Host "Maven installed!" -ForegroundColor Green
} else {
    Write-Host "Maven already present at: $mvnBin" -ForegroundColor Green
}

Write-Host "Building and starting FitForge Gym Portal..." -ForegroundColor Cyan
Write-Host "App will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host ""

& $mvnBin "spring-boot:run"
