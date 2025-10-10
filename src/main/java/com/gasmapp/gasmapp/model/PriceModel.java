package com.gasmapp.gasmapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prices")
public class PriceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "fuel_id", nullable = false)
    private FuelModel fuel;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientModel client;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationModel> evaluations = new ArrayList<>();

    public PriceModel() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public FuelModel getFuel() { return fuel; }
    public void setFuel(FuelModel fuel) { this.fuel = fuel; }

    public ClientModel getClient() { return client; }
    public void setClient(ClientModel client) { this.client = client; }

    public void addEvaluation(EvaluationModel evaluation) {
        evaluations.add(evaluation);
        evaluation.setPrice(this);
    }

    public void removeEvaluation(EvaluationModel evaluation) {
        evaluations.remove(evaluation);
        evaluation.setPrice(null);
    }
}
