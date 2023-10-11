package com.gmail.iamdroal099.inventorymanagementsystem.model.POJO;

import lombok.Data;

@Data
public class QuantityUpdateRequest {
    private Long itemId;
    private Integer quantityValue;

}
