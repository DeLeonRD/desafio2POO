"Proyecto Mediateca" 
# SISTEMA - MEDIATECA

## Proyecto Fase I - Desafío 2 POO941 G01T (Virtual)



Integrantes 

- Kevin Alexander Cardoza Márquez — CM251645
- Francisco Adalberto De la O González — DG200722
- Francisco Alexander Rivera Gómez — RG253507
- Ricardo Balmore Aguilar Ventura — AV253053
- Ricardo Daniel De León Cruz — DC251463



    Ingeniero 
Rafael Alexander Torres Rodriguez



    Descripcion dek proyecto 


Sistema de gestión de mediateca que permite:

- Gestión de usuarios con diferentes roles
- Gestión de materiales
- Registro de préstamos y devoluciones
- Cálculo automático de mora
- Configuración dinámica de tipos de materiales
- Control de préstamos y disponibilidad

El sistema fue desarrollado utilizando Programación Orientada a Objetos, JDBC, Swing y MySQL.



    Tecnoologias Usadas en el proyecto


 Tecnología | Versión | Uso |
------------|---------|-----|
 Java | 17 | Lenguaje principal |
 Maven | 3.9.x | Gestión de dependencias |
 MySQL | 8.0 | Base de datos |
 Git | - | Control de versiones |
 Swing | Java 17 | Interfaz gráfica |
 JDBC | - | Conexión con MySQL |
 Log4j2 | 2.x | Manejo de logs |



    Arquitectura 

El proyecto se encuentra organizado en módulos:

src/main/java/com/mediateca

    Estructura 


- model: Clases del sistema
- dao: Acceso a base de datos
- control: Lógica del sistema
- vista: Interfaces gráficas
- util: Validaciones y utilidades
- resources: Configuración del sistema


    Funcionalidades 


    Backend + Base de datos 


- DatabaseConnection
- CRUD de materiales
- Gestión de préstamos
- Gestión de devoluciones
- Validación de mora
- Control de límite de préstamos
- Roles de usuario
- Login


    Modelo POO
- Herencia
- Clases abstractas
- Reutilización de código


    Interfaz 
    
- JFrame principal
 JMenuBar
- Formularios Swing
- JTable para listados
- Navegación con CardLayout


    logs

Se implementó Log4j2 para registrar:

- Eventos importantes
- Errores del sistema
- Acciones realizadas


# Dependencias (pom.xml)

```xml
<dependencies>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.1.0</version>
    </dependency>

    <!-- BCrypt para contraseñas -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>

    <!-- Log4j2 -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.20.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.20.0</version>
    </dependency>

</dependencies>


Intalacion:

Requisitos

JDK 17+
Maven
MySQL
NetBeans o VS Code



clonar repositorio
git clone https://github.com/DeLeonRD/desafio2POO.git


clonar base de datos
Mediateca G1.sql

para conexion:
src/main/resources/config.properties
----------------------------------------------
db.url=jdbc:mysql://localhost:3306/mediateca
db.user=root
db.password=1234
----------------------------------------------


Ejecutar proyecto:
VentanaPrincipal.java


El proyecto fue desarrollado utilizando Git y GitHub mediante ramas:

backend
modelado
formularios
ui-principal
integracion

Luego se realizó integración y merge hacia main.

