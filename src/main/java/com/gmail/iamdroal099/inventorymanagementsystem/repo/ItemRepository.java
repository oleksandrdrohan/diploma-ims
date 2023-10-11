package com.gmail.iamdroal099.inventorymanagementsystem.repo;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemByArticleAndColorAndItemName(String article, String color, String itemName);

    Item findItemByArticle(String article);

}
