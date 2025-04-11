package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "rooms", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Room extends BaseEntity {

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String type;
}
