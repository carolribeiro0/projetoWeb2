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

import com.example.demo.domain.Moto;
import com.example.demo.service.MotoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarrinhoController {

    @Autowired
    private MotoService motoService;

    @GetMapping("/adicionarCarrinho")
    public String adicionarAoCarrinho(@RequestParam("id") String id, HttpSession session, Model model) {
        // Obtém o carrinho da sessão ou cria um novo se não existir
        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        // Encontra o moto pelo ID
        Optional<Moto> motoOpt = motoService.findById(id);
        if (motoOpt.isPresent()) {
            Moto moto = motoOpt.get();
            carrinho.add(moto);
            session.setAttribute("carrinho", carrinho);
        }

        return "redirect:/index";
    }

    @GetMapping("/carrinhoCompras")
    public String exibirCarrinho(Model model, HttpSession session) {
        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");

        if (carrinho != null) {
            model.addAttribute("carrinho", carrinho);
            model.addAttribute("quantidadeCarrinho", carrinho.size());
        } else {
            model.addAttribute("carrinho", List.of()); 
            model.addAttribute("quantidadeCarrinho", 0);
        }

        double subtotal = 0.0;
        if (carrinho != null) {
            for (Moto moto : carrinho) {
                subtotal += moto.getPreco();
            }
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
