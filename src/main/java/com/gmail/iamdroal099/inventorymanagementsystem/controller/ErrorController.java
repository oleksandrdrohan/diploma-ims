package com.gmail.iamdroal099.inventorymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/item-not-found")
    public String showItemNotFoundError() {
        return "item-not-found";
    }

    @GetMapping("/order-not-found")
    public String showOrderNotFoundError() {
        return "order-not-found";
    }

    @GetMapping("/order-with-this-cnn-already-exists")
    public String showOrderCnnError() {
        return "order-with-this-cnn-already-exists";
    }

    @GetMapping("/you-cant-change-order-status")
    public String showOrderStatusError() {
        return "you-cant-change-order-status";
    }

    @GetMapping("/token-time-is-over")
    public String showTokenTimeError() {
        return "token-time-is-over";
    }

    @GetMapping("/quantity-error")
    public String showQuantityError() {
        return "quantity-error";
    }

    @GetMapping("/quantity-less-than-zero-error")
    public String showQuantityLessThanZeroError() {
        return "quantity-less-than-zero-error";
    }

    @GetMapping("/order-field-error")
    public String showCnnFieldError(){
        return "order-field-error";
    }


}
