package com.gmail.iamdroal099.inventorymanagementsystem.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "selected_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SelectedItems {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @CollectionTable(name = "selected_item_ids", joinColumns = @JoinColumn(name = "selected_items_id"))
    @Column(name = "item_id")
    private List<Long> selectedItemsForOrder = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "token_id")
    private Token token;
}
