# core-module

Shared kernel of the Delivery System (`org.jala.edu.core`). Every other repository depends on it.

- **Domain:** `BaseModel<ID>` (id, `createdAt`, `updatedAt`, equality by id) and base exceptions
  (`DomainException`, `NotFoundException`, `ConflictException`).
- **Application:** `UseCase<Input, Output>` and the generic outbound port `CrudRepositoryPort<T, ID>`.
- **Integration contracts:** `UserService`, `CatalogService`, `PaymentService`, `OrderService`, `UserRole` and
  `SampleUsers` (`org.jala.edu.core.contract`), plus a fallback for each one until its module implements it
  (`ContractFallbackAutoConfiguration`).
- **Persistence:** in-memory and JPA base adapters, plus the `delivery.persistence.type` switch
  (`PersistenceType`, `@ConditionalOnJpaPersistence`, `@ConditionalOnInMemoryPersistence`).
- **JavaFX shell:** main window with one navigation button per module (`NavigationItem`, `Navigator`) and UI helpers
  (`BackgroundTasks`, `Dialogs`, `ErrorMessages`, `FormBindings`, `EnumLabelConverter`, `DisplayFormat`).

## Build

```bash
./mvnw install
```

Installs `org.jala.edu:core-module:0.0.1-SNAPSHOT` into the local Maven repository (`~/.m2`), where `user-module`,
`catalog-module` and `delivery-system` pick it up. Requires JDK 25.

The architecture of the whole system is described in the `delivery-system` repository README.
