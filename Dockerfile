## Build
FROM maven:3.9.0-eclipse-temurin-19-alpine as Build
WORKDIR /app

COPY . /app
# Install dependencies, compile, create jar...
RUN mvn clean package

## Run
FROM eclipse-temurin:19-alpine
WORKDIR /app

COPY --from=Build /app/target/theshop*-with-dependencies.jar /app/app.jar
COPY /public/static /app/public/static
ENTRYPOINT ["java", "-jar", "app.jar"]
# CMD ["tail", "-f", "/dev/null"] # debug