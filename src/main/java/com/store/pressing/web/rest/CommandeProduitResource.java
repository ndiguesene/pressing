package com.store.pressing.web.rest;

import com.store.pressing.domain.CommandeProduit;
import com.store.pressing.repository.CommandeProduitRepository;
import com.store.pressing.service.CommandeProduitService;
import com.store.pressing.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.store.pressing.domain.CommandeProduit}.
 */
@RestController
@RequestMapping("/api")
public class CommandeProduitResource {

    private final Logger log = LoggerFactory.getLogger(CommandeProduitResource.class);

    private static final String ENTITY_NAME = "commandeProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandeProduitService commandeProduitService;

    private final CommandeProduitRepository commandeProduitRepository;

    public CommandeProduitResource(CommandeProduitService commandeProduitService, CommandeProduitRepository commandeProduitRepository) {
        this.commandeProduitService = commandeProduitService;
        this.commandeProduitRepository = commandeProduitRepository;
    }

    /**
     * {@code POST  /commande-produits} : Create a new commandeProduit.
     *
     * @param commandeProduit the commandeProduit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commandeProduit, or with status {@code 400 (Bad Request)} if the commandeProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commande-produits")
    public ResponseEntity<CommandeProduit> createCommandeProduit(@RequestBody CommandeProduit commandeProduit) throws URISyntaxException {
        log.debug("REST request to save CommandeProduit : {}", commandeProduit);
        if (commandeProduit.getId() != null) {
            throw new BadRequestAlertException("A new commandeProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommandeProduit result = commandeProduitService.save(commandeProduit);
        return ResponseEntity
            .created(new URI("/api/commande-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commande-produits/:id} : Updates an existing commandeProduit.
     *
     * @param id the id of the commandeProduit to save.
     * @param commandeProduit the commandeProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandeProduit,
     * or with status {@code 400 (Bad Request)} if the commandeProduit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commandeProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commande-produits/{id}")
    public ResponseEntity<CommandeProduit> updateCommandeProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandeProduit commandeProduit
    ) throws URISyntaxException {
        log.debug("REST request to update CommandeProduit : {}, {}", id, commandeProduit);
        if (commandeProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandeProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandeProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommandeProduit result = commandeProduitService.save(commandeProduit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandeProduit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commande-produits/:id} : Partial updates given fields of an existing commandeProduit, field will ignore if it is null
     *
     * @param id the id of the commandeProduit to save.
     * @param commandeProduit the commandeProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandeProduit,
     * or with status {@code 400 (Bad Request)} if the commandeProduit is not valid,
     * or with status {@code 404 (Not Found)} if the commandeProduit is not found,
     * or with status {@code 500 (Internal Server Error)} if the commandeProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commande-produits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommandeProduit> partialUpdateCommandeProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandeProduit commandeProduit
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommandeProduit partially : {}, {}", id, commandeProduit);
        if (commandeProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandeProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandeProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommandeProduit> result = commandeProduitService.partialUpdate(commandeProduit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandeProduit.getId().toString())
        );
    }

    /**
     * {@code GET  /commande-produits} : get all the commandeProduits.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commandeProduits in body.
     */
    @GetMapping("/commande-produits")
    public List<CommandeProduit> getAllCommandeProduits(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CommandeProduits");
        return commandeProduitService.findAll();
    }

    /**
     * {@code GET  /commande-produits/:id} : get the "id" commandeProduit.
     *
     * @param id the id of the commandeProduit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commandeProduit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commande-produits/{id}")
    public ResponseEntity<CommandeProduit> getCommandeProduit(@PathVariable Long id) {
        log.debug("REST request to get CommandeProduit : {}", id);
        Optional<CommandeProduit> commandeProduit = commandeProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commandeProduit);
    }

    /**
     * {@code DELETE  /commande-produits/:id} : delete the "id" commandeProduit.
     *
     * @param id the id of the commandeProduit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commande-produits/{id}")
    public ResponseEntity<Void> deleteCommandeProduit(@PathVariable Long id) {
        log.debug("REST request to delete CommandeProduit : {}", id);
        commandeProduitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
