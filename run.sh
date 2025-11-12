#!/bin/bash

echo "Compilando el proyecto..."

# Limpiar y compilar el proyecto
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Error durante la compilación."
    exit 1
fi

echo "Ejecutando la aplicación..."

# Ejecutar el archivo Main.class con todas las dependencias
# Usar ; como separador en Windows
java -Dorg.mongodb.driver.level=ERROR \
     -Dcom.datastax.oss.driver.level=ERROR \
     -cp "target/classes;target/dependency/*" \
     main.Main
