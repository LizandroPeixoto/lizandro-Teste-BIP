@echo off
set JAVA_HOME=C:\Users\lvieirpe\scoop\apps\temurin17-jdk\17.0.18-8
set PATH=%JAVA_HOME%\bin;C:\Users\lvieirpe\scoop\apps\maven\current\bin;%PATH%
cd /d "c:\Users\lvieirpe\OneDrive - NTT DATA EMEAL\pessoal\lizandro-Teste-BIP-main\backend-module\src\main\java\com\example\backend"
mvn spring-boot:run
