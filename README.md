# Tarea: Alquiler de vehículos

## Profesor: José Ramón Jiménez Reyes

## Alumno: Oussama Atifi

El cliente nos acaba de dar unos nuevos requisitos a aplicar sobre la última versión que le mostramos y que le gustó bastante. Lo que nos pide el cliente es lo siguiente:

1. Que la aplicación no almacene los datos en ficheros y que lo haga en una base de datos creada para tal efecto.
2. La base de datos debe estar implementada en MySql.

**NOTA**. Los datos de acceso al servidor con la base de datos Mysql, serán facilitados por el profesor.

Tu tarea consiste en dotar a la aplicación de la tarea anterior de un nuevo modelo de datos que en vez de utilizar ficheros XML para almacenar los datos, lo haga haciendo uso de una Base de Datos (BBDD) Relacional SQL. Se pide al menos:

1. Acomodar el proyecto para que Gradle gestione las dependencias con el driver MySql para java en su última vesión.
2. Modificar el proyecto para que se puedan ejecutar todas las posibles combinaciones de almacenamiento e interfaz gráfica:
   - Memoria con IU textual
   - Memoria con IU gráfica
   - Ficheros con IU textual
   - Ficheros con IU gráfica
   - BBDD con IU textual.
   - BBDD con IU gráfica.
3. Gestionar los vehículos para que su persistencia se lleve a cabo por medio de BBDD.
4. Gestionar los clientes para que su persistencia se lleve a cabo por medio de BBDD.
5. Gestiona los alquileres para que su persistencia se lleve a cabo por medio de BBDD.

Para ello debes realizar las siguientes acciones:

1. Lo primero que debes hacer es crear un repositorio en GitHub a partir de tu repositorio de la tarea anterior.
2. Clona dicho repositorio localmente para empezar a modificarlo. Modifica el fichero **README.md** para que indique tus datos y los de esta tarea. Realiza tu primer commit.
3. Realiza los cambios necesarios para que el proyecto pueda contener seis aplicaciones diferentes (Haz un commit):
   - Memoria con IU textual
   - Memoria con IU gráfica
   - Ficheros XML con IU textual
   - Ficheros XML con IU gráfica
   - BBDD con IU textual
   - BBDD con IU gráfica.
   - Haz que la gestión de vehículos utilice la persistencia en BBDD. Haz un commit.
   - Haz que la gestión de clientes utilice la persistencia en BBDD. Haz un commit.
   - Haz que la gestión de alquileres utilice la persistencia en BBDD. Haz un commit.
