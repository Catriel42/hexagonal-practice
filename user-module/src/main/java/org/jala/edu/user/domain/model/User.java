package org.jala.edu.user.domain.model;

import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.jala.edu.core.domain.exception.DomainException;
import org.jala.edu.core.domain.model.BaseModel;

/**
 * User aggregate root. Framework-free: persistence and web concerns live in adapters.
 * State only changes through behavior methods, so no setters are generated.
 * Identity, timestamps and equality come from {@link BaseModel}.
 */
@Getter
@ToString(callSuper = true)
public class User extends BaseModel<UserId> {

    private String firstName;
    private String lastName;
    private final Email email;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;

    private User(UserId id, String firstName, String lastName, Email email, String phoneNumber,
                 UserRole role, UserStatus status, Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
        this.firstName = requireText(firstName, "First name");
        this.lastName = requireText(lastName, "Last name");
        this.email = Objects.requireNonNull(email, "Email is required");
        this.phoneNumber = phoneNumber;
        this.role = Objects.requireNonNull(role, "Role is required");
        this.status = Objects.requireNonNull(status, "Status is required");
    }

    /**
     * Creates a brand-new user. New users start as {@link UserStatus#ACTIVE}.
     */
    public static User register(String firstName, String lastName, Email email, String phoneNumber, UserRole role) {
        Instant now = now();
        return new User(UserId.newId(), firstName, lastName, email, phoneNumber, role, UserStatus.ACTIVE, now, now);
    }

    /**
     * Rebuilds an existing user from storage. Only persistence adapters should call this.
     */
    public static User restore(UserId id, String firstName, String lastName, Email email, String phoneNumber,
                               UserRole role, UserStatus status, Instant createdAt, Instant updatedAt) {
        return new User(id, firstName, lastName, email, phoneNumber, role, status, createdAt, updatedAt);
    }

    /**
     * Replaces the profile data. All values are validated before any is applied, so a rejected
     * update leaves the user unchanged.
     */
    public void updateProfile(String firstName, String lastName, String phoneNumber) {
        String validFirstName = requireText(firstName, "First name");
        String validLastName = requireText(lastName, "Last name");
        this.firstName = validFirstName;
        this.lastName = validLastName;
        this.phoneNumber = phoneNumber;
        touch();
    }

    public void changeRole(UserRole role) {
        this.role = Objects.requireNonNull(role, "Role is required");
        touch();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        touch();
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new DomainException(field + " cannot be blank");
        }
        return value.strip();
    }
}
