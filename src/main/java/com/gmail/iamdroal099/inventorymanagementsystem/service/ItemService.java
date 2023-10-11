package com.gmail.iamdroal099.inventorymanagementsystem.service;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import com.gmail.iamdroal099.inventorymanagementsystem.model.enums.ItemAvailableStatus;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private  final ItemUpdateNotesService itemUpdateNotesService;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemUpdateNotesService itemUpdateNotesService) {
        this.itemRepository = itemRepository;
        this.itemUpdateNotesService = itemUpdateNotesService;
    }

    @Transactional
    public void deleteItem(Long itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }

        itemRepository.deleteById(itemId);
    }

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void saveAll(List <Item> items) {
        itemRepository.saveAll(items);
    }

    public Item findByArticleAndColorAndItemName(Item item) {
        return itemRepository.findItemByArticleAndColorAndItemName(item.getArticle(), item.getColor(), item.getItemName());
    }

    public Page<Item> getItemsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    public Item findById(Long id){
        return itemRepository.findById(id).orElseThrow();
    }


    public Item areInStock(Item item) {
        if (item.getQuantity() > 0) {
            item.setIas(ItemAvailableStatus.IN_STOCK);
        } else {
            item.setIas(ItemAvailableStatus.OUT_OF_STOCK);
        }
        return item;
    }

    public void changeQuantityInStockForSupply(Item item, Integer supplyQuantity){
        String oldQuantityInfo = item.getQuantity().toString();
        Integer newQuantity = item.getQuantity() + supplyQuantity;
        item.setQuantity(newQuantity);
        item.setQuantityForOrder(0);
        areInStock(item);
        itemRepository.save(item);
        itemUpdateNotesService.createUpdateNote(item, oldQuantityInfo, newQuantity.toString());
    }

    public void changeQuantityInStockForShipment(Item item, Integer shipmentQuantity){
        String oldQuantityInfo = item.getQuantity().toString();
        Integer newQuantity = item.getQuantity() - shipmentQuantity;
        item.setQuantity(newQuantity);
        item.setQuantityForOrder(0);
        areInStock(item);
        itemRepository.save(item);
        itemUpdateNotesService.createUpdateNote(item, oldQuantityInfo, newQuantity.toString());
    }

    public Item findByArticle(String article) {
        return itemRepository.findItemByArticle(article);
    }

    public List<Item>  findAllItems(){
        return itemRepository.findAll();
    }

}
