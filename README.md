# Proyecto Mediateca

## SISTEMA DE PRÉSTAMOS - MEDIATECA

### Proyecto Fase I - Desafío 2 POO941 G01T (Virtual)

### Integrantes

- Kevin Alexander Cardoza Márquez CM251645
- Francisco Adalberto De la O González DG200722
- Francisco Alexander Rivera Gómez RG253507
- Ricardo Balmore Aguilar Ventura AV253053
- Ricardo Daniel De León Cruz DC251463

### Catedrático

Rafael Alexander Torres Rodriguez

---

##  Descripción del Proyecto

Sistema de gestión de préstamos para una mediateca que permite:

- Gestión de usuarios con diferentes roles (ADMIN, EMPLEADO, PROFESOR, ALUMNO)
- Gestión de materiales (libros, revistas, CDs, DVDs)
- Préstamos y devoluciones con cálculo automático de mora
- Configuración dinámica de tipos de materiales y usuarios
- Configuración de mora por día y días de préstamo por tipo
- ** Reporte de disponibilidad de materiales** (NUEVO)

---

---

## 📋 Requisitos previos (instalar UNA SOLA VEZ)

Antes de ejecutar el proyecto, **debes tener instalado**:

| Requisito | Versión | Enlace de descarga |
|-----------|---------|---------------------|
| Java JDK | 17 o superior | [Adoptium](https://adoptium.net/) |
| MySQL Server | 8.0 o superior | [MySQL Community Server](https://dev.mysql.com/downloads/mysql/) |

###  Configuración de MySQL

La contraseña del usuario `root` de MySQL **debe ser `323564.f`** para que el script funcione automáticamente.

Si tu MySQL tiene otra contraseña, edita el archivo `ejecutar.bat` y cambia `-p323564.f` por `-pTU_CONTRASEÑA` en todas las líneas donde aparece.

---

##  Instalación y Ejecución

### Opción 1: Automática (Recomendada - Solo Windows)

1. **Descomprimir** el archivo ZIP del proyecto
2. **Hacer doble clic** en el archivo `ejecutar.bat`
3. El script verificará los requisitos, creará la base de datos e iniciará el programa automáticamente

### Opción 2: Manual

```bash
# 1. Crear la base de datos
mysql -u root -p323564.f -e "CREATE DATABASE IF NOT EXISTS mediateca;"

# 2. Importar los datos de prueba
mysql -u root -p323564.f mediateca < mediateca_con_pruebas.sql

# 3. Compilar y ejecutar
mvn clean compile exec:java -Dexec.mainClass="com.mediateca.vistas.Ventana_PPAL"