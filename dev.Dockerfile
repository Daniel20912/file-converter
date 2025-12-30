FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

# LibreOffice para testes locais
RUN apt-get update && apt-get install -y \
    libreoffice \
    fontconfig \
    fonts-crosextra-carlito \
    fonts-crosextra-caladea \
    fonts-liberation \
    --no-install-recommends \
 && fc-cache -f -v \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*
