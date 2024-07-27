package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Moto;
import com.example.demo.service.MotoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarrinhoController {

    @Autowired
    private MotoService motoService;

    @GetMapping("/adicionarCarrinho")
    public String adicionarAoCarrinho(@RequestParam("id") String id, HttpSession session, Model model) {
        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        Optional<Moto> motoOpt = motoService.findById(id);
        if (motoOpt.isPresent()) {
            Moto moto = motoOpt.get();
            carrinho.add(moto);
            session.setAttribute("carrinho", carrinho);
        }

        return "redirect:/index";
    }

    @GetMapping("/verCarrinho")
    public String exibirCarrinho(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");
    
        if (carrinho == null || carrinho.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Não existem itens no carrinho");
            return "redirect:/index";
        }
    
        model.addAttribute("carrinho", carrinho);
        model.addAttribute("quantidadeCarrinho", carrinho.size());
    
        double subtotal = 0.0;
        for (Moto moto : carrinho) {
            subtotal += moto.getPreco();
        }
        model.addAttribute("subtotal", subtotal);
    
        return "carrinhoCompras"; 
    }
    

    @GetMapping("/removerDoCarrinho")
    public String removerDoCarrinho(@RequestParam("id") String id, HttpSession session) {
        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");
        if (carrinho != null) {
            carrinho.removeIf(moto -> moto.getId().equals(id));
            session.setAttribute("carrinho", carrinho);
        }
        return "redirect:/index";
    }
}
