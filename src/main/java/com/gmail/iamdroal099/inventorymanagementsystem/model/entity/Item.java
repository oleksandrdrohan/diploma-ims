package com.gmail.iamdroal099.inventorymanagementsystem.model.entity;

import com.gmail.iamdroal099.inventorymanagementsystem.model.enums.ItemAvailableStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "article", nullable = false)
    private String article;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updating_date")
    @UpdateTimestamp
    private Date updatingDate;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "color")
    private String color;

    @Column(name = "characteristics")
    private String characteristics;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantity;

    @Column(name = "quantity_for_order")
    private Integer quantityForOrder = 0;


    @Enumerated(EnumType.STRING)
    @Column(name = "is_available")
    private ItemAvailableStatus ias = ItemAvailableStatus.OUT_OF_STOCK;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ITEM_ORDER_TABLE",
            joinColumns = {
                    @JoinColumn(name = "item_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "order_id", referencedColumnName = "id")
            })
    private List<Order> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinTable(name = "ITEM_UPDATE_NOTES_TABLE",
            joinColumns = {
                    @JoinColumn(name = "item_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "location_id", referencedColumnName = "id")
            })
    private List<ItemUpdateNotes> itemUpdateNotesList;
}
