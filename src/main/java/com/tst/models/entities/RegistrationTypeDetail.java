package com.tst.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "registration_type_details",
    uniqueConstraints = {
            @UniqueConstraint(name = "unique_code_registration_type_id", columnNames = {"code", "registration_type_id"})
    }
)
public class RegistrationTypeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 5)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "registration_type_id", nullable = false)
    private RegistrationType registrationType;
}
