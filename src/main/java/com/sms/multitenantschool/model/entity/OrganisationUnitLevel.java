package com.sms.multitenantschool.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.multitenantschool.Utils.SecurityUtils.SecurityUtil;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "base_organisation_unit_level")
public class OrganisationUnitLevel implements Serializable {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "archived")
    private Integer archived;
    @Basic
    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "organisationUnitLevelByOrganisationUnitLevelId")
    @ToString.Exclude
    @JsonIgnore
    public List<OrganisationUnit> organisationUnitsById;

    @ManyToOne
    @JoinColumn(name = "parent_organisation_unit_level_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    @ToString.Exclude
    private OrganisationUnitLevel parentOrganisationUnitLevelIdByOrganisationUnitLevelId;

}
