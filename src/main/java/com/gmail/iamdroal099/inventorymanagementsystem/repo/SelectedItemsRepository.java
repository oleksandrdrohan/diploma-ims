package com.gmail.iamdroal099.inventorymanagementsystem.repo;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.SelectedItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SelectedItemsRepository extends JpaRepository<SelectedItems, Long> {

    @Query("SELECT si FROM SelectedItems si WHERE si.id = (SELECT MAX(id) FROM SelectedItems)")
    SelectedItems findLatestSelectedItems();

}
