FROM eclipse-temurin:21-jre-jammy

RUN useradd -s /bin/bash user
USER user
COPY --chown=644 target/loan-transaction-api-*.jar /loan-transaction-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/loan-transaction-api.jar"]
