FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

# LibreOffice para testes locais
RUN apt-get update && \
    apt-get install -y libreoffice --no-install-recommends && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
