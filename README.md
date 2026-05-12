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
- ** Reporte de disponibilidad de materiales** 
---


##  Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje principal |
| Maven | 3.9.x | Gestión de dependencias |
| MySQL | 8.0 | Base de datos |
| Git | - | Control de versiones |
| Swing | - | Interfaz gráfica |

---

##  Dependencias (pom.xml)

```xml
<dependencies>
    <!-- MySQL Connector -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.1.0</version>
    </dependency>
    
    <!-- BCrypt para encriptación de contraseñas -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
</dependencies>