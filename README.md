#  AsisTech - Sistema de Gestión de Citas de Atención Técnica

Aplicación de escritorio desarrollada en **JavaFX 21** y **PostgreSQL** mediante **JDBC puro (sin ORM)** para la administración, programación y seguimiento de citas de soporte técnico.

---

##  Integrantes del Equipo y Usuarios de GitHub

| Integrante | Usuario de GitHub | Rol Principal |
| :--- | :--- | :--- |
| **Raúl Valverde** | `raul-valverde` | Backend DAO, Integridad Referencial y Gestión de Recursos JDBC |
| **Kellys Bellanger** | `kellys-bellanger` | UI/UX, Navegación Unificada por Pestañas (`TabPane`) y Estilos CSS |
| **Nora Obregón** | `nora3071` | Documentación, Script SQL DDL/DML y Pruebas de Persistencia |
| **Steven Flores** | `st3v3n030707` | Controladores JavaFX (`ClienteController` y `CitaController`) y Validaciones |

---

##  Distribución de Tareas y Commits por Integrante

###  Steven Flores (`st3v3n030707`) - Controladores JavaFX y Validaciones
- **Implementación de Controladores:** Creación de `ClienteController` y `CitaController` para conectar la vista FXML con la capa DAO.
- **Refresco Dinámico (RF04):** Actualización automática e inmediata de las `TableView` tras operaciones CRUD.
- **Búsqueda Parcial (RF05):** Filtrado en tiempo real case-insensitive por nombre, apellido o correo (`buscarCliente`, `limpiarBusqueda`).
- **Filtros Combinados (RF06):** Filtrado dinámico simultáneo de citas por fecha (`DatePicker`) y estado (`ComboBox`) con opción de restablecimiento (`aplicarFiltrosCombinados`, `limpiarFiltros`).
- **Validaciones de Negocio (RF07):**
  - Control de campos obligatorios en formularios.
  - Validación de formato de correo mediante Expresión Regular Regex (`EMAIL_REGEX`).
  - Verificación de correo único en PostgreSQL antes de registrar o modificar.
- **Alertas y Confirmaciones (RF08):** Modales `Alert.AlertType.CONFIRMATION` previos a la eliminación de registros y captura de errores.

###  Raúl Valverde (`raul-valverde`) - Backend DAO e Integridad Referencial
- **Control de Integridad Referencial (Commit 1):** Implementación de `tieneCitasAsociadas(int clienteId)` en `ClienteDAO` para prevenir el borrado de clientes con citas vinculadas en PostgreSQL (restricción de clave foránea).
- **Cierre de Recursos JDBC (Commit 2):** Refactorización de todas las consultas SQL en `ClienteDAO` y `CitaTecnicaDAO` garantizando el uso de `try-with-resources` para liberar conexiones, sentencias y result sets.

###  Kellys Bellanger (`kellys-bellanger`) - UI/UX y Navegación
- **Navegación por Pestañas (Commit 1):** Creación de `MainView.fxml` integrando un contenedor `TabPane` con `<fx:include>` para unificar la navegación entre `VistaCliente.fxml` y `VistaCitas.fxml`.
- **Diseño y Estilos CSS (Commit 2):** Creación y pulido del archivo `style.css` aplicando paleta de colores moderna para el `TabPane`, cabeceras de `TableView`, estados activo/inactivo y botones de acción.

###  Nora Obregón (`nora3071`) - Documentación y Base de Datos
- **Documentación del Proyecto (Commit 1):** Creación del archivo `README.md` con arquitectura, requisitos del sistema, tabla de integrantes/roles y guía de instalación.
- **Script SQL y Persistencia (Commit 2):** Verificación y estructuración del script `sql/gestion_citas.sql` con esquemas DDL (tablas `cliente` y `cita_tecnica`) y registros de datos semilla DML para pruebas completas.

---

##  Requisitos del Sistema

- **Java Development Kit (JDK):** Versión 21 o superior.
- **Base de Datos:** PostgreSQL 14 o superior.
- **Gestor de Dependencias:** Apache Maven 3.8+.
- **Librerías Incluidas (vía Maven):**
  - JavaFX (Controls, FXML, Graphics) v21.0.6
  - Driver PostgreSQL JDBC v42.7.3

---

##  Configuración Local de PostgreSQL

1. Abra su gestor de PostgreSQL (**pgAdmin 4** o **DBeaver**).
2. Cree una base de datos nombrada `gestion_citas`:
   ```sql
   Ejecuta el archivo gestion_citas.sql que esta en la carpeta sql

 Pasos de Instalación y Ejecución
Clonar el repositorio: git clone https://github.com/raul-valverde/Evaluacion_JavaFX_CitasTecnicas.git

Importar el proyecto:

Abra IntelliJ IDEA o su IDE preferido.

Seleccione Open y elija la carpeta del proyecto.

Deje que Maven descargue las dependencias automáticamente.

Ejecutar la aplicación:

Ubique la clase src/main/java/ni/edu/uam/asistech/MainApp.java.

Ejecute el método main o presione Shift + F10.


 Capturas de Funcionamiento
1. Interfaz Principal y Navegación por Pestañas (TabPane)
Vista unificada que permite alternar entre la gestión de clientes y citas técnicas.
<img width="652" height="592" alt="image" src="https://github.com/user-attachments/assets/76b68b0d-6c6e-42f9-9bb8-aed6f27de80a" />




3. Búsqueda Parcial de Clientes en Tiempo Real (RF05)
Filtrado case-insensitive por nombres o correo electrónico.
<img width="1600" height="775" alt="image" src="https://github.com/user-attachments/assets/f1f58f38-1985-44b2-a0f8-099fbba7eaea" />




5. Filtros Combinados de Citas por Fecha y Estado (RF06)
Evaluación simultánea por texto, fecha y estado de la cita.
<img width="726" height="165" alt="image" src="https://github.com/user-attachments/assets/9fc92faf-65dc-425a-b732-3faf8039a4aa" />
<img width="786" height="151" alt="image" src="https://github.com/user-attachments/assets/45448dee-7633-4d1a-805b-40c63d326ddb" />
<img width="785" height="185" alt="image" src="https://github.com/user-attachments/assets/aa082283-9447-4dc9-9fff-5ac8bc845baf" />





6. Validaciones de Negocio y Alerta de Confirmación (RF07 / RF08)
Validación de correo único, formato Regex y confirmación modal de eliminación.
<img width="1235" height="892" alt="image" src="https://github.com/user-attachments/assets/eea1db32-dc5b-4358-ba6f-f554bccf2979" />
<img width="1238" height="491" alt="image" src="https://github.com/user-attachments/assets/85f88c49-5dff-49ca-a0da-66cf61aade54" />



