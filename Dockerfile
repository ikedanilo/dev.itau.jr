# Etapa 1: Build da aplicação usando Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia tudo para dentro da imagem de build
COPY . .

# Faz o build do projeto
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final apenas com o jar e o JDK (mais leve)
FROM openjdk:21-jdk-slim

# Cria e define a pasta de trabalho dentro da imagem
WORKDIR /app

# Copia o jar para dentro da pasta /app
COPY --from=build /app/target/*.jar app.jar
# ARG JAR_FILE=target/dev.itau.jr-0.0.1-SNAPSHOT.jar
# COPY ${JAR_FILE} app.jar

# EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
