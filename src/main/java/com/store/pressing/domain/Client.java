package com.store.pressing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * The Client entity.\n@author Ndigue Sene
 */
@ApiModel(description = "The Client entity.\n@author Ndigue Sene")
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * fieldName
     */
    @ApiModelProperty(value = "fieldName")
    @Column(name = "telephone")
    private String telephone;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "produits", "client" }, allowSetters = true)
    private Set<CommandeProduit> commandeProduits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client id(Long id) {
        this.id = id;
        return this;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Client telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUser() {
        return this.user;
    }

    public Client user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CommandeProduit> getCommandeProduits() {
        return this.commandeProduits;
    }

    public Client commandeProduits(Set<CommandeProduit> commandeProduits) {
        this.setCommandeProduits(commandeProduits);
        return this;
    }

    public Client addCommandeProduit(CommandeProduit commandeProduit) {
        this.commandeProduits.add(commandeProduit);
        commandeProduit.setClient(this);
        return this;
    }

    public Client removeCommandeProduit(CommandeProduit commandeProduit) {
        this.commandeProduits.remove(commandeProduit);
        commandeProduit.setClient(null);
        return this;
    }

    public void setCommandeProduits(Set<CommandeProduit> commandeProduits) {
        if (this.commandeProduits != null) {
            this.commandeProduits.forEach(i -> i.setClient(null));
        }
        if (commandeProduits != null) {
            commandeProduits.forEach(i -> i.setClient(this));
        }
        this.commandeProduits = commandeProduits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
