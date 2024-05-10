package homez.homes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class TownGraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String town;

    private String pharmacy;
    private String womenWelfare;
    private String education;
    private String culture;
    private String cinema;
    private String art;
    private String concert;
    private String park;
    private String library;
    private String green;
    private String noise;
    private String air;
    private String water;
    private String safety;
    private String clean;
    private String parking;
}
