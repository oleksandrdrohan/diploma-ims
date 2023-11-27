package com.gmail.iamdroal099.inventorymanagementsystem.service;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.ItemUpdateNotes;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.ItemRepository;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.ItemUpdateNotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ItemUpdateNotesService {


    private final ItemUpdateNotesRepository itemUpdateNotesRepository;


    private final ItemRepository itemRepository;

    @Autowired
    public ItemUpdateNotesService(ItemUpdateNotesRepository itemUpdateNotesRepository, ItemRepository itemRepository) {
        this.itemUpdateNotesRepository = itemUpdateNotesRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void createUpdateNote(Item item, String oldQuantityInfo, String newQuantityInfo) {
        ItemUpdateNotes itemUpdateNotes = new ItemUpdateNotes();
        Date date = new Date(System.currentTimeMillis());
        itemUpdateNotes.setCreationDate(date);
        String updatingInfo = "Quantity was change from " + oldQuantityInfo + " to " + newQuantityInfo + " at " + itemUpdateNotes.getCreationDate();
        itemUpdateNotes.setUpdateInfo(updatingInfo);
        itemUpdateNotes.setItem(item);
        item.getItemUpdateNotesList().add(itemUpdateNotes);
        itemUpdateNotesRepository.save(itemUpdateNotes);

    }

    @Transactional(readOnly = true)
    public Page<ItemUpdateNotes> getItemNotesPage(int page, int size, String article) {
        Item item = itemRepository.findItemByArticle(article);
        Pageable pageable = PageRequest.of(page, size);
        return itemUpdateNotesRepository.findAllByItem(item, pageable);
    }

}
