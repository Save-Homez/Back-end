package homez.homes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String town;

    private double pharmacy;
    private double womenWelfare;
    private double education;
    private double culture;
    private double movie;
    private double art;
    private double park;
    private double library;
    private double green;
    private double noise;
    private double air;
    private double rest;
    private double water;
    private double safety;
    private double clean;
    private double parking;
}
