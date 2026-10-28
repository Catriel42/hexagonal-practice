# Practica de Arquitectura Hexagonal — Modular Monolith

Esta es una version simplificada del proyecto principal (capstone), preparada especificamente para que trabajes de forma enfocada y sin distracciones.

Elimine intencionalmente los modulos mas complejos (`order-module`, `delivery-module` y `payment-module`) para quitar ruido del medio. El objetivo de este laboratorio es puramente didactico: quiero que tomes todo lo que ya analizamos y conoces de `user-module` y lo traslades paso a paso a `catalog-module`. `user-module` es tu espejo y tu guia de referencia; tu tarea es replicar esa misma disciplina de diseno, separacion de responsabilidades y arquitectura hexagonal en el contexto de un catalogo de productos.

---

## Comandos rapidos para iniciar

Antes de comenzar a programar, asegurate de compilar e instalar los modulos en tu repositorio local de Maven (`~/.m2`):

### Windows (PowerShell o CMD)

```cmd
rem 1. Compilar e instalar en orden de dependencias
cd core-module && mvnw.cmd install -DskipTests
cd ..\user-module && mvnw.cmd install -DskipTests
cd ..\catalog-module && mvnw.cmd install -DskipTests

rem 2. Ejecutar la aplicacion en modo memoria (sin necesidad de base de datos)
cd ..\delivery-system
mvnw.cmd javafx:run -Pmemory
```

### Linux / macOS

```bash
# 1. Dar permisos de ejecucion a los wrappers de Maven
chmod +x core-module/mvnw user-module/mvnw catalog-module/mvnw delivery-system/mvnw

# 2. Compilar e instalar en orden de dependencias
cd core-module && ./mvnw install -DskipTests
cd ../user-module && ./mvnw install -DskipTests
cd ../catalog-module && ./mvnw install -DskipTests

# 3. Ejecutar la aplicacion en modo memoria (sin necesidad de base de datos)
cd ../delivery-system
./mvnw javafx:run -Pmemory
```

---

## Estructura del proyecto

Vas a encontrar 4 carpetas en el repositorio:

- **`core-module`**: El kernel compartido del sistema. Aca viven las abstracciones genericas (`BaseModel`, `CrudRepositoryPort`), el manejo base de excepciones, el contenedor principal de la interfaz JavaFX y los contratos con sus implementaciones de fallback para que todo compile sin depender de modulos externos.
- **`user-module`**: Tu referencia terminada. Esta completo de punta a punta: modelo de dominio, validaciones en Value Objects, casos de uso, doble adaptador de persistencia (memoria y JPA con Flyway) y pantallas en JavaFX. Usalo para consultar como se estructuro cada clase.
- **`catalog-module`**: Tu espacio de trabajo. Actualmente es solo un esqueleto con una vista vacia. Aca vas a programar todo el catalogo de productos imitando la estructura de `user-module`.
- **`delivery-system`**: La aplicacion de escritorio que une todas las piezas, inicia Spring Boot y levanta la interfaz grafica.

---

## Requisitos previos

- **JDK 25** o superior (el proyecto esta configurado con Lombok 1.18.48 para compilar sin problemas en versiones modernas de OpenJDK).
- **Git**.
- **Docker y Docker Compose** (opcional, unicamente si vas a probar persistencia real con MySQL).

Verifica que tu terminal reconozca el compilador y la variable `JAVA_HOME`:

### Windows

```powershell
java -version
javac -version
echo $env:JAVA_HOME
```

### Linux / macOS

```bash
java -version
javac -version
echo $JAVA_HOME
```

---

## Formas de ejecutar la aplicacion

### 1. Modo en memoria (Recomendado para el desarrollo diario)

No necesitas instalar bases de datos ni correr Docker. La aplicacion guarda los datos en memoria mientras este abierta.

#### Windows:

```cmd
cd delivery-system
mvnw.cmd javafx:run -Pmemory
```

#### Linux / macOS:

```bash
cd delivery-system
./mvnw javafx:run -Pmemory
```

En la barra de navegacion vas a ver dos botones:
- **Users**: modulo completamente funcional para que interactues con el y veas como responde la UI ante validaciones y errores.
- **Catalog**: tu punto de partida. Actualmente muestra una vista placeholder que vas a reemplazar a medida que construyas tus pantallas.

---

### 2. Modo MySQL con Docker

Cuando tengas lista la persistencia JPA y quieras comprobar que las migraciones de Flyway y la base de datos funcionan en un entorno real:

1. Inicia el contenedor de MySQL:

   ```bash
   cd delivery-system
   docker compose up -d
   docker compose ps
   ```

2. Ejecuta la aplicacion indicando que use el perfil JPA:

   #### Windows (PowerShell):
   ```powershell
   $env:DELIVERY_PERSISTENCE_TYPE="jpa"
   mvnw.cmd javafx:run
   ```

   #### Windows (CMD):
   ```cmd
   set DELIVERY_PERSISTENCE_TYPE=jpa
   mvnw.cmd javafx:run
   ```

   #### Linux / macOS:
   ```bash
   DELIVERY_PERSISTENCE_TYPE=jpa ./mvnw javafx:run
   ```

3. Para detener el contenedor:

   ```bash
   docker compose down       # Detiene el servicio y conserva los datos en el volumen
   docker compose down -v    # Detiene y borra todo para reiniciar desde cero
   ```

---

## Hoja de ruta recomendada para `catalog-module`

No empieces tirando botones en la interfaz ni configurando base de datos. Avanza de adentro hacia afuera, respetando la arquitectura:

```
org.jala.edu.catalog
├── domain/                               # 1. Reglas puras de negocio (Java puro, sin dependencias)
│   ├── model/ (Product, ProductId, Price, Stock)
│   └── exception/ (ProductNotFoundException, InvalidPriceException)
│
├── application/                          # 2. Casos de uso y Puertos
│   ├── port/in/ (CreateProductCommand, UpdateProductCommand, ListProductsQuery)
│   ├── port/out/ (CatalogRepositoryPort extends CrudRepositoryPort)
│   └── usecase/ (CreateProductUseCase, ListProductsUseCase, etc.)
│
└── infrastructure/                       # 3. Adaptadores y Configuracion
    ├── adapter/out/persistence/memory/   # InMemoryCatalogRepositoryAdapter
    ├── adapter/out/persistence/jpa/      # ProductJpaEntity, ProductJpaRepository, ProductPersistenceAdapter
    ├── adapter/in/contract/              # CatalogServiceAdapter (implementa el contrato de core)
    ├── adapter/in/ui/                    # Controladores JavaFX y vistas .fxml
    └── config/                           # CatalogModuleConfiguration, CatalogUiConfiguration
```

### Pasos sugeridos:

1. **Disena el dominio**: Crea la entidad `Product` heredando de `BaseModel<ProductId>`. Modela los atributos principales (nombre, descripcion, precio, stock, estado) y protege los invariantes con Value Objects y excepciones de dominio.
2. **Define los puertos y casos de uso**: Declara las operaciones que necesita tu modulo en `port/in` y la interfaz del repositorio en `port/out`. Implementa cada caso de uso con su test unitario correspondiente.
3. **Persistencia en memoria**: Construye primero `InMemoryCatalogRepositoryAdapter` para poder probar todo el flujo de negocio rapidamente sin tocar bases de datos.
4. **Persistencia JPA**: Crea el script de migracion SQL en `src/main/resources/db/migration/catalog/` y escribe el adaptador JPA con su entidad y repositorio de Spring Data.
5. **Interfaz grafica (JavaFX)**: Disena las vistas `.fxml` e implementa los controladores correspondientes para listar, crear y editar productos.
6. **Integracion y prueba final**: Ejecuta `./mvnw clean install` dentro de `catalog-module` y vuelve a levantar `delivery-system` para verificar tu modulo integrado en el sistema.
