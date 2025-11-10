
#!/bin/bash

# Exportar variables del .env
export $(grep -v '^#' .env | xargs)

# Cambiar al directorio del script
cd "$(dirname $(realpath $0))"

# Detectar el directorio base del proyecto
BASE_DIR=$(pwd)
TARGET_DIR="$BASE_DIR/target/classes"
DEPENDENCIES_DIR="$BASE_DIR/target/dependency"

# Asegurarse de que las dependencias estén descargadas
mvn dependency:copy-dependencies -DoutputDirectory=$DEPENDENCIES_DIR

# Compilar los archivos Java
mvn compile

# Ejecutar el archivo Main.class con todas las dependencias y suprimir logs de drivers
JAVA_OPTS="-Dorg.mongodb.driver.level=ERROR -Dcom.datastax.oss.driver.level=ERROR"
java $JAVA_OPTS -cp "$TARGET_DIR:$DEPENDENCIES_DIR/*" main.Main