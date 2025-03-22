package com.bachar.law.domain;

import com.bachar.law.domain.enumeration.ClientHistorySubType;
import com.bachar.law.domain.enumeration.ClientHistoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClientHistory.
 */
@Entity
@Table(name = "client_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ClientHistoryType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type")
    private ClientHistorySubType subType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "histories", "payments", "fees" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClientHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public ClientHistory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return this.date;
    }

    public ClientHistory date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public ClientHistoryType getType() {
        return this.type;
    }

    public ClientHistory type(ClientHistoryType type) {
        this.setType(type);
        return this;
    }

    public void setType(ClientHistoryType type) {
        this.type = type;
    }

    public ClientHistorySubType getSubType() {
        return this.subType;
    }

    public ClientHistory subType(ClientHistorySubType subType) {
        this.setSubType(subType);
        return this;
    }

    public void setSubType(ClientHistorySubType subType) {
        this.subType = subType;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ClientHistory client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientHistory{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", subType='" + getSubType() + "'" +
            "}";
    }
}
