package org.jala.edu.user.infrastructure.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jala.edu.core.infrastructure.persistence.jpa.BaseJpaEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity extends BaseJpaEntity {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(length = 20, nullable = false)
    private String role;

    @Column(length = 20, nullable = false)
    private String status;

    public UserJpaEntity(String id, String firstName, String lastName, String email, String phoneNumber,
                         String role, String status, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }
}
