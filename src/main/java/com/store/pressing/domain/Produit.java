package com.store.pressing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store.pressing.domain.enumeration.Taille;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Produit entity.\n@author Ndigue Sene
 */
@ApiModel(description = "The Produit entity.\n@author Ndigue Sene")
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Produit implements Serializable {

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

    @NotNull
    @Column(name = "prix", nullable = false)
    private Double prix;

    @Column(name = "code_produit")
    private String codeProduit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "taille_produit", nullable = false)
    private Taille tailleProduit;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produits" }, allowSetters = true)
    private Categorie categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit id(Long id) {
        this.id = id;
        return this;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Produit designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Produit prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getCodeProduit() {
        return this.codeProduit;
    }

    public Produit codeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
        return this;
    }

    public void setCodeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
    }

    public Taille getTailleProduit() {
        return this.tailleProduit;
    }

    public Produit tailleProduit(Taille tailleProduit) {
        this.tailleProduit = tailleProduit;
        return this;
    }

    public void setTailleProduit(Taille tailleProduit) {
        this.tailleProduit = tailleProduit;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Produit image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Produit imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public Produit categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prix=" + getPrix() +
            ", codeProduit='" + getCodeProduit() + "'" +
            ", tailleProduit='" + getTailleProduit() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
