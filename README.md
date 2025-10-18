# TiendaOnline

Pequeña aplicación Java Spring Boot para aprendizaje y pruebas — tienda online de ejemplo.

## Descripción

Proyecto demo construido con Spring Boot (starter web + JPA). Por defecto utiliza una base de datos en memoria H2 para desarrollo y pruebas rápidas. Ideal para explorar entidades, controladores y la integración con JPA/Hibernate.

## Requisitos mínimos

- JDK 17 (el proyecto usa `java.version=17` en `pom.xml`).
- Maven 3.x o el wrapper incluido (`mvnw` / `mvnw.cmd`).

## Configuración por defecto (H2)

El proyecto ya viene configurado para arrancar con H2 en memoria. Las propiedades principales están en `src/main/resources/application.properties`:

- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (vacía)
- Consola H2 habilitada en `/h2-console`

Mientras la aplicación está corriendo, puedes abrir la consola H2 en:

http://localhost:8080/h2-console

Usa los valores anteriores (URL JDBC: `jdbc:h2:mem:testdb`, user: `sa`).

> Nota: el puerto por defecto es 8080; si cambias `server.port` en `application.properties`, ajusta la URL.

## Comandos útiles

Desde Windows (wrapper incluido):

```cmd
mvnw.cmd spring-boot:run
```

Con Maven instalado globalmente (Linux/macOS/Windows):

```bash
mvn spring-boot:run
```

Para empaquetar y ejecutar el JAR generado:

```bash
mvn clean package
java -jar target/*.jar
```

Para ejecutar tests:

```bash
mvn test
```

## Endpoints

A continuación se listan los endpoints expuestos por los controladores del proyecto (método HTTP + ruta y nota breve):

- GET    /api/clientes                      — Obtener todos los clientes
- GET    /api/clientes/{id}                 — Obtener cliente por ID
- POST   /api/clientes                      — Crear nuevo cliente (body: ClienteDTO)
- POST   /api/clientes/con-direccion         — Crear cliente con dirección (body: ClienteDTO)
- PUT    /api/clientes/{id}                 — Actualizar cliente (body: ClienteDTO)
- DELETE /api/clientes/{id}                 — Eliminar cliente
- DELETE /api/clientes/cliente/{id}         — Eliminar cliente (alias)

- GET    /api/categorias                    — Obtener todas las categorías
- GET    /api/categorias/{id}               — Obtener categoría por ID
- POST   /api/categorias                    — Crear categoría (body: Categoria)
- PUT    /api/categorias/{id}               — Actualizar categoría (body: Categoria)
- DELETE /api/categorias/{id}               — Eliminar categoría

- GET    /api/productos                     — Obtener productos (opcional: ?categoria=, ?page=, ?size=)
- GET    /api/productos/{id}                — Obtener producto por ID
- POST   /api/productos                     — Crear producto (body: Producto)
- POST   /api/productos/{id}/categorias     — Asignar categorías a producto (body: List<Long>)
- PUT    /api/productos/{id}                — Actualizar producto (body: Producto)
- DELETE /api/productos/{id}                — Eliminar producto

- GET    /api/pedidos                       — Obtener todos los pedidos
- GET    /api/pedidos/{id}                  — Obtener pedido por ID
- POST   /api/pedidos                       — Crear pedido (body: PedidoCreateDTO)
- POST   /api/pedidos/clientes/{clienteId}  — Crear pedido para cliente (body: List<ItemPedidoCreateDTO>)
- PUT    /api/pedidos/{id}                  — Actualizar pedido (body: Pedido)
- PUT    /api/pedidos/{id}/estado           — Cambiar estado del pedido (param: EstadoPedido)
- DELETE /api/pedidos/{id}                  — Eliminar pedido

## Cambiar a otra base de datos (p. ej. PostgreSQL)

El `pom.xml` ya incluye driver de PostgreSQL. Para usarlo en lugar de H2, crea un perfil o modifica `application.properties` con la URL, usuario y contraseña de tu instancia PostgreSQL. Ejemplo mínimo para `application-prod.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/tu_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Y arranca la app con el perfil `prod`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
---
## 📊 Capturas H2 Console o Logs de SQL (Evidencia de Relaciones)

A continuación se muestran capturas del **H2 Console** y de los **logs SQL**, donde se evidencian las relaciones entre las entidades del proyecto.

### 🗄️ Tablas en la base de datos H2
Las siguientes imágenes muestran la estructura generada por JPA, incluyendo claves primarias y foráneas.

<p align="center">
  <img src="https://github.com/user-attachments/assets/8ee8ee83-e46e-4958-a533-f57643105c28" width="90%" alt="Estructura de tablas en H2 Console" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/9dc1392a-d350-45ca-ac25-73477582b693" width="90%" alt="Vista de relaciones en H2 Console" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/2cb6b92b-bcd3-4488-8270-89689788cd4d" width="90%" alt="Relaciones SQL generadas" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/bccea03c-f211-4c4e-8c17-660dc44eaeca" width="90%" alt="Logs SQL evidenciando relaciones" />
</p>
