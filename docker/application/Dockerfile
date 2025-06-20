FROM bellsoft/liberica-openjdk-alpine:21.0.4 AS builder
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM bellsoft/liberica-openjdk-alpine:21.0.4
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT ["java", "--enable-preview", "org.springframework.boot.loader.JarLauncher"]