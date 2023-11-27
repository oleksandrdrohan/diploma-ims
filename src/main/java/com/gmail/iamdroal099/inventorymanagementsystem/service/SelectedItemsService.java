package com.gmail.iamdroal099.inventorymanagementsystem.service;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Order;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.SelectedItems;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.SelectedItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelectedItemsService {

    private final SelectedItemsRepository selectedItemsRepository;

    @Autowired
    public SelectedItemsService(SelectedItemsRepository selectedItemsRepository) {
        this.selectedItemsRepository = selectedItemsRepository;
    }

    @Transactional
    public void save(SelectedItems selectedItems){
        selectedItemsRepository.save(selectedItems);
    }

    @Transactional
    public void delete(SelectedItems selectedItems){
        selectedItemsRepository.delete(selectedItems);
    }

    @Transactional(readOnly = true)
    public SelectedItems getLastSelectedItem(){
        return selectedItemsRepository.findLatestSelectedItems();
    }

}
