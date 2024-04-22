package com.tst.models.entities.locationRegion;

import com.tst.models.responses.locationRegion.LocationProvinceResponse;
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

    public LocationProvinceResponse toLocationProvinceResponse() {
        return new LocationProvinceResponse()
                .setId(id)
                .setName(name)
                .setCode(code)
                .setDivisionType(divisionType)
                .setPhoneCode(phoneCode)
                ;
    }

}

