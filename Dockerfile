# Используем официальный образ OpenJDK 18
FROM openjdk:18-jdk-alpine

# Указываем путь к jar-файлу, который будет собран Maven-ом
ARG JAR_FILE=target/exchanger.jar

# Создаём рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный jar-файл внутрь контейнера
COPY ${JAR_FILE} app.jar

EXPOSE 8081

# Команда запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
