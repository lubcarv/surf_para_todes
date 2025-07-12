# Usa Java 21 (em vez de 17)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia apenas arquivos necessários para cache de dependências
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Faz o download das dependências (melhora performance do build)
RUN ./mvnw dependency:go-offline

# Copia o restante do código
COPY src src

# Build do projeto (sem rodar testes)
RUN ./mvnw clean package -DskipTests

# Ajuste o nome do JAR gerado no target/
CMD ["java", "-jar", "target/surfparatodes-0.0.1-SNAPSHOT.jar"]
