package com.store.pressing.repository;

import com.store.pressing.domain.CommandeProduit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommandeProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Long> {}
