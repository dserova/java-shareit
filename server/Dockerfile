FROM maven:slim as builder
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package
#RUN mvn clean package -DskipTests

FROM amazoncorretto:11-alpine-jdk
WORKDIR /app
RUN apk add --no-cache curl dumb-init  \
    && addgroup --system javauser  \
    && adduser -S -s /bin/false -G javauser javauser  \
    && chown -R javauser:javauser /app
COPY --from=builder /usr/src/app/target/shareit-server-0.0.1-SNAPSHOT.jar ./shareit-server.jar
USER javauser
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["dumb-init", "java", "-jar", "./shareit-server.jar"]