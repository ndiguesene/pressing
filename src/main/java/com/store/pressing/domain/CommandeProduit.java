package com.store.pressing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store.pressing.domain.enumeration.StatusCommandeProduit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The CommandeProduit entity.\n@author Ndigue Sene
 */
@ApiModel(description = "The CommandeProduit entity.\n@author Ndigue Sene")
@Entity
@Table(name = "commande_produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandeProduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * fieldName
     */
    @ApiModelProperty(value = "fieldName")
    @Column(name = "prix_avance")
    private Double prixAvance;

    @Column(name = "prix_restant")
    private Double prixRestant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCommandeProduit status;

    @Column(name = "date_commande_produit")
    private LocalDate dateCommandeProduit;

    @Column(name = "date_last_modified")
    private LocalDate dateLastModified;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_commande_produit__produit",
        joinColumns = @JoinColumn(name = "commande_produit_id"),
        inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    @JsonIgnoreProperties(value = { "categorie", "commandeProduits" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "commandeProduits" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommandeProduit id(Long id) {
        this.id = id;
        return this;
    }

    public Double getPrixAvance() {
        return this.prixAvance;
    }

    public CommandeProduit prixAvance(Double prixAvance) {
        this.prixAvance = prixAvance;
        return this;
    }

    public void setPrixAvance(Double prixAvance) {
        this.prixAvance = prixAvance;
    }

    public Double getPrixRestant() {
        return this.prixRestant;
    }

    public CommandeProduit prixRestant(Double prixRestant) {
        this.prixRestant = prixRestant;
        return this;
    }

    public void setPrixRestant(Double prixRestant) {
        this.prixRestant = prixRestant;
    }

    public StatusCommandeProduit getStatus() {
        return this.status;
    }

    public CommandeProduit status(StatusCommandeProduit status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusCommandeProduit status) {
        this.status = status;
    }

    public LocalDate getDateCommandeProduit() {
        return this.dateCommandeProduit;
    }

    public CommandeProduit dateCommandeProduit(LocalDate dateCommandeProduit) {
        this.dateCommandeProduit = dateCommandeProduit;
        return this;
    }

    public void setDateCommandeProduit(LocalDate dateCommandeProduit) {
        this.dateCommandeProduit = dateCommandeProduit;
    }

    public LocalDate getDateLastModified() {
        return this.dateLastModified;
    }

    public CommandeProduit dateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
        return this;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public CommandeProduit produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public CommandeProduit addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getCommandeProduits().add(this);
        return this;
    }

    public CommandeProduit removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getCommandeProduits().remove(this);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Client getClient() {
        return this.client;
    }

    public CommandeProduit client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeProduit)) {
            return false;
        }
        return id != null && id.equals(((CommandeProduit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeProduit{" +
            "id=" + getId() +
            ", prixAvance=" + getPrixAvance() +
            ", prixRestant=" + getPrixRestant() +
            ", status='" + getStatus() + "'" +
            ", dateCommandeProduit='" + getDateCommandeProduit() + "'" +
            ", dateLastModified='" + getDateLastModified() + "'" +
            "}";
    }
}
