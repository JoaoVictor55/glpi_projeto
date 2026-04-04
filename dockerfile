#Estágio 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para baixar as dependências (otimiza cache)
COPY glpi_worker/pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o .jar
COPY glpi_worker/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia apenas o arquivo .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

#porta do container (não é necessário nessa aplicação)
#EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]