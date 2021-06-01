package com.store.pressing.service;

import com.store.pressing.domain.CommandeProduit;
import com.store.pressing.repository.CommandeProduitRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommandeProduit}.
 */
@Service
@Transactional
public class CommandeProduitService {

    private final Logger log = LoggerFactory.getLogger(CommandeProduitService.class);

    private final CommandeProduitRepository commandeProduitRepository;

    public CommandeProduitService(CommandeProduitRepository commandeProduitRepository) {
        this.commandeProduitRepository = commandeProduitRepository;
    }

    /**
     * Save a commandeProduit.
     *
     * @param commandeProduit the entity to save.
     * @return the persisted entity.
     */
    public CommandeProduit save(CommandeProduit commandeProduit) {
        log.debug("Request to save CommandeProduit : {}", commandeProduit);
        return commandeProduitRepository.save(commandeProduit);
    }

    /**
     * Partially update a commandeProduit.
     *
     * @param commandeProduit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandeProduit> partialUpdate(CommandeProduit commandeProduit) {
        log.debug("Request to partially update CommandeProduit : {}", commandeProduit);

        return commandeProduitRepository
            .findById(commandeProduit.getId())
            .map(
                existingCommandeProduit -> {
                    if (commandeProduit.getPrixAvance() != null) {
                        existingCommandeProduit.setPrixAvance(commandeProduit.getPrixAvance());
                    }
                    if (commandeProduit.getPrixRestant() != null) {
                        existingCommandeProduit.setPrixRestant(commandeProduit.getPrixRestant());
                    }
                    if (commandeProduit.getStatus() != null) {
                        existingCommandeProduit.setStatus(commandeProduit.getStatus());
                    }
                    if (commandeProduit.getDateCommandeProduit() != null) {
                        existingCommandeProduit.setDateCommandeProduit(commandeProduit.getDateCommandeProduit());
                    }
                    if (commandeProduit.getDateLastModified() != null) {
                        existingCommandeProduit.setDateLastModified(commandeProduit.getDateLastModified());
                    }

                    return existingCommandeProduit;
                }
            )
            .map(commandeProduitRepository::save);
    }

    /**
     * Get all the commandeProduits.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommandeProduit> findAll() {
        log.debug("Request to get all CommandeProduits");
        return commandeProduitRepository.findAll();
    }

    /**
     * Get one commandeProduit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandeProduit> findOne(Long id) {
        log.debug("Request to get CommandeProduit : {}", id);
        return commandeProduitRepository.findById(id);
    }

    /**
     * Delete the commandeProduit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommandeProduit : {}", id);
        commandeProduitRepository.deleteById(id);
    }
}
