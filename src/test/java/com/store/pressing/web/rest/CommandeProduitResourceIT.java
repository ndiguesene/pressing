package com.store.pressing.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.store.pressing.IntegrationTest;
import com.store.pressing.domain.CommandeProduit;
import com.store.pressing.domain.enumeration.StatusCommandeProduit;
import com.store.pressing.repository.CommandeProduitRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommandeProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommandeProduitResourceIT {

    private static final Double DEFAULT_PRIX_AVANCE = 1D;
    private static final Double UPDATED_PRIX_AVANCE = 2D;

    private static final Double DEFAULT_PRIX_RESTANT = 1D;
    private static final Double UPDATED_PRIX_RESTANT = 2D;

    private static final StatusCommandeProduit DEFAULT_STATUS = StatusCommandeProduit.PAYER;
    private static final StatusCommandeProduit UPDATED_STATUS = StatusCommandeProduit.NONPAYER;

    private static final LocalDate DEFAULT_DATE_COMMANDE_PRODUIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMMANDE_PRODUIT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/commande-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeProduitRepository commandeProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandeProduitMockMvc;

    private CommandeProduit commandeProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandeProduit createEntity(EntityManager em) {
        CommandeProduit commandeProduit = new CommandeProduit()
            .prixAvance(DEFAULT_PRIX_AVANCE)
            .prixRestant(DEFAULT_PRIX_RESTANT)
            .status(DEFAULT_STATUS)
            .dateCommandeProduit(DEFAULT_DATE_COMMANDE_PRODUIT)
            .dateLastModified(DEFAULT_DATE_LAST_MODIFIED);
        return commandeProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandeProduit createUpdatedEntity(EntityManager em) {
        CommandeProduit commandeProduit = new CommandeProduit()
            .prixAvance(UPDATED_PRIX_AVANCE)
            .prixRestant(UPDATED_PRIX_RESTANT)
            .status(UPDATED_STATUS)
            .dateCommandeProduit(UPDATED_DATE_COMMANDE_PRODUIT)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED);
        return commandeProduit;
    }

    @BeforeEach
    public void initTest() {
        commandeProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createCommandeProduit() throws Exception {
        int databaseSizeBeforeCreate = commandeProduitRepository.findAll().size();
        // Create the CommandeProduit
        restCommandeProduitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isCreated());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeCreate + 1);
        CommandeProduit testCommandeProduit = commandeProduitList.get(commandeProduitList.size() - 1);
        assertThat(testCommandeProduit.getPrixAvance()).isEqualTo(DEFAULT_PRIX_AVANCE);
        assertThat(testCommandeProduit.getPrixRestant()).isEqualTo(DEFAULT_PRIX_RESTANT);
        assertThat(testCommandeProduit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCommandeProduit.getDateCommandeProduit()).isEqualTo(DEFAULT_DATE_COMMANDE_PRODUIT);
        assertThat(testCommandeProduit.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void createCommandeProduitWithExistingId() throws Exception {
        // Create the CommandeProduit with an existing ID
        commandeProduit.setId(1L);

        int databaseSizeBeforeCreate = commandeProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeProduitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommandeProduits() throws Exception {
        // Initialize the database
        commandeProduitRepository.saveAndFlush(commandeProduit);

        // Get all the commandeProduitList
        restCommandeProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandeProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].prixAvance").value(hasItem(DEFAULT_PRIX_AVANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].prixRestant").value(hasItem(DEFAULT_PRIX_RESTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCommandeProduit").value(hasItem(DEFAULT_DATE_COMMANDE_PRODUIT.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getCommandeProduit() throws Exception {
        // Initialize the database
        commandeProduitRepository.saveAndFlush(commandeProduit);

        // Get the commandeProduit
        restCommandeProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, commandeProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commandeProduit.getId().intValue()))
            .andExpect(jsonPath("$.prixAvance").value(DEFAULT_PRIX_AVANCE.doubleValue()))
            .andExpect(jsonPath("$.prixRestant").value(DEFAULT_PRIX_RESTANT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateCommandeProduit").value(DEFAULT_DATE_COMMANDE_PRODUIT.toString()))
            .andExpect(jsonPath("$.dateLastModified").value(DEFAULT_DATE_LAST_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommandeProduit() throws Exception {
        // Get the commandeProduit
        restCommandeProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommandeProduit() throws Exception {
        // Initialize the database
        commandeProduitRepository.saveAndFlush(commandeProduit);

        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();

        // Update the commandeProduit
        CommandeProduit updatedCommandeProduit = commandeProduitRepository.findById(commandeProduit.getId()).get();
        // Disconnect from session so that the updates on updatedCommandeProduit are not directly saved in db
        em.detach(updatedCommandeProduit);
        updatedCommandeProduit
            .prixAvance(UPDATED_PRIX_AVANCE)
            .prixRestant(UPDATED_PRIX_RESTANT)
            .status(UPDATED_STATUS)
            .dateCommandeProduit(UPDATED_DATE_COMMANDE_PRODUIT)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED);

        restCommandeProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommandeProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommandeProduit))
            )
            .andExpect(status().isOk());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
        CommandeProduit testCommandeProduit = commandeProduitList.get(commandeProduitList.size() - 1);
        assertThat(testCommandeProduit.getPrixAvance()).isEqualTo(UPDATED_PRIX_AVANCE);
        assertThat(testCommandeProduit.getPrixRestant()).isEqualTo(UPDATED_PRIX_RESTANT);
        assertThat(testCommandeProduit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommandeProduit.getDateCommandeProduit()).isEqualTo(UPDATED_DATE_COMMANDE_PRODUIT);
        assertThat(testCommandeProduit.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingCommandeProduit() throws Exception {
        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();
        commandeProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommandeProduit() throws Exception {
        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();
        commandeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommandeProduit() throws Exception {
        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();
        commandeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeProduitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandeProduitWithPatch() throws Exception {
        // Initialize the database
        commandeProduitRepository.saveAndFlush(commandeProduit);

        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();

        // Update the commandeProduit using partial update
        CommandeProduit partialUpdatedCommandeProduit = new CommandeProduit();
        partialUpdatedCommandeProduit.setId(commandeProduit.getId());

        partialUpdatedCommandeProduit.dateCommandeProduit(UPDATED_DATE_COMMANDE_PRODUIT);

        restCommandeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandeProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandeProduit))
            )
            .andExpect(status().isOk());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
        CommandeProduit testCommandeProduit = commandeProduitList.get(commandeProduitList.size() - 1);
        assertThat(testCommandeProduit.getPrixAvance()).isEqualTo(DEFAULT_PRIX_AVANCE);
        assertThat(testCommandeProduit.getPrixRestant()).isEqualTo(DEFAULT_PRIX_RESTANT);
        assertThat(testCommandeProduit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCommandeProduit.getDateCommandeProduit()).isEqualTo(UPDATED_DATE_COMMANDE_PRODUIT);
        assertThat(testCommandeProduit.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateCommandeProduitWithPatch() throws Exception {
        // Initialize the database
        commandeProduitRepository.saveAndFlush(commandeProduit);

        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();

        // Update the commandeProduit using partial update
        CommandeProduit partialUpdatedCommandeProduit = new CommandeProduit();
        partialUpdatedCommandeProduit.setId(commandeProduit.getId());

        partialUpdatedCommandeProduit
            .prixAvance(UPDATED_PRIX_AVANCE)
            .prixRestant(UPDATED_PRIX_RESTANT)
            .status(UPDATED_STATUS)
            .dateCommandeProduit(UPDATED_DATE_COMMANDE_PRODUIT)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED);

        restCommandeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandeProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandeProduit))
            )
            .andExpect(status().isOk());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
        CommandeProduit testCommandeProduit = commandeProduitList.get(commandeProduitList.size() - 1);
        assertThat(testCommandeProduit.getPrixAvance()).isEqualTo(UPDATED_PRIX_AVANCE);
        assertThat(testCommandeProduit.getPrixRestant()).isEqualTo(UPDATED_PRIX_RESTANT);
        assertThat(testCommandeProduit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommandeProduit.getDateCommandeProduit()).isEqualTo(UPDATED_DATE_COMMANDE_PRODUIT);
        assertThat(testCommandeProduit.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingCommandeProduit() throws Exception {
        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();
        commandeProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandeProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommandeProduit() throws Exception {
        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();
        commandeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommandeProduit() throws Exception {
        int databaseSizeBeforeUpdate = commandeProduitRepository.findAll().size();
        commandeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommandeProduit in the database
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommandeProduit() throws Exception {
        // Initialize the database
        commandeProduitRepository.saveAndFlush(commandeProduit);

        int databaseSizeBeforeDelete = commandeProduitRepository.findAll().size();

        // Delete the commandeProduit
        restCommandeProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, commandeProduit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommandeProduit> commandeProduitList = commandeProduitRepository.findAll();
        assertThat(commandeProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
