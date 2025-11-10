@echo off
echo Compilando el proyecto...
call mvn clean package
if errorlevel 1 (
    echo Error durante la compilacion
    pause
    exit /b 1
)
echo Ejecutando la aplicacion...
java -jar target/CatalogoProductos-0.0.1-SNAPSHOT.jar