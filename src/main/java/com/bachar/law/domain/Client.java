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
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "phone")
    private String phone;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<ClientHistory> histories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "linkedFees", "linkedTos", "client", "agreement" }, allowSetters = true)
    private Set<Fee> fees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public Client address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return this.description;
    }

    public Client description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Client fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Client lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPhone() {
        return this.phone;
    }

    public Client phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Client active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ClientHistory> getHistories() {
        return this.histories;
    }

    public void setHistories(Set<ClientHistory> clientHistories) {
        if (this.histories != null) {
            this.histories.forEach(i -> i.setClient(null));
        }
        if (clientHistories != null) {
            clientHistories.forEach(i -> i.setClient(this));
        }
        this.histories = clientHistories;
    }

    public Client histories(Set<ClientHistory> clientHistories) {
        this.setHistories(clientHistories);
        return this;
    }

    public Client addHistories(ClientHistory clientHistory) {
        this.histories.add(clientHistory);
        clientHistory.setClient(this);
        return this;
    }

    public Client removeHistories(ClientHistory clientHistory) {
        this.histories.remove(clientHistory);
        clientHistory.setClient(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setClient(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setClient(this));
        }
        this.payments = payments;
    }

    public Client payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Client addPayments(Payment payment) {
        this.payments.add(payment);
        payment.setClient(this);
        return this;
    }

    public Client removePayments(Payment payment) {
        this.payments.remove(payment);
        payment.setClient(null);
        return this;
    }

    public Set<Fee> getFees() {
        return this.fees;
    }

    public void setFees(Set<Fee> fees) {
        if (this.fees != null) {
            this.fees.forEach(i -> i.setClient(null));
        }
        if (fees != null) {
            fees.forEach(i -> i.setClient(this));
        }
        this.fees = fees;
    }

    public Client fees(Set<Fee> fees) {
        this.setFees(fees);
        return this;
    }

    public Client addFees(Fee fee) {
        this.fees.add(fee);
        fee.setClient(this);
        return this;
    }

    public Client removeFees(Fee fee) {
        this.fees.remove(fee);
        fee.setClient(null);
        return this;
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
        return getId() != null && getId().equals(((Client) o).getId());
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
            ", address='" + getAddress() + "'" +
            ", description='" + getDescription() + "'" +
            ", email='" + getEmail() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", phone='" + getPhone() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
