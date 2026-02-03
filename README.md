# Product API

API REST para administrar productos y soportar una página de detalle de artículo.

## Requisitos
- Java 21
- Maven

## Configuración de base de datos (H2)
La aplicación usa H2 en memoria con inicialización automática de esquema y datos.

Archivo de configuración:
- [src/main/resources/application.properties](src/main/resources/application.properties)

Scripts:
- [src/main/resources/schema.sql](src/main/resources/schema.sql)
- [src/main/resources/data.sql](src/main/resources/data.sql)

Consola H2:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:productsdb
- User: sa
- Password: (vacío)

## Endpoints
Base URL: http://localhost:8080

- GET /products
  - Descripción: lista todos los productos.
  - Respuesta: 200 OK, JSON array.
- GET /products/{id}
  - Descripción: obtiene un producto por id.
  - Respuesta: 200 OK o 404 Not Found.
- GET /products/search?title={title}
  - Descripción: busca productos por título (LIKE, case-insensitive).
  - Respuesta: 200 OK, JSON array.
- GET /products/report/topvalue
  - Descripción: retorna el producto con mayor precio.
  - Respuesta: 200 OK o 404 Not Found.
- GET /products/report/groupcurrency
  - Descripción: agrupa productos por moneda.
  - Respuesta: 200 OK, JSON object.
- POST /products
  - Descripción: crea un producto.
  - Respuesta: 201 Created.
- PUT /products/{id}
  - Descripción: actualiza un producto.
  - Respuesta: 200 OK o 404 Not Found.
- DELETE /products/{id}
  - Descripción: elimina un producto.
  - Respuesta: 204 No Content o 404 Not Found.

Validaciones:
- Definidas en [src/main/java/com/hackerrank/sample/dto/ProductDto.java](src/main/java/com/hackerrank/sample/dto/ProductDto.java)
- Manejo de errores en [src/main/java/com/hackerrank/sample/exception/GlobalExceptionHandler.java](src/main/java/com/hackerrank/sample/exception/GlobalExceptionHandler.java)

## Postman
Colección disponible en:
- [postman/ProductController.postman_collection.json](postman/ProductController.postman_collection.json)

Configurar la variable baseUrl en Postman (por defecto http://localhost:8080).

## Ejecución de la aplicación
Desde la raíz del proyecto:
- mvn clean spring-boot:run

## Pruebas
- Ejecutar todas las pruebas:
  - mvn test

Incluye:
- Pruebas dinámicas con JSON en [src/test/java/com/hackerrank/sample/HttpJsonDynamicUnitTest.java](src/test/java/com/hackerrank/sample/HttpJsonDynamicUnitTest.java)
- Casos de prueba en [src/test/resources/testcases](src/test/resources/testcases)
  - Incluye búsquedas y reportes (http02.json)
- Pruebas unitarias del servicio en [src/test/java/com/hackerrank/sample/service/Impl/ProductServiceImplTest.java](src/test/java/com/hackerrank/sample/service/Impl/ProductServiceImplTest.java)
- Pruebas de mapeo en [src/test/java/com/hackerrank/sample/mapper/ProductMapperTest.java](src/test/java/com/hackerrank/sample/mapper/ProductMapperTest.java)

## Notas de buenas prácticas
- Validación de entradas con Bean Validation.
- Manejo centralizado de errores con RestControllerAdvice.
- Datos de prueba automáticos con scripts SQL.
