package com.gasmapp.gasmapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fuels")
public class FuelModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "gas_station_id", nullable = false)
    private GasStationModel gasStation;

    @OneToMany(mappedBy = "fuel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceModel> prices = new ArrayList<>();

    public FuelModel() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public GasStationModel getGasStation() { return gasStation; }
    public void setGasStation(GasStationModel gasStation) { this.gasStation = gasStation; }
}
