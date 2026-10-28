# user-module

User management for the Delivery System (`org.jala.edu.user`), built with the port/adapter layout.

- **Use cases:** `CreateUserUseCase`, `ListUsersUseCase`, `UpdateUserUseCase`, `DeleteUserUseCase`,
  `CheckUserAccessUseCase`.
- **Contract:** serves core's `UserService` to the other modules (`UserServiceAdapter`) and stores the core
  `SampleUsers` at startup (`SampleUsersLoader`).
- **Persistence:** JPA adapter (MySQL, Flyway migration in `db/migration/user`) and in-memory adapter,
  selected with `delivery.persistence.type`.
- **UI:** "Users" navigation button → Users main view → Create user, List users (edit dialog, delete confirmation).

## Build

Depends on `core-module`, so install that first (`./mvnw install` in its repository). Then:

```bash
./mvnw install
```

Installs `org.jala.edu:user-module:0.0.1-SNAPSHOT` into `~/.m2` for `delivery-system`. Tests need no database:
the JPA adapter runs against H2 in MySQL mode with the real Flyway migration. Requires JDK 25.

The architecture of the whole system is described in the `delivery-system` repository README.
