# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen oficial de Maven con la versión de Java 17 (definida en tu pom.xml)
FROM maven:3.9-eclipse-temurin-17 AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo los archivos de build y el wrapper de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# --- ¡ESTA ES LA CORRECCIÓN! ---
# Damos permisos de ejecución al script 'mvnw' antes de usarlo.
RUN chmod +x ./mvnw

# Descargamos todas las dependencias. Ahora este comando funcionará.
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos la aplicación y creamos el .jar
RUN ./mvnw package -DskipTests

# --- ETAPA 2: EJECUCIÓN (RUNNER) ---
# Usamos una imagen de Java Runtime (JRE) ligera, solo para ejecutar.
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copiamos el .jar construido de la etapa 'builder' a esta nueva imagen
COPY --from=builder /app/target/tienda-0.0.1-SNAPSHOT.jar ./app.jar

# Exponemos el puerto 8080 (el puerto por defecto de Spring Boot)
EXPOSE 8080

# El comando para iniciar la aplicación cuando el contenedor arranque
ENTRYPOINT ["java", "-jar", "app.jar"]