package com.gasmapp.gasmapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "fuels",
        uniqueConstraints = @UniqueConstraint(columnNames = {"gas_station_id", "name"})
)
public class FuelModel {

    public enum FuelType {
        DIESEL,
        GASOLINA,
        ETANOL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "gas_station_id", nullable = false)
    @JsonBackReference
    private GasStationModel gasStation;

    @OneToMany(mappedBy = "fuel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "fuel-prices")
    private List<PriceModel> prices = new ArrayList<>();

    public FuelModel() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FuelType getName() { return name; }
    public void setName(FuelType name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public GasStationModel getGasStation() { return gasStation; }
    public void setGasStation(GasStationModel gasStation) { this.gasStation = gasStation; }

    public List<PriceModel> getPrices() { return prices; }
    public void setPrices(List<PriceModel> prices) { this.prices = prices; }
}
