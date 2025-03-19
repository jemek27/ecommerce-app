# Używamy obrazu OpenJDK jako bazy
FROM openjdk:17-jdk-slim

# Ustawiamy katalog roboczy
WORKDIR /app
ARG JAR_FILE=target/*.jar
# Kopiujemy plik JAR do kontenera
COPY ${JAR_FILE} app.jar

# Otwieramy port, na którym aplikacja będzie działać
EXPOSE 8081

# Uruchamiamy aplikację Spring Boot
CMD ["java", "-jar", "app.jar"]
