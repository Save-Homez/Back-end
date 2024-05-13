package homez.homes.entity;

import homez.homes.entity.constant.RentType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String town;

    private String name;

    private double area;

    private String type;

    private int floor;

    @Enumerated(value = EnumType.STRING)
    private RentType rentType;

    private int deposit;

    private int rental;
}
