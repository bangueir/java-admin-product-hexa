# ARQUITECTURA HEXAGONAL - PRODUCT API

## 📋 Índice

1. [Introducción](#1-introducción)
2. [Estructura de Capas](#2-estructura-de-capas)
3. [Flujo de Datos](#3-flujo-de-datos-completo)
4. [Comparación de Arquitecturas](#4-comparación-arquitectura-anterior-vs-hexagonal)
5. [Ventajas](#5-ventajas-de-la-nueva-arquitectura)
6. [Guía de Implementación](#6-guía-de-implementación)
7. [Estructura Final de Archivos](#7-estructura-final-de-archivos)
8. [Resumen de Cambios](#8-resumen-de-cambios)

---

## 1. Introducción

La **Arquitectura Hexagonal** (también conocida como **Puertos y Adaptadores**) fue propuesta por Alistair Cockburn. El objetivo principal es crear aplicaciones que sean:

- ✅ **Independientes de frameworks**
- ✅ **Testables**
- ✅ **Independientes de la UI**
- ✅ **Independientes de la base de datos**
- ✅ **Independientes de agentes externos**

### 1.1 Principios Fundamentales

1. **Separación de Responsabilidades**: El dominio de negocio está separado de los detalles técnicos
2. **Inversión de Dependencias**: Las dependencias apuntan hacia el centro (dominio)
3. **Puertos y Adaptadores**: 
   - **Puertos**: Interfaces que definen cómo interactuar con la aplicación
   - **Adaptadores**: Implementaciones concretas de los puertos

---

## 2. Estructura de Capas

### 2.1 Domain Layer (Centro del Hexágono) 🔵

**Ubicación**: `src/main/java/com/hackerrank/sample/domain/`

**Responsabilidades**:
- Contiene la lógica de negocio pura
- Define las entidades del dominio
- Define las excepciones del dominio
- **NO** tiene dependencias externas

#### Componentes Creados:

##### 📄 `domain/model/ProductDomain.java`
```java
public class ProductDomain {
    // Atributos del dominio (sin anotaciones de JPA)
    private Long id;
    private String title;
    private float price;
    private String currencyId;
    private int availableQuantity;
    private String condition;
    private boolean freeShipping;
    private String description;
    private String pictureUrl;
    
    // Métodos de negocio
    public boolean isNew() {
        return "NEW".equals(condition);
    }
    
    public boolean hasStock() {
        return availableQuantity > 0;
    }
    
    public float calculateTotalValue() {
        return price * availableQuantity;
    }
}
```

**✨ Diferencias clave con el modelo anterior**:
- ✅ Sin anotaciones `@Entity`, `@Table`, `@Column`
- ✅ Contiene lógica de negocio
- ✅ Usa tipos primitivos de Java (String en lugar de Enums de JPA)

##### 📄 Excepciones del Dominio
- `domain/exception/BadResourceRequestException.java`
- `domain/exception/NoSuchResourceFoundException.java`

---

### 2.2 Application Layer (Lógica de Aplicación) 🟢

**Ubicación**: `src/main/java/com/hackerrank/sample/application/`

#### 2.2.1 Puertos de Entrada (Input Ports)

**Ubicación**: `application/port/input/usecase/`

Definen **casos de uso** específicos. Cada caso de uso es una interfaz que representa UNA acción del negocio:

##### 📄 Casos de Uso Implementados:

1. **`CreateProductUseCase`** - Crear un producto
2. **`GetProductUseCase`** - Obtener un producto por ID
3. **`GetAllProductsUseCase`** - Listar todos los productos
4. **`SearchProductsByTitleUseCase`** - Buscar productos por título
5. **`UpdateProductUseCase`** - Actualizar un producto
6. **`DeleteProductUseCase`** - Eliminar un producto
7. **`GetProductWithHigherValueUseCase`** - Obtener el producto con mayor valor
8. **`GetProductsGroupedByCurrencyUseCase`** - Agrupar productos por moneda

**Ejemplo**:
```java
public interface CreateProductUseCase {
    ProductDomain createProduct(ProductDomain product);
}
```

#### 2.2.2 Puertos de Salida (Output Ports)

**Ubicación**: `application/port/output/persistence/`

##### 📄 `ProductPersistencePort.java`

Define operaciones de persistencia **sin especificar cómo se implementan**:

```java
public interface ProductPersistencePort {
    ProductDomain save(ProductDomain product);
    Optional<ProductDomain> findById(Long id);
    List<ProductDomain> findAll();
    List<ProductDomain> findByTitleLikeIgnoreCase(String title);
    void deleteById(Long id);
    boolean existsById(Long id);
}
```

**✨ Beneficios**:
- ✅ El dominio no conoce JPA, Hibernate, o cualquier tecnología de persistencia
- ✅ Podemos cambiar de base de datos sin afectar la lógica de negocio

#### 2.2.3 Service (Implementación de Casos de Uso)

**Ubicación**: `application/service/`

##### 📄 `ProductService.java`

Implementa TODOS los casos de uso y orquesta la lógica de aplicación:

```java
@Service
@Transactional
public class ProductService implements 
        CreateProductUseCase,
        GetProductUseCase,
        GetAllProductsUseCase,
        SearchProductsByTitleUseCase,
        UpdateProductUseCase,
        DeleteProductUseCase,
        GetProductWithHigherValueUseCase,
        GetProductsGroupedByCurrencyUseCase {
    
    private final ProductPersistencePort persistencePort;

    @Override
    public ProductDomain createProduct(ProductDomain product) {
        // Validación
        if (product == null) {
            throw new BadResourceRequestException("Product payload is required.");
        }
        // Delegación al puerto de salida
        return persistencePort.save(product);
    }
    
    // ... otros métodos
}
```

---

### 2.3 Infrastructure Layer (Adaptadores) 🟡

**Ubicación**: `src/main/java/com/hackerrank/sample/infrastructure/`

#### 2.3.1 Adaptador de Entrada REST

**Ubicación**: `infrastructure/adapter/input/rest/`

##### 📄 `controller/ProductController.java`

Adaptador REST que:
- Recibe peticiones HTTP
- Valida DTOs REST
- Invoca casos de uso
- Convierte respuestas del dominio a DTOs REST

```java
@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    // ... otros casos de uso
    private final ProductRestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductRestDto createProduct(@Valid @RequestBody ProductRestDto dto) {
        ProductDomain domain = mapper.toDomain(dto);
        ProductDomain created = createProductUseCase.createProduct(domain);
        return mapper.toRestDto(created);
    }
    
    // ... otros endpoints
}
```

##### 📄 `dto/ProductRestDto.java`

- Contiene validaciones Bean Validation (`@NotBlank`, `@Pattern`, etc.)
- Específico para la API REST
- **NO se usa en el dominio**

```java
public class ProductRestDto {
    @NotBlank(message = "Title must not be null or blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Float price;
    
    // ... otros campos con validaciones
}
```

##### 📄 `mapper/ProductRestMapper.java`

Convierte entre `ProductRestDto` ↔ `ProductDomain`

```java
@Component
public class ProductRestMapper {
    public ProductDomain toDomain(ProductRestDto dto) {
        // Conversión de DTO REST → Dominio
    }
    
    public ProductRestDto toRestDto(ProductDomain domain) {
        // Conversión de Dominio → DTO REST
    }
}
```

#### 2.3.2 Adaptador de Salida JPA

**Ubicación**: `infrastructure/adapter/output/persistence/`

##### 📄 `jpa/entity/ProductEntity.java`

Entidad JPA para persistencia:

```java
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private float price;
    
    // ... otros campos con anotaciones JPA
}
```

##### 📄 `jpa/repository/ProductJpaRepository.java`

Repositorio JPA estándar:

```java
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<ProductEntity> findByTitleLikeIgnoreCase(@Param("title") String title);
}
```

##### 📄 `ProductPersistenceAdapter.java`

**Implementa** el puerto de salida `ProductPersistencePort`:

```java
@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {
    private final ProductJpaRepository repository;
    private final ProductPersistenceMapper mapper;

    @Override
    public ProductDomain save(ProductDomain product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }
    
    // ... otros métodos
}
```

##### 📄 `mapper/ProductPersistenceMapper.java`

Convierte entre `ProductEntity` ↔ `ProductDomain`

```java
@Component
public class ProductPersistenceMapper {
    public ProductDomain toDomain(ProductEntity entity) {
        // Conversión de Entity → Dominio
    }
    
    public ProductEntity toEntity(ProductDomain domain) {
        // Conversión de Dominio → Entity
    }
}
```

##### 📄 `exception/GlobalExceptionHandler.java`

Maneja todas las excepciones de forma centralizada:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(...) {
        // Manejo de errores de validación
    }
    
    @ExceptionHandler(BadResourceRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadResourceRequest(...) {
        // Manejo de solicitudes inválidas
    }
    
    @ExceptionHandler(NoSuchResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(...) {
        // Manejo de recursos no encontrados
    }
}
```

---

## 3. Flujo de Datos Completo

### Ejemplo: Crear un Producto

```
┌─────────────┐
│   Cliente   │
│  (Postman)  │
└──────┬──────┘
       │ POST /products + JSON
       │
┌──────▼──────────────────────────────────────────┐
│  Infrastructure Layer (Adaptador REST)          │
│                                                  │
│  ProductController                               │
│  ├─ Recibe ProductRestDto (validado)            │
│  ├─ ProductRestMapper.toDomain()                │
│  └─ Llama CreateProductUseCase                  │
└──────┬──────────────────────────────────────────┘
       │ ProductDomain (objeto del dominio)
       │
┌──────▼──────────────────────────────────────────┐
│  Application Layer (Lógica de Aplicación)       │
│                                                  │
│  ProductService                                  │
│  ├─ Valida reglas de negocio                    │
│  └─ Llama ProductPersistencePort.save()         │
└──────┬──────────────────────────────────────────┘
       │ ProductDomain
       │
┌──────▼──────────────────────────────────────────┐
│  Infrastructure Layer (Adaptador Persistencia)  │
│                                                  │
│  ProductPersistenceAdapter                       │
│  ├─ ProductPersistenceMapper.toEntity()         │
│  ├─ ProductJpaRepository.save()                 │
│  └─ ProductPersistenceMapper.toDomain()         │
└──────┬──────────────────────────────────────────┘
       │ Retorna ProductDomain
       │
┌──────▼──────────────────────────────────────────┐
│  Base de Datos (H2)                             │
│  INSERT INTO products ...                        │
└─────────────────────────────────────────────────┘
```

---

## 4. Comparación: Arquitectura Anterior vs Hexagonal

### ❌ Arquitectura en Capas (Anterior)

```
Controller → Service → Repository → Database
     ↓          ↓          ↓
   DTOs    Entity+DTO   Entity (JPA)
```

**Problemas**:
- ❌ El servicio conoce detalles de JPA
- ❌ Entity mezcla lógica de negocio con persistencia
- ❌ Difícil de testear sin base de datos
- ❌ Acoplamiento alto con frameworks

### ✅ Arquitectura Hexagonal (Nueva)

```
        ┌─────────────────────┐
        │   Domain Layer      │
        │  (ProductDomain)    │
        │  (Excepciones)      │
        └─────────┬───────────┘
                  │
        ┌─────────▼───────────┐
        │  Application Layer  │
        │  (Casos de Uso)     │
        │  (Puertos)          │
        └─────┬───────────┬───┘
              │           │
    ┌─────────▼───┐   ┌──▼────────────┐
    │ Adaptador   │   │  Adaptador    │
    │   REST      │   │  Persistencia │
    │ (Controller)│   │  (JPA)        │
    └─────────────┘   └───────────────┘
```

**✨ Beneficios**:
- ✅ Dominio puro (sin dependencias externas)
- ✅ Fácil de testear (mockear puertos)
- ✅ Cambiar tecnologías sin afectar el dominio
- ✅ Código más mantenible

---

## 5. Ventajas de la Nueva Arquitectura

### 5.1 Independencia de Frameworks

```java
// ProductDomain - Sin anotaciones de framework
public class ProductDomain {
    private Long id;
    private String title;
    // Lógica de negocio pura
}
```

### 5.2 Testabilidad Mejorada

```java
@Test
public void createProduct_shouldSaveProduct() {
    // Mock del puerto de salida
    ProductPersistencePort mockPort = mock(ProductPersistencePort.class);
    ProductService service = new ProductService(mockPort);
    
    // Test sin base de datos
    ProductDomain product = new ProductDomain(...);
    when(mockPort.save(any())).thenReturn(product);
    
    ProductDomain result = service.createProduct(product);
    
    assertNotNull(result);
}
```

### 5.3 Flexibilidad Tecnológica

Cambiar de JPA a MongoDB solo requiere:
1. Crear nuevo adaptador de persistencia
2. Implementar `ProductPersistencePort`
3. **NO cambiar** dominio ni casos de uso

---

## 6. Guía de Implementación

### 6.1 Agregar un Nuevo Caso de Uso

**Paso 1**: Crear puerto de entrada
```java
// application/port/input/usecase/GetProductsByCategoryUseCase.java
public interface GetProductsByCategoryUseCase {
    List<ProductDomain> getByCategory(String category);
}
```

**Paso 2**: Implementar en el servicio
```java
@Service
public class ProductService implements GetProductsByCategoryUseCase {
    @Override
    public List<ProductDomain> getByCategory(String category) {
        // Lógica de negocio
    }
}
```

**Paso 3**: Exponer en el controlador
```java
@GetMapping("/category/{category}")
public List<ProductRestDto> getByCategory(@PathVariable String category) {
    return useCase.getByCategory(category).stream()
            .map(mapper::toRestDto)
            .collect(Collectors.toList());
}
```

### 6.2 Cambiar Proveedor de Base de Datos

Solo necesitas crear un nuevo adaptador:

```java
@Component
public class MongoProductPersistenceAdapter implements ProductPersistencePort {
    // Implementación con MongoDB
}
```

**No hay cambios en**:
- Domain
- Application Layer
- REST Controller

---

## 7. Estructura Final de Archivos

```
src/main/java/com/hackerrank/sample/
│
├── domain/                                    # 🔵 DOMINIO (Centro)
│   ├── model/
│   │   └── ProductDomain.java                # Entidad del dominio
│   └── exception/
│       ├── BadResourceRequestException.java
│       └── NoSuchResourceFoundException.java
│
├── application/                               # 🟢 APLICACIÓN
│   ├── port/
│   │   ├── input/
│   │   │   └── usecase/                      # Casos de uso (8 interfaces)
│   │   │       ├── CreateProductUseCase.java
│   │   │       ├── GetProductUseCase.java
│   │   │       ├── GetAllProductsUseCase.java
│   │   │       ├── SearchProductsByTitleUseCase.java
│   │   │       ├── UpdateProductUseCase.java
│   │   │       ├── DeleteProductUseCase.java
│   │   │       ├── GetProductWithHigherValueUseCase.java
│   │   │       └── GetProductsGroupedByCurrencyUseCase.java
│   │   └── output/
│   │       └── persistence/
│   │           └── ProductPersistencePort.java  # Puerto de salida
│   └── service/
│       └── ProductService.java                # Implementa todos los casos de uso
│
└── infrastructure/                            # 🟡 INFRAESTRUCTURA
    └── adapter/
        ├── input/
        │   └── rest/                          # Adaptador REST
        │       ├── controller/
        │       │   └── ProductController.java
        │       ├── dto/
        │       │   └── ProductRestDto.java    # DTO con validaciones
        │       ├── mapper/
        │       │   └── ProductRestMapper.java
        │       └── exception/
        │           └── GlobalExceptionHandler.java
        └── output/
            └── persistence/                   # Adaptador JPA
                ├── ProductPersistenceAdapter.java  # Implementa el puerto
                ├── jpa/
                │   ├── entity/
                │   │   └── ProductEntity.java     # Entidad JPA
                │   └── repository/
                │       └── ProductJpaRepository.java
                └── mapper/
                    └── ProductPersistenceMapper.java
```

---

## 8. Resumen de Cambios

| Concepto | Arquitectura Anterior | Arquitectura Hexagonal |
|----------|----------------------|------------------------|
| **Modelo** | `Product` (Entity con `@Entity`) | `ProductDomain` (POJO puro) |
| **DTO** | `ProductDto` (usado en servicio) | `ProductRestDto` (solo en REST) |
| **Servicio** | `ProductServiceImpl` | `ProductService` (casos de uso) |
| **Repositorio** | `ProductRepository` (directo) | `ProductPersistencePort` (interfaz) |
| **Entidad JPA** | `Product` (mezclado) | `ProductEntity` (separado) |
| **Mappers** | `ProductMapper` (uno) | `ProductRestMapper` + `ProductPersistenceMapper` |
| **Excepciones** | En `exception/` | En `domain/exception/` |
| **Capas** | Controller → Service → Repository | Controller → UseCase → Port → Adapter |

---

## 9. Endpoints de la API

Todos los endpoints permanecen iguales que en la versión anterior:

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

---

## 10. Conclusión

La **Arquitectura Hexagonal** proporciona:

1. ✅ **Dominio puro** sin dependencias externas
2. ✅ **Alta testabilidad** mediante puertos mockeables
3. ✅ **Flexibilidad** para cambiar tecnologías
4. ✅ **Separación clara** de responsabilidades
5. ✅ **Mantenibilidad** a largo plazo

El proyecto ahora cumple con los principios **SOLID** y **Clean Architecture**, siendo:
- **Testeable** sin necesidad de base de datos
- **Escalable** para agregar nuevos adaptadores
- **Mantenible** con responsabilidades bien definidas

---

## 📚 Referencias

- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [DDD (Domain-Driven Design) - Eric Evans](https://www.domainlanguage.com/ddd/)
