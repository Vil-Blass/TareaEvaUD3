# Tarea evaluativa AD03: JDBC

Solución de referencia para los apartados 1–5. Los ejercicios 1–4 están en el proyecto Maven raíz; el ejercicio 5 es un proyecto Maven independiente en `ejercicio5-hsqldb-vibur`.

## Requisitos

- JDK 17 o superior.
- Maven.
- MySQL 8.x para los ejercicios 1–4.
- Docker Desktop con Docker Compose para la opción Docker.

## Preparar MySQL

1. Ejecuta `sql/dbformacion.sql` desde MySQL Workbench o el cliente MySQL. El script crea `dbformacion`, sus tablas, datos de prueba y la función `obtener_numero_matriculados`. El curso 2 está completo y el curso 6 permanece sin matrículas.
2. Define estas variables de entorno con un usuario local de MySQL; no guardes contraseñas en el repositorio:

   ```powershell
   $env:AD03_DB_USER = "tu_usuario"
   $env:AD03_DB_PASSWORD = "tu_contraseña"
   # Opcional:
   $env:AD03_DB_URL = "jdbc:mysql://localhost:3306/dbformacion?serverTimezone=UTC&useSSL=false"
   ```

   El programa usa `localhost:3306/dbformacion` si no se define `AD03_DB_URL`.

## Ejecutar los ejercicios 1–4 con Docker

Desde la carpeta raíz, Docker Compose inicia MySQL 8.4 y ejecuta el ejercicio 1. No hace falta instalar Java, Maven ni MySQL en el equipo:
Esta configuración cubre los ejercicios 1–4; el ejercicio 5 usa HSQLDB y Vibur en su proyecto independiente.

```powershell
docker compose run --build --rm app
```

La base de datos queda disponible en Docker Desktop y también se publica solo en el equipo local, en `127.0.0.1:3307`. Para conectar MySQL Workbench, usa ese host y puerto, la base `dbformacion` y las credenciales `ad03` / `ad03-local` (valores por defecto).

Para probar otro ejercicio, cambia la clase principal antes de ejecutar el mismo comando:

```powershell
$env:AD03_MAIN_CLASS = "es.ejemplo.ad03.ejercicio2.ModificarAlumno"
docker compose run --build --rm app

$env:AD03_MAIN_CLASS = "es.ejemplo.ad03.ejercicio3.MatricularAlumno"
docker compose run --build --rm app

$env:AD03_MAIN_CLASS = "es.ejemplo.ad03.ejercicio4.ConsultarMatriculados"
docker compose run --build --rm app
```

Los ejercicios interactivos reciben los datos desde la terminal. Para cambiar las credenciales o el puerto, define `AD03_DB_USER`, `AD03_DB_PASSWORD`, `MYSQL_ROOT_PASSWORD` o `MYSQL_PORT` en el entorno antes de ejecutar Compose, o en un archivo `.env` local (no se sube a Git).

La base de datos persiste en el volumen `db-data`; detener los contenedores no borra los datos. Para detener MySQL usa `docker compose down`. Para borrar también la base de datos y volver a inicializarla con los datos del script, usa `docker compose down -v` (elimina permanentemente los datos de ese volumen).

## Ejecutar los ejercicios 1–4

Desde la carpeta raíz, usa un comando por programa:

```powershell
mvn compile exec:java "-Dexec.mainClass=es.ejemplo.ad03.ejercicio1.ListadoCursos"
mvn compile exec:java "-Dexec.mainClass=es.ejemplo.ad03.ejercicio2.ModificarAlumno"
mvn compile exec:java "-Dexec.mainClass=es.ejemplo.ad03.ejercicio3.MatricularAlumno"
mvn compile exec:java "-Dexec.mainClass=es.ejemplo.ad03.ejercicio4.ConsultarMatriculados"
```

El ejercicio 3 usa una transacción sobre una sola conexión. Bloquea la fila del curso mientras comprueba plazas, y confirma conjuntamente el alta del alumno nuevo y su matrícula. Los rechazos y errores de SQL provocan `rollback`.

## Ejecutar el ejercicio 5

```powershell
cd ejercicio5-hsqldb-vibur
mvn compile exec:java "-Dexec.mainClass=es.ejemplo.ad03.ejercicio5.ComprobarPool"
```

Es un proyecto separado y usa estas coordenadas Maven:

- `org.hsqldb:hsqldb:2.7.4`
- `org.vibur:vibur-dbcp:26.0`
- `org.slf4j:slf4j-simple:2.0.17` como proveedor de logging para evitar el aviso de SLF4J sin proveedor.

El archivo de base de datos queda en `ejercicio5-hsqldb-vibur/data/` y no se incluye en Git. La tabla se conserva entre ejecuciones; por tanto, el programa añade una fila de prueba en cada ejecución.

## Autoevaluación y evidencias

Completa `AUTOEVALUACION.md` después de ejecutar cada programa. Añade capturas propias o un enlace a un vídeo que pruebe los casos satisfactorios y los casos límite indicados. No se incluyen evidencias simuladas: deben obtenerse en tu equipo con la base de datos funcionando.
