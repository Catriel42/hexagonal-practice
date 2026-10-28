/**
 * Integration contracts between business modules. Modules depend only on {@code core-module}, so the services they
 * call on each other are declared here: the owning module implements one as a Spring bean and the others receive it
 * through their use case constructors. Contracts carry {@code UUID}s and plain records, never entities or repositories.
 * Changes to this package go through the practitioner.
 */
package org.jala.edu.core.contract;
