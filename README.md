# Información del proyecto NMDA Admisión

___________________
### Actualizar versión 
Para liberar una versión en el ambiente de desarrollo es importante actualizar versión de la aplicación ajustando el archivo en pom.xml ajustando la version 1.0.0 de acuerdo con el [versionamiento semantico](https://ernestoaguaysol.github.io/versionamiento-semantico/)  que corresponda con la aplicación.

```
    <project>
        ...
        <version>1.0.0</version>
        ...
    </project>

```
____________________
### Microservicio nuevo módulo de Admisión.

A continuación se indican los pasos para verificación de servicio en ambiente local usando Docker.

###Compilar y generar package

``$ mvn clean package``

###Generación contenedor Docker

``$ docker build --no-cache --build-arg JAR_FILE=./target/corona-financiero-nmda-admision-1.0.jar -t admision:latest .``


###Ejecutar Docker

``$ docker run -p 8080:8080 admision``

###Comprobación Local

``http://localhost:8080/admision/api/swagger-ui.html``

____________________________________________________________________
____________________________________________________________________

### Migración y despliegue

Para realizar la migración de los cambios entre los diferentes ambientes es necesario que el código se encuentre bajo los lineamientos definidos en el [documento](https://proyecto-personal.atlassian.net/wiki/spaces/ARQUITECTU/pages/216334360/Gu+a+y+lineamientos+DevOps#Lineamientos-An%C3%A1lisis-de-c%C3%B3digo), se requiere realizar la creación del Pullrequest hacia el ambiente requerido asociado a la [rama](https://proyecto-personal.atlassian.net/wiki/spaces/ARQUITECTU/pages/216334360/Gu+a+y+lineamientos+DevOps#Flujo-Despliegues) develop, release o master, esta migración se realizará con los roles requeridos
### Stack tecnológico
Para montar los entornos en local se han definido las siguientes [herramientas ](https://proyecto-personal.atlassian.net/l/cp/fT2HGXi0)

### Resultados Sonarqube

Actualmente se está trabajando bajo una arquitectura privada, lo cual implica que el servidor de sonarqube tenga asignado una ip Privada que es la siguiente http://10.10.0.135:9000/
para consultar los resultados se requieren los siguientes datos

### url: http://20.124.148.110:50005/projects
### usr: user_financiero
### pass: Financiero#2022
### nombre del proyecto: cor-fin-nmda-admision-api