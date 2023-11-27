package com.gmail.iamdroal099.inventorymanagementsystem.service;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Order;
import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.SelectedItems;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Transactional
    public void createOrder(Order newOrder) {
        orderRepository.save(newOrder);
    }

    @Transactional(readOnly = true)
    public Order findOrderByCNN(String cnn){
        return orderRepository.findOrderByConsignmentNoteNumber(cnn);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrderPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        Order order = findById(orderId).orElseThrow();
        order.setSelectedItems(null);
        order.setItemsSet(null);


        orderRepository.deleteById(orderId);
    }


}
