package com.sms.multitenantschool.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses", schema = "public")
public class Address extends BaseEntity {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;



}