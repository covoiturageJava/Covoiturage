FROM openjdk:23-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y libgl1-mesa-glx libgtk-3-0 wget unzip xvfb x11vnc fluxbox

RUN wget https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_linux-x64_bin-sdk.zip && \
    unzip openjfx-23.0.1_linux-x64_bin-sdk.zip -d /opt/javafx && \
    rm openjfx-23.0.1_linux-x64_bin-sdk.zip

COPY target/carpooling-shaded.jar /app/app.jar

COPY .env /app/.env

ENV DISPLAY=:99

EXPOSE 5900 12345 8080 8081

CMD ["sh", "-c", "Xvfb :99 -screen 0 1280x720x24 & fluxbox & x11vnc -display :99 -nopw -forever & java --module-path /opt/javafx/javafx-sdk-23.0.1/lib:/app/lib --add-modules javafx.controls,javafx.fxml,javafx.web,java.sql,java.net.http --add-opens javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED -cp /app/app.jar com.example.carpoolingapp.Main"]
