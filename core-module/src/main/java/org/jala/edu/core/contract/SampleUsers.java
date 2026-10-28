package org.jala.edu.core.contract;

import java.util.UUID;

/**
 * Fixed accounts, one per role plus an inactive one, so every team can try its module with known ids from day one.
 * {@code user-module} stores them at startup; the fallback {@link UserService} answers for them too.
 */
public final class SampleUsers {

    public static final UUID ADMIN = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID MERCHANT = UUID.fromString("00000000-0000-0000-0000-000000000002");
    public static final UUID CUSTOMER = UUID.fromString("00000000-0000-0000-0000-000000000003");
    public static final UUID COURIER = UUID.fromString("00000000-0000-0000-0000-000000000004");
    public static final UUID INACTIVE_CUSTOMER = UUID.fromString("00000000-0000-0000-0000-000000000005");

    private SampleUsers() {
    }
}
