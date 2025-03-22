package com.bachar.law.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fee.
 */
@Entity
@Table(name = "fee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "adjusted_value")
    private String adjustedValue;

    @Column(name = "adjusted_value_plus")
    private String adjustedValuePlus;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "value")
    private Float value;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_fee__linked_fees",
        joinColumns = @JoinColumn(name = "fee_id"),
        inverseJoinColumns = @JoinColumn(name = "linked_fees_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "linkedFees", "linkedTos", "client", "agreement" }, allowSetters = true)
    private Set<Fee> linkedFees = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "linkedFees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "linkedFees", "linkedTos", "client", "agreement" }, allowSetters = true)
    private Set<Fee> linkedTos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "histories", "payments", "fees" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fees" }, allowSetters = true)
    private Agreement agreement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Fee active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAdjustedValue() {
        return this.adjustedValue;
    }

    public Fee adjustedValue(String adjustedValue) {
        this.setAdjustedValue(adjustedValue);
        return this;
    }

    public void setAdjustedValue(String adjustedValue) {
        this.adjustedValue = adjustedValue;
    }

    public String getAdjustedValuePlus() {
        return this.adjustedValuePlus;
    }

    public Fee adjustedValuePlus(String adjustedValuePlus) {
        this.setAdjustedValuePlus(adjustedValuePlus);
        return this;
    }

    public void setAdjustedValuePlus(String adjustedValuePlus) {
        this.adjustedValuePlus = adjustedValuePlus;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Fee amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public Fee description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public Fee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Fee startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Float getValue() {
        return this.value;
    }

    public Fee value(Float value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Set<Fee> getLinkedFees() {
        return this.linkedFees;
    }

    public void setLinkedFees(Set<Fee> fees) {
        this.linkedFees = fees;
    }

    public Fee linkedFees(Set<Fee> fees) {
        this.setLinkedFees(fees);
        return this;
    }

    public Fee addLinkedFees(Fee fee) {
        this.linkedFees.add(fee);
        return this;
    }

    public Fee removeLinkedFees(Fee fee) {
        this.linkedFees.remove(fee);
        return this;
    }

    public Set<Fee> getLinkedTos() {
        return this.linkedTos;
    }

    public void setLinkedTos(Set<Fee> fees) {
        if (this.linkedTos != null) {
            this.linkedTos.forEach(i -> i.removeLinkedFees(this));
        }
        if (fees != null) {
            fees.forEach(i -> i.addLinkedFees(this));
        }
        this.linkedTos = fees;
    }

    public Fee linkedTos(Set<Fee> fees) {
        this.setLinkedTos(fees);
        return this;
    }

    public Fee addLinkedTo(Fee fee) {
        this.linkedTos.add(fee);
        fee.getLinkedFees().add(this);
        return this;
    }

    public Fee removeLinkedTo(Fee fee) {
        this.linkedTos.remove(fee);
        fee.getLinkedFees().remove(this);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Fee client(Client client) {
        this.setClient(client);
        return this;
    }

    public Agreement getAgreement() {
        return this.agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Fee agreement(Agreement agreement) {
        this.setAgreement(agreement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fee)) {
            return false;
        }
        return getId() != null && getId().equals(((Fee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fee{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", adjustedValue='" + getAdjustedValue() + "'" +
            ", adjustedValuePlus='" + getAdjustedValuePlus() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
