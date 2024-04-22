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
@Table(name = "location_wards")
public class LocationWard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false)
    private String name;

    @Column(nullable= false)
    private String code;

    @Column(name = "division_type", nullable= false)
    private String divisionType;

    @Column(name = "short_code", nullable= false)
    private String shortCode;

    @ManyToOne
    @JoinColumn(name = "location_district_id")
    private LocationDistrict locationDistrict;
}
