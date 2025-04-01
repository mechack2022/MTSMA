package com.sms.multitenantschool.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.multitenantschool.Utils.SecurityUtils.SecurityUtil;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "base_organisation_unit")
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationUnit extends BaseEntity implements Serializable {

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
    @Column(name = "organisation_unit_level_id")
    private Long organisationUnitLevelId;

    @Basic
    @Column(name = "parent_organisation_unit_id")
    private Long parentOrganisationUnitId;

    @Basic
    @Column(name = "archived")
    @JsonIgnore
    private Integer archived = 0;

    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    @ToString.Exclude
    private String createdBy = SecurityUtil.getCurrentUserLogin().orElse(null);

    @Column(name = "date_created", nullable = false, updatable = false)
    @JsonIgnore
    @ToString.Exclude
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "modified_by")
    @JsonIgnore
    @ToString.Exclude
    private String modifiedBy = SecurityUtil.getCurrentUserLogin().orElse(null);

    @Column(name = "date_modified")
    @JsonIgnore
    @ToString.Exclude
    private LocalDateTime dateModified = LocalDateTime.now();

    @Transient
    private String parentOrganisationUnitName;

    @Transient
    private String parentParentOrganisationUnitName;

    @OneToMany(mappedBy = "tenantUuid")
    @JsonIgnore
    @ToString.Exclude
    public List<User> users;

    @Basic
    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "organisation_unit_level_id", referencedColumnName = "id", updatable = false, insertable = false)
    public OrganisationUnitLevel organisationUnitLevelByOrganisationUnitLevelId;

    public OrganisationUnit(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}