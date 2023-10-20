package com.gmail.iamdroal099.inventorymanagementsystem.controller;


import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.ItemUpdateNotes;
import com.gmail.iamdroal099.inventorymanagementsystem.model.POJO.ExcelItemExport;
import com.gmail.iamdroal099.inventorymanagementsystem.service.ItemService;
import com.gmail.iamdroal099.inventorymanagementsystem.service.ItemUpdateNotesService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.List;


@Controller
@Data
public class ItemController {


    private final ItemService itemService;


    private final ItemUpdateNotesService itemUpdateNotesService;


    @GetMapping("add")
    public String showAddItemPage(Model model) {
        model.addAttribute("item", new Item());
        return "/add";
    }

    @PostMapping("add")
    public String addItem(@ModelAttribute("item") Item item, Model model) {
        Item itemInSQL = itemService.findByArticleAndColorAndItemName(item);

        if (itemInSQL != null) {
            model.addAttribute("error", "Item already in storage");
            return "/add";
        } else {

            Item newItem = new Item();
            newItem.setArticle(item.getArticle());
            newItem.setItemName(item.getItemName());
            newItem.setColor(item.getColor());
            newItem.setCharacteristics(item.getCharacteristics());
            newItem.setCost(item.getCost());
            newItem.setQuantity(item.getQuantity());
            if (newItem.getArticle() == null ||
                newItem.getItemName() == null ||
                newItem.getColor() == null ||
                newItem.getCharacteristics() == null ||
                newItem.getCost() == null ||
                newItem.getQuantity() == null){
                model.addAttribute("error_fields", "You must fill the fields");
                return "/add";
            }
            itemService.areInStock(newItem);

            itemService.save(newItem);
            return "redirect:/items-list";
        }
    }

    @GetMapping("/items-list")
    public String listItems(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Item> itemsPage = itemService.getItemsPage(page, size);
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("itemsPage", itemsPage);
        return "items-list";
    }

    @PostMapping("/delete-items")
    public ResponseEntity<String> deleteItems(@RequestParam(name = "itemIds") List<Long> itemIds) {
        for (Long itemId : itemIds) {
            itemService.deleteItem(itemId);
        }
        return ResponseEntity.ok("Items deleted successfully");
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable(value = "id") Long itemId, Model model) {
        Item item = itemService.findById(itemId);

        if (item == null) {
            return "redirect:/item-not-found";
        } else {
            model.addAttribute("item", item);
        }

        return "update";
    }

    @PostMapping("/update-item/{id}")
    public String updateItem(@PathVariable(value = "id") Long id, @RequestParam String article,
                             @RequestParam String itemName,
                             @RequestParam String color,
                             @RequestParam String characteristics,
                             @RequestParam Double cost,
                             @RequestParam Integer quantity) {
        Item item = itemService.findById(id);
        if (quantity < 0){
            return "redirect:/quantity-less-than-zero-error";
        }
        itemUpdateNotesService.createUpdateNote(item, item.getQuantity().toString(), quantity.toString());
        item.setArticle(article);
        item.setItemName(itemName);
        item.setColor(color);
        item.setCharacteristics(characteristics);
        item.setCost(cost);
        item.setQuantity(quantity);
        itemService.areInStock(item);
        itemService.save(item);
        return "redirect:/items-list";
    }

    @GetMapping("/item/{article}")
    public String itemDetails(@PathVariable("article") String article, Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size) {

        Item item = itemService.findByArticle(article);
        Page<ItemUpdateNotes> itemsUpdateNotesPage = itemUpdateNotesService.getItemNotesPage(page, size, article);

        if (item != null) {
            model.addAttribute("item", item);
            model.addAttribute("itemsUpdateNotes", itemsUpdateNotesPage.getContent());
            model.addAttribute("itemsUpdateNotesPage", itemsUpdateNotesPage);
            return "item";
        } else {
            return "/item-not-found";
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query) {
        Item item = itemService.findByArticle(query);
        String article = item.getArticle();
        return "redirect:/item/{article}";
    }

    @GetMapping("/items-excel-export")
    public ResponseEntity<byte[]> exportItemsToExcel() {
        try {
            List<Item> itemsToExport = itemService.findAllItems();

            ExcelItemExport excelItemExport = new ExcelItemExport(itemsToExport);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            excelItemExport.generate(outputStream);
            byte[] excelData = outputStream.toByteArray();

            outputStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported_items_list.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error exporting data".getBytes());
        }
    }
}
