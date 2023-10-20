package com.gmail.iamdroal099.inventorymanagementsystem.controller;


import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Order;
import com.gmail.iamdroal099.inventorymanagementsystem.model.POJO.QuantityUpdateRequest;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Token;
import com.gmail.iamdroal099.inventorymanagementsystem.model.enums.OrderStatus;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.SelectedItems;
import com.gmail.iamdroal099.inventorymanagementsystem.model.enums.OrderType;
import com.gmail.iamdroal099.inventorymanagementsystem.service.*;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Data
public class OrdersController {


    private final OrderService orderService;


    private final ItemService itemService;


    private final SelectedItemsService selectedItemsService;


    private final TokenService tokenService;


    private final ItemUpdateNotesService itemUpdateNotesService;

    @GetMapping("make-order")
    public String showMakeOrderPage(@RequestParam(name = "token") String token, Model model) {

        Order order = new Order();
        SelectedItems selectedItems = selectedItemsService.getLastSelectedItem();
        Token tokenFromSI = tokenService.getByTokenName(selectedItems.getToken().getTokenName());
        if (tokenFromSI != null && tokenService.isValidToken(tokenFromSI.getTokenName())) {
            if (Objects.equals(tokenFromSI.getTokenName(), token)) {
                List<Long> itemIds = selectedItems.getSelectedItemsForOrder();
                for (Long id : itemIds) {
                    Item item = itemService.findById(id);
                    order.getItemsSet().add(item);
                }
                model.addAttribute("order", order);
                return "make-order";
            } else {
                return "redirect:/order-with-this-cnn-already-exists";
            }
        } else {
            return "redirect:/token-time-is-over";
        }
    }


    @PostMapping("/collect-items")
    public ResponseEntity<Map<String, String>> collectItems(@RequestBody List<Long> selectedIds) {
        SelectedItems si = new SelectedItems();
        for (Long itemId : selectedIds) {
            si.getSelectedItemsForOrder().add(itemId);
        }
        String generatedToken = UUID.randomUUID().toString();
        Token token = new Token();
        token.setTokenName(generatedToken);
        Date date = new Date(System.currentTimeMillis());
        token.setCreationDate(date);
        token.setSelectedItems(si);
        si.setToken(token);
        selectedItemsService.save(si);
        Map<String, String> response = new HashMap<>();
        response.put("token", generatedToken);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/make-order")
    public String createOrder(@ModelAttribute("order") Order order) {

        Order orderInSQL = orderService.findOrderByCNN(order.getConsignmentNoteNumber());

        if (orderInSQL != null) {
            return "redirect:/order-with-this-cnn-already-exists";
        } else {

            SelectedItems si = selectedItemsService.getLastSelectedItem();
            Order newOrder = new Order();
            if (Objects.equals(order.getCarNumber(), "") ||
                    Objects.equals(order.getDriverName(), "") ||
                    Objects.equals(order.getConsignmentNoteNumber(), "") ||
                    Objects.equals(order.getIncomingDate(), "")) {
                order.setCarNumber(null);
                order.setDriverName(null);
                order.setConsignmentNoteNumber(null);
                order.setIncomingDate(null);
            }
            newOrder.setDriverName(order.getDriverName());
            newOrder.setIncomingDate(order.getIncomingDate());
            newOrder.setCarNumber(order.getCarNumber());
            newOrder.setConsignmentNoteNumber(order.getConsignmentNoteNumber());
            if (newOrder.getConsignmentNoteNumber() == null ||
                    newOrder.getDriverName() == null ||
                    newOrder.getIncomingDate() == null ||
                    newOrder.getCarNumber() == null) {
                return "redirect:/order-field-error";
            }
            newOrder.setType(order.getType());
            newOrder.setSelectedItems(si);
            si.setOrder(newOrder);

            int count = 0;
            int index = 0;


            for (Long id : si.getSelectedItemsForOrder()) {
                Item item = itemService.findById(id);
                newOrder.getItemsSet().add(item);
                newOrder.getSelectedQuantityForOrder().add(item.getQuantityForOrder());
                if (newOrder.getType() == OrderType.SHIPMENT && newOrder.getSelectedQuantityForOrder().get(index) > item.getQuantity()) {
                    return "redirect:/quantity-error";
                }
                item.getOrders().add(newOrder);
                item.setQuantityForOrder(0);
                count = count + 1;
                index = index + 1;
            }


            newOrder.setTotalAmountOfItems(count);

            orderService.createOrder(newOrder);

            return "redirect:/orders-list";
        }
    }


    @GetMapping("/make-order-items-list")
    public String showMakeOrderItemListPage(Model model, Item item) {
        List<Item> items = itemService.findAllItems();
        model.addAttribute("item", item);
        model.addAttribute("items", items);
        return "make-order-items-list";
    }


    @GetMapping("/orders-list")
    public String listOrders(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Order> ordersPage = orderService.getOrderPage(page, size);
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("ordersPage", ordersPage);
        return "orders-list";
    }

    @GetMapping("/order/{cnn}")
    public String orderDetails(@PathVariable("cnn") String cnn, Model model) {

        Order order = orderService.findOrderByCNN(cnn);
        if (order != null) {
            int index = 0;
            for (Item item : order.getItemsSet()) {
                item.setQuantityForOrder(order.getSelectedQuantityForOrder().get(index));
                if (index == order.getSelectedQuantityForOrder().size()) {
                    break;
                }
                index = index + 1;
            }
            model.addAttribute("order", order);
            return "order";
        } else {
            return "order-not-found";
        }
    }

    @PostMapping("/change-status")
    public String changeOrderStatus(@RequestParam("orderCNN") String cnn, @RequestParam("newStatus") OrderStatus newStatus, Model model) {

        Order order = orderService.findOrderByCNN(cnn);

        if (order != null) {
            if (order.getStatus() != OrderStatus.PROCESSED && order.getType() == OrderType.SUPPLY) {
                order.setStatus(newStatus);
                orderService.createOrder(order);

                int index = 0;
                for (Item item : order.getItemsSet()) {
                    itemService.changeQuantityInStockForSupply(item, order.getSelectedQuantityForOrder().get(index));
                    if (index == order.getSelectedQuantityForOrder().size()) {
                        break;
                    } else {
                        index = index + 1;
                    }
                }
                model.addAttribute("order", order);
                return "order";
            } else if (order.getStatus() != OrderStatus.PROCESSED && order.getType() == OrderType.SHIPMENT) {
                order.setStatus(newStatus);
                orderService.createOrder(order);

                int index = 0;
                for (Item item : order.getItemsSet()) {
                    itemService.changeQuantityInStockForShipment(item, order.getSelectedQuantityForOrder().get(index));
                    if (index == order.getSelectedQuantityForOrder().size()) {
                        break;
                    } else {
                        index = index + 1;
                    }
                }
                model.addAttribute("order", order);
                return "order";
            } else {
                return "you-cant-change-order-status";
            }
        } else {
            return "order-not-found";
        }
    }

    @PostMapping("/update-quantity-for-order")
    public ResponseEntity<String> updateQuantityForOrder(@RequestBody List<QuantityUpdateRequest> quantityUpdateRequests) {


        for (QuantityUpdateRequest request : quantityUpdateRequests) {
            Long itemId = request.getItemId();
            Integer quantityValue = request.getQuantityValue();

            Item item = itemService.findById(itemId);
            item.setQuantityForOrder(quantityValue);

            itemService.save(item);
        }

        return ResponseEntity.ok("Quantity updated successfully");
    }

    @GetMapping("/update-order/{id}")
    public String showUpdateOrderPage(@PathVariable("id") Long id, Model model) {
        Order order = orderService.findById(id).orElseThrow();
        model.addAttribute("order", order);
        return "update-order";
    }

    @PostMapping("/update-order/{id}")
    public String updateOrder(@PathVariable(value = "id") Long id,
                              @RequestParam String consignmentNoteNumber,
                              @RequestParam String incomingDate,
                              @RequestParam String carNumber,
                              @RequestParam String driverName) {
        Order order = orderService.findById(id).orElseThrow();
        order.setConsignmentNoteNumber(consignmentNoteNumber);
        order.setIncomingDate(incomingDate);
        order.setCarNumber(carNumber);
        order.setDriverName(driverName);
        orderService.createOrder(order);
        return "redirect:/orders-list";
    }

    @GetMapping("/delete-order/{id}")
    public String deleteOrder(@PathVariable(value = "id") Long id) {
        Order order = orderService.findById(id).orElseThrow();
        List <Item> items = order.getItemsSet();

        for (Item item:items){
            item.setOrders(null);
        }
        itemService.saveAll(items);

        SelectedItems si = order.getSelectedItems();
        si.setOrder(null);
        Token token = si.getToken();
        token.setSelectedItems(null);
        si.setToken(null);
        selectedItemsService.save(si);
        tokenService.save(token);

        orderService.deleteOrder(id);
        selectedItemsService.delete(si);
        tokenService.delete(token);
        return "redirect:/orders-list";
    }

}
