package com.gmail.iamdroal099.inventorymanagementsystem.model.entity;

import com.gmail.iamdroal099.inventorymanagementsystem.model.enums.OrderStatus;
import com.gmail.iamdroal099.inventorymanagementsystem.model.enums.OrderType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "total_amount_of_items")
    private int totalAmountOfItems;

    @Column(name = "cnn_number")
    private String consignmentNoteNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;


    @Column(name = "incoming_date")
    private String incomingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.IN_PROCESSING;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType type = OrderType.SUPPLY;

    @ElementCollection
    @CollectionTable(name = "selected_quantity_ids", joinColumns = @JoinColumn(name = "selected_items_id"))
    @Column(name = "item_id")
    private List<Integer> selectedQuantityForOrder = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "selected_items_id")
    private SelectedItems selectedItems;

    @ManyToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<Item> itemsSet = new ArrayList<>();

}
