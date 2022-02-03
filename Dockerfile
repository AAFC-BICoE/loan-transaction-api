FROM openjdk:11-jre-slim

RUN useradd -s /bin/bash user
USER user
COPY --chown=644 target/loan-transaction-api-*.jar /loan-transaction-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/loan-transaction-api.jar"]

