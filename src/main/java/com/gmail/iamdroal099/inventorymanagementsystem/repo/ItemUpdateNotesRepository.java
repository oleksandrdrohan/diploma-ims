package com.gmail.iamdroal099.inventorymanagementsystem.repo;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.ItemUpdateNotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemUpdateNotesRepository extends JpaRepository<ItemUpdateNotes, Long> {

    Page<ItemUpdateNotes> findAllByItem(Item item, Pageable pageable);
}
