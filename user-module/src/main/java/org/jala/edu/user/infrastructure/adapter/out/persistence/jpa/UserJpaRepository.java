package org.jala.edu.user.infrastructure.adapter.out.persistence.jpa;

import org.jala.edu.core.infrastructure.persistence.jpa.BaseEntityRepository;

interface UserJpaRepository extends BaseEntityRepository<UserJpaEntity, String> {

    boolean existsByEmail(String email);
}
