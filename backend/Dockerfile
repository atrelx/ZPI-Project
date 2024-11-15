FROM maven AS build

COPY /amoz/target/amoz-0.0.1-SNAPSHOT.jar /app/amoz.jar

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/amoz.jar /app/amoz.jar

EXPOSE 8080

CMD ["java", "-jar", "amoz.jar"]