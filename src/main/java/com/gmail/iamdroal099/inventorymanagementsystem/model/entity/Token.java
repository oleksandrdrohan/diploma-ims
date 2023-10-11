package com.gmail.iamdroal099.inventorymanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "token_name")
    String tokenName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "selected_items_id")
    private SelectedItems selectedItems;
}
