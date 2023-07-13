package cz.pwc.borders.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Country {

    public Country(String cca3) {
        this.cca3 = cca3;
    }

    public Country() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private String cca3;

}
