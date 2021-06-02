package com.store.pressing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store.pressing.domain.enumeration.StatusCategorie;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Categorie entity.\n@author Ndigue Sene
 */
@ApiModel(description = "The Categorie entity.\n@author Ndigue Sene")
@Entity
@Table(name = "categorie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * fieldName
     */
    @NotNull
    @ApiModelProperty(value = "fieldName", required = true)
    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCategorie status;

    @OneToMany(mappedBy = "categorie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categorie", "commandeProduits" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categorie id(Long id) {
        this.id = id;
        return this;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Categorie designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return this.description;
    }

    public Categorie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusCategorie getStatus() {
        return this.status;
    }

    public Categorie status(StatusCategorie status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusCategorie status) {
        this.status = status;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public Categorie produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public Categorie addProduit(Produit produit) {
        this.produits.add(produit);
        produit.setCategorie(this);
        return this;
    }

    public Categorie removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.setCategorie(null);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.setCategorie(null));
        }
        if (produits != null) {
            produits.forEach(i -> i.setCategorie(this));
        }
        this.produits = produits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categorie)) {
            return false;
        }
        return id != null && id.equals(((Categorie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categorie{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
