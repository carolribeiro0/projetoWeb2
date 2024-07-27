package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CheckoutController {

    @GetMapping("/finalizarCompra")
    public RedirectView finalizarCompra(HttpSession session) {
        session.invalidate();
        return new RedirectView("/index");
    }
}
