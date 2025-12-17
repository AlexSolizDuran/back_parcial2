# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen de Maven con JDK 17 para compilar el proyecto
FROM maven:3.9-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el archivo pom.xml y descargamos las dependencias
# Esto aprovecha la caché de Docker para no descargar todo de nuevo si solo cambia el código
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente del proyecto
COPY src ./src

# Compilamos el proyecto y generamos el .jar (saltando los tests para agilizar el build en producción)
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen ligera de Java 17 (JRE) para correr la aplicación
FROM eclipse-temurin:17-jre-alpine

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo .jar generado en la etapa anterior
# El nombre 'tienda-0.0.1-SNAPSHOT.jar' se basa en tu pom.xml, pero usamos el comodín *.jar para ser más flexibles
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080 (puerto por defecto de Spring Boot)
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]