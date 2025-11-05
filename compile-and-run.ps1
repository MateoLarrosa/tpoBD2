# Script para compilar y ejecutar sin Maven

Write-Host "=== Compilando proyecto ===" -ForegroundColor Cyan

# Crear directorios
New-Item -ItemType Directory -Force -Path "target\classes" | Out-Null

# Encontrar todos los archivos Java
$javaFiles = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

# Verificar si hay dependencias
if (Test-Path "target\dependency\*.jar") {
    $classpath = "target\classes;target\dependency\*"
} else {
    Write-Host "ADVERTENCIA: No se encontraron dependencias. Ejecuta Maven primero para descargarlas." -ForegroundColor Yellow
    Write-Host "Necesitas ejecutar: mvn dependency:copy-dependencies" -ForegroundColor Yellow
    exit 1
}

# Compilar
Write-Host "Compilando archivos Java..." -ForegroundColor Green
$compileOutput = javac -encoding UTF-8 -cp $classpath -d "target\classes" $javaFiles 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilacion exitosa" -ForegroundColor Green
    
    # Copiar recursos
    if (Test-Path "src\main\resources") {
        Copy-Item -Path "src\main\resources\*" -Destination "target\classes\" -Recurse -Force
        Write-Host "Recursos copiados" -ForegroundColor Green
    }
    
    Write-Host ""
    Write-Host "=== Ejecutando aplicacion ===" -ForegroundColor Cyan
    java -cp "target\classes;target\dependency\*" main.Main
} else {
    Write-Host "Error en la compilacion" -ForegroundColor Red
    Write-Host $compileOutput
    exit 1
}
