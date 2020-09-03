package com.space.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import java.util.Date;
@Entity
@JsonAutoDetect
@Table(name = "ship")
public class Ship {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ship ID
    @Column(name = "name",length = 50)
    private String name; // ship name max size = 50;
    @Column(name = "planet", length = 50)
    private String planet; // departure planet max size = 50;
    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType; // ship type ( Enum class on project)
    @Column(name = "prodDate")
    private Date prodDate; // Date of issue  between 2800 - 3019 year
    @Column(name = "isUsed")
    private Boolean isUsed;// used or new ship
    @Column(name = "speed")
    private Double speed;//  max ship speed between 0.01 - 0.99 need use round to 2 digit
    @Column(name = "crewSize")
    private Integer crewSize; // crew size between 1 - 9999 members
    @Column(name = "rating")
    private Double rating; // ship rating round to 2 digit

    public Ship() {

    }
    //for rating need use formula R = (80*v*k)/(y0-y1 + 1),
    // v - speed,
    // k -(1 if ship new/ 0.5 if ship was use)
    //y0 - current year (for project is 3019)
    //y1 - date of issue ship


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Ship(Long id, String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

}
