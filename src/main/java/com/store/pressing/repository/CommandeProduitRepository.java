package com.store.pressing.repository;

import com.store.pressing.domain.CommandeProduit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommandeProduit entity.
 */
@Repository
public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Long> {
    @Query(
        value = "select distinct commandeProduit from CommandeProduit commandeProduit left join fetch commandeProduit.produits",
        countQuery = "select count(distinct commandeProduit) from CommandeProduit commandeProduit"
    )
    Page<CommandeProduit> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct commandeProduit from CommandeProduit commandeProduit left join fetch commandeProduit.produits")
    List<CommandeProduit> findAllWithEagerRelationships();

    @Query(
        "select commandeProduit from CommandeProduit commandeProduit left join fetch commandeProduit.produits where commandeProduit.id =:id"
    )
    Optional<CommandeProduit> findOneWithEagerRelationships(@Param("id") Long id);
}
