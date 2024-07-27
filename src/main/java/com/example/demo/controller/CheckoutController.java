/*package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {
    @GetMapping("/finalizarCompra")
    public String checkout(Model model) {
        return "checkout";
    }
}
*/

package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CheckoutController {

    @GetMapping("/finalizarCompra")
    public RedirectView finalizarCompra(HttpSession session) {
        // Invalidar a sessão
        session.invalidate();
        // Redirecionar para a página inicial (index)
        return new RedirectView("/index");
    }
}
