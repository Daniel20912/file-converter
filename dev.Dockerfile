FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

RUN export DEBIAN_FRONTEND=noninteractive \
 && echo "deb http://archive.ubuntu.com/ubuntu/ jammy main restricted universe" > /etc/apt/sources.list \
 && echo "deb http://archive.ubuntu.com/ubuntu/ jammy-updates main restricted universe" >> /etc/apt/sources.list \
 && echo "deb http://security.ubuntu.com/ubuntu/ jammy-security main restricted universe" >> /etc/apt/sources.list \
 && apt-get update \
 && apt-get install -y --no-install-recommends \
    libreoffice \
    libreoffice-java-common \
    fontconfig \
    fonts-liberation \
    fonts-croscore \
    fonts-crosextra-carlito \
    fonts-crosextra-caladea \
 && fc-cache -f -v \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*
