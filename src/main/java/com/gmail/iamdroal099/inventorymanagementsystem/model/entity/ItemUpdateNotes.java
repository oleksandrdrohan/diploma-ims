package com.gmail.iamdroal099.inventorymanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "item_update_notes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemUpdateNotes {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "update_info")
    private String updateInfo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Item item;
}
