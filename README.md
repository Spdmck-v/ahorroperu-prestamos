#  AhorroPeru - Sistema de Gestión de Préstamos

Sistema web desarrollado en Java para la gestión de préstamos de la Cooperativa de Ahorro y Crédito **AHORROPERU**. Permite administrar asociados, solicitudes de préstamo, aprobaciones, generación automática de cuotas y reportes.

---

##  Descripción

AhorroPeru es una aplicación web MVC que digitaliza y automatiza el proceso de préstamos de una cooperativa. Antes del sistema todo se hacía en papel, generando pérdida de información y errores. Ahora todo está centralizado en una plataforma accesible desde cualquier navegador.

### ¿Qué puede hacer el sistema?

- **Gestión de asociados** — registro, edición y desactivación de miembros
- **Solicitudes de préstamo** — los asociados crean solicitudes y el admin las aprueba o rechaza
- **Generación automática de cuotas** — al aprobar un préstamo el sistema calcula y genera todas las cuotas usando el sistema francés (cuota fija)
- **Simulador de cuota** — calcula en tiempo real la cuota estimada antes de enviar la solicitud
- **Registro de pagos** — el admin puede registrar el pago de cada cuota
- **Reportes en Excel** — exporta solicitudes y cronogramas de cuotas con formato profesional
- **Reportes gráficos** — gráfico de torta por estado y barras por monto aprobado por tipo
- **Seguridad** — contraseñas encriptadas con SHA-256 y control de acceso por roles

---

##  Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 11 | Lenguaje principal del backend |
| Apache Tomcat | 9 | Servidor web |
| MySQL | 8.0 | Base de datos |
| Maven | 3.x | Gestión de dependencias |
| JSP + Servlets | Java EE 8 | Patrón MVC |
| Bootstrap | 5.3 | Diseño visual |
| Apache POI | 5.3 | Generación de Excel |
| JFreeChart | 1.5 | Generación de gráficos |
| MySQL Connector/J | 9.x | Conexión Java-MySQL |
| JSTL | 1.2 | Etiquetas para JSP |

---

##  Estructura del proyecto

```
AhorroPeru/
├── pom.xml                          → Dependencias Maven
├── ahorroperu_db.sql                → Script de base de datos
└── src/main/
    ├── java/com/ahorroperu/
    │   ├── modelo/                  → Clases de datos (beans)
    │   ├── dao/                     → Acceso a base de datos (SQL)
    │   ├── servlet/                 → Controladores MVC
    │   └── util/                    → Utilidades (Conexión, Excel, Gráficos)
    └── webapp/
        ├── WEB-INF/                 → Configuración + header y footer
        ├── vistas/                  → Páginas JSP
        ├── css/                     → Estilos (Bootstrap + custom)
        └── js/                      → Scripts JavaScript
```

---

##  Requisitos previos

Antes de instalar asegúrate de tener:

- **JDK 11** o superior
- **Apache Tomcat 9**
- **MySQL 8.0** (o XAMPP)
- **Apache NetBeans** (recomendado) o cualquier IDE con soporte Maven
- **MySQL Workbench** (opcional, para administrar la BD)

---

##  Instalación y configuración

### Paso 1 — Clonar el repositorio
```bash
git clone https://github.com/Spdmck-v/ahorroperu-prestamos.git
```

### Paso 2 — Crear la base de datos
- Abre MySQL Workbench o cualquier cliente MySQL
- Ejecuta el archivo `ahorroperu_tomcat9/ahorroperu_db.sql`
- Esto creará la base de datos `ahorroperu_db` con todas las tablas y datos iniciales

### Paso 3 — Configurar la conexión
Edita el archivo `src/main/java/com/ahorroperu/util/Conexion.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/ahorroperu_db...";
private static final String USUARIO  = "root";
private static final String PASSWORD = "tu_contraseña";
```

### Paso 4 — Abrir en NetBeans
- **File → Open Project** → selecciona la carpeta `ahorroperu_tomcat9`
- NetBeans lo detecta automáticamente como proyecto Maven
- Clic derecho en el proyecto → **Properties → Run** → selecciona Tomcat 9
- **Clean and Build** para descargar dependencias
- **Run** para ejecutar

### Paso 5 — Acceder al sistema
Abre tu navegador en:
```
http://localhost:8080/AhorroPeru
```

---

##  Credenciales de prueba

| Rol | Email | Contraseña |
|---|---|---|
| Administrador | admin@ahorroperu.com | Admin1234 |
| Asociado | juan@test.com | Test1234 |

---

##  Funcionalidades principales

### Panel de administrador
- Dashboard con estadísticas en tiempo real
- Gestión completa de asociados (CRUD)
- Revisión y aprobación/rechazo de solicitudes
- Visualización de préstamos y cuotas
- Generación de reportes Excel y gráficos

### Panel de asociado
- Ver estado de sus solicitudes
- Crear nuevas solicitudes con simulador de cuota
- Ver sus préstamos activos y cronograma de cuotas

---

##  Base de datos

El sistema usa 7 tablas relacionadas:

| Tabla | Descripción |
|---|---|
| usuarios | Personas que acceden al sistema |
| asociados | Miembros de la cooperativa |
| tipo_prestamo | Catálogo de tipos de préstamo |
| solicitudes_prestamo | Solicitudes realizadas por asociados |
| prestamos | Préstamos aprobados activos |
| cuotas | Cuotas generadas automáticamente |
| soportes | Documentos adjuntos a solicitudes |

---

##  Seguridad

- Contraseñas encriptadas con **SHA-256**
- Filtro de autenticación en todas las rutas `/app/*`
- Control de acceso por roles (ADMIN / ASOCIADO)
- Sesión con expiración automática a los 30 minutos

---

##  Cálculo de cuotas

Se usa el **sistema francés** (cuota fija mensual):

```
Cuota = Monto × TasaMensual × (1 + TasaMensual)^Plazo
        ─────────────────────────────────────────────
               (1 + TasaMensual)^Plazo - 1
```

Donde: `TasaMensual = TasaAnual ÷ 12 ÷ 100`

---

##  Autor

Desarrollado por Seiki Cerna Leon como proyecto final del curso **Desarrollo de Software I** — SENATI  

