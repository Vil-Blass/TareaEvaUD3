# Autoevaluación AD03

Completa esta autoevaluación después de ejecutar los programas. Sustituye los campos entre corchetes por resultados y evidencias reales; no afirmes pruebas que no hayas realizado.

| Apartado | Máximo | Puntuación propuesta | Evidencia propia |
|---|---:|---:|---|
| 1. Listado de cursos y ocupación | 1,5 | [ ] | [Captura/vídeo: cursos ordenados y curso sin matrículas] |
| 2. Modificar alumno | 1,5 | [ ] | [Captura/vídeo: DNI inexistente, cancelación en blanco y cambio] |
| 3. Matrícula con transacción | 3,0 | [ ] | [Captura/vídeo: alumno existente/nuevo, plazas, duplicado y rechazo] |
| 4. Función almacenada | 1,5 | [ ] | [Captura/vídeo: llamada CallableStatement y valor devuelto] |
| 5. HSQLDB y pool Vibur | 1,5 | [ ] | [Captura/vídeo: conexión válida, creación/consulta de fila y cierre] |
| 6. Proceso, autoevaluación e IA | 1,0 | [ ] | [Captura/vídeo o enlace de entrega] |
| **Total** | **10,0** | **[ ]** | |

## Uso de IA y proceso

En esta solución sí se ha utilizado IA como apoyo. Esta interacción es representativa:

- Petición: «crea todo lo necesario» a partir del enunciado AD03.
- Uso: generar una estructura Maven y código inicial para JDBC/MySQL, la transacción de matrícula, la función almacenada, HSQLDB/Vibur y documentación.
- Cambios y revisión: se prepararon configuraciones por variables de entorno, se revisaron los casos de cancelación/rollback y se reforzó el control concurrente de plazas con lecturas bloqueantes. No se inventaron capturas ni resultados de ejecución.

Antes de entregar, añade una explicación personal sobre lo que hayas comprendido, modificado o descartado, y documenta las pruebas reales que ejecutes:

- Cómo explicarías el código y la transacción: [ ]
- Qué modificaste después de probarlo: [ ]
- Pruebas realizadas, comandos y resultados: [ ]
- Capturas propias o vídeo: [ ]

La compilación y ejecución todavía deben comprobarse en el equipo con JDK y Maven instalados y MySQL preparado. No asignes puntuaciones hasta obtener evidencias reales.

## Coordenadas Maven del apartado 5

- HSQLDB: `org.hsqldb:hsqldb:2.7.4`
- Vibur DBCP: `org.vibur:vibur-dbcp:26.0`
- Proveedor SLF4J incluido: `org.slf4j:slf4j-simple:2.0.17`
