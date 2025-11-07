package com.gasmapp.gasmapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "evaluations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"price_id", "client_id"})
)
public class EvaluationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean evaluation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "price_id", nullable = false)
    private PriceModel price;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private ClientModel client;

    public EvaluationModel() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Boolean getEvaluation() { return evaluation; }
    public void setEvaluation(Boolean evaluation) { this.evaluation = evaluation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public PriceModel getPrice() { return price; }
    public void setPrice(PriceModel price) { this.price = price; }

    public ClientModel getClient() { return client; }
    public void setClient(ClientModel client) { this.client = client; }
}
