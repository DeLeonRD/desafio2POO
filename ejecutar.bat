@echo off
title MEDIATECA DON BOSCO - SISTEMA DE PRÉSTAMOS
color 0A

echo ===============================================
echo    MEDIATECA DON BOSCO - SISTEMA DE PRÉSTAMOS
echo ===============================================
echo.

echo Verificando requisitos...
echo.

REM Verificar Java
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java no encontrado. Instale Java 17 o superior.
    echo Descargar de: https://adoptium.net/
    pause
    exit /b 1
)
echo [OK] Java encontrado

REM Verificar MySQL
echo Verificando conexion a MySQL...
mysql -u root -p323564.f -e "SELECT 1" >nul 2>&1
if errorlevel 1 (
    echo.
    echo [ADVERTENCIA] No se pudo conectar a MySQL.
    echo Asegurese de que MySQL este corriendo y la contrasena sea '323564.f'
    echo.
    echo Presione cualquier tecla para continuar de todas formas...
    pause >nul
)

echo.
echo Creando base de datos...
mysql -u root -p323564.f -e "CREATE DATABASE IF NOT EXISTS mediateca;" 2>nul

echo Importando datos...
mysql -u root -p323564.f mediateca < mediateca_con_pruebas.sql 2>nul

echo.
echo Ejecutando el proyecto...
echo.

REM Ejecutar el JAR FAT (ubicado en la raiz)
java -jar ProyectoJava-1.0-SNAPSHOT-jar-with-dependencies.jar

echo.
echo Programa finalizado.
pause