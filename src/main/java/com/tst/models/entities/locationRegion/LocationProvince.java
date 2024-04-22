package com.tst.models.entities.locationRegion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "location_provinces")
public class LocationProvince {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false)
    private String name;

    @Column(nullable= false)
    private String code;

    @Column(name = "division_type", nullable= false)
    private String divisionType;

    @Column(name = "phone_code")
    private String phoneCode;

}

