# Migración a Arquitectura Hexagonal - Resumen

Este proyecto ha sido migrado exitosamente de una arquitectura en capas tradicional a una **Arquitectura Hexagonal (Puertos y Adaptadores)**.

## 📁 Nueva Estructura del Proyecto

```
src/main/java/com/hackerrank/sample/
│
├── domain/                                    # 🔵 DOMINIO (Núcleo del negocio)
│   ├── model/
│   │   └── ProductDomain.java
│   └── exception/
│       ├── BadResourceRequestException.java
│       └── NoSuchResourceFoundException.java
│
├── application/                               # 🟢 APLICACIÓN (Lógica de aplicación)
│   ├── port/
│   │   ├── input/usecase/                    # 8 interfaces de casos de uso
│   │   └── output/persistence/
│   │       └── ProductPersistencePort.java
│   └── service/
│       └── ProductService.java
│
└── infrastructure/                            # 🟡 INFRAESTRUCTURA (Adaptadores)
    └── adapter/
        ├── input/rest/                        # Adaptador de entrada REST
        │   ├── controller/
        │   │   └── ProductController.java
        │   ├── dto/
        │   │   └── ProductRestDto.java
        │   ├── mapper/
        │   │   └── ProductRestMapper.java
        │   └── exception/
        │       └── GlobalExceptionHandler.java
        └── output/persistence/                # Adaptador de salida JPA
            ├── ProductPersistenceAdapter.java
            ├── jpa/
            │   ├── entity/
            │   │   └── ProductEntity.java
            │   └── repository/
            │       └── ProductJpaRepository.java
            └── mapper/
                └── ProductPersistenceMapper.java
```

## 🎯 Cambios Principales

### Archivos Creados (Nueva Arquitectura)

**Domain Layer:**
- ✅ `domain/model/ProductDomain.java` - Entidad del dominio (sin JPA)
- ✅ `domain/exception/BadResourceRequestException.java`
- ✅ `domain/exception/NoSuchResourceFoundException.java`

**Application Layer:**
- ✅ `application/port/input/usecase/CreateProductUseCase.java`
- ✅ `application/port/input/usecase/GetProductUseCase.java`
- ✅ `application/port/input/usecase/GetAllProductsUseCase.java`
- ✅ `application/port/input/usecase/SearchProductsByTitleUseCase.java`
- ✅ `application/port/input/usecase/UpdateProductUseCase.java`
- ✅ `application/port/input/usecase/DeleteProductUseCase.java`
- ✅ `application/port/input/usecase/GetProductWithHigherValueUseCase.java`
- ✅ `application/port/input/usecase/GetProductsGroupedByCurrencyUseCase.java`
- ✅ `application/port/output/persistence/ProductPersistencePort.java`
- ✅ `application/service/ProductService.java` - Implementa todos los casos de uso

**Infrastructure Layer:**
- ✅ `infrastructure/adapter/input/rest/controller/ProductController.java`
- ✅ `infrastructure/adapter/input/rest/dto/ProductRestDto.java`
- ✅ `infrastructure/adapter/input/rest/mapper/ProductRestMapper.java`
- ✅ `infrastructure/adapter/input/rest/exception/GlobalExceptionHandler.java`
- ✅ `infrastructure/adapter/output/persistence/jpa/entity/ProductEntity.java`
- ✅ `infrastructure/adapter/output/persistence/jpa/repository/ProductJpaRepository.java`
- ✅ `infrastructure/adapter/output/persistence/ProductPersistenceAdapter.java`
- ✅ `infrastructure/adapter/output/persistence/mapper/ProductPersistenceMapper.java`

### Archivos Antiguos (Renombrados con .old)

Los siguientes archivos fueron renombrados para evitar conflictos:
- `controller/ProductController.java.old`
- `service/ProductService.java.old`
- `service/Impl/ProductServiceImpl.java.old`
- `exception/GlobalExceptionHandler.java.old`
- `exception/BadResourceRequestException.java.old`
- `exception/NoSuchResourceFoundException.java.old`

## ✅ Verificación

### Compilación
```bash
mvn clean compile
```
✅ **BUILD SUCCESS**

### Tests
```bash
mvn test
```
✅ **Tests run: 18, Failures: 0, Errors: 0, Skipped: 0**

## 📚 Documentación Completa

Para una explicación detallada de la arquitectura hexagonal, consulta:
- **[ARQUITECTURA_HEXAGONAL.md](ARQUITECTURA_HEXAGONAL.md)** - Documentación completa con diagramas y ejemplos

## 🚀 Ventajas de la Nueva Arquitectura

1. **Independencia de Frameworks**: El dominio no depende de Spring, JPA o cualquier framework
2. **Testabilidad**: Fácil mockear puertos para tests unitarios
3. **Flexibilidad**: Cambiar la BD o framework REST sin afectar el dominio
4. **Mantenibilidad**: Responsabilidades claramente separadas
5. **Escalabilidad**: Fácil agregar nuevos adaptadores (GraphQL, gRPC, etc.)

## 📋 Endpoints de la API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/products` | Listar todos los productos |
| GET | `/products/{id}` | Obtener un producto por ID |
| GET | `/products/search?title={title}` | Buscar productos por título |
| GET | `/products/report/topvalue` | Producto con mayor precio |
| GET | `/products/report/groupcurrency` | Agrupar por moneda |
| POST | `/products` | Crear un nuevo producto |
| PUT | `/products/{id}` | Actualizar un producto |
| DELETE | `/products/{id}` | Eliminar un producto |

## 🔄 Flujo de Datos

```
Cliente HTTP → ProductController (REST Adapter)
            ↓
    ProductRestMapper.toDomain()
            ↓
    Use Case (ProductService)
            ↓
    ProductPersistencePort
            ↓
    ProductPersistenceAdapter (JPA Adapter)
            ↓
    ProductJpaRepository
            ↓
    Base de Datos (H2)
```

## 🛠️ Tecnologías

- **Spring Boot 3.2.5**
- **Java 21**
- **JPA/Hibernate**
- **H2 Database**
- **Maven**
- **JUnit 5**
- **Mockito**

## 📝 Notas

- La funcionalidad de la API permanece **100% igual**
- Los tests existentes fueron actualizados para usar la nueva arquitectura
- No se requieren cambios en los clientes de la API
- Todos los endpoints funcionan exactamente igual que antes

---

**Fecha de Migración**: Febrero 6, 2026  
**Estado**: ✅ Completado y Verificado
