package com.example.demo.controller;

import com.example.demo.domain.Moto;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.MotoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.Errors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MotoController {

    private final MotoService motoService;
    private final FileStorageService fileStorageService;

    @Autowired
    public MotoController(MotoService motoService, FileStorageService fileStorageService) {
        this.motoService = motoService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/index")
    public String listAll(Model model, HttpSession session, HttpServletResponse response) {
        List<Moto> motos = motoService.findAllNotDeleted();

        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        Cookie visitaCookie = new Cookie("visita", currentDateTime);
        visitaCookie.setMaxAge(24 * 60 * 60); // 24 horas
        visitaCookie.setPath("/"); 
        response.addCookie(visitaCookie);

        model.addAttribute("motos", motos);

        // Obtém o carrinho da sessão
        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");

        // Adiciona a quantidade de itens no carrinho ao modelo
        if (carrinho != null) {
            model.addAttribute("quantidadeCarrinho", carrinho.size());
        } else {
            model.addAttribute("quantidadeCarrinho", 0);
        }

        return "principal";
    }

    @GetMapping("/cadastro")
    public String getCadastroPage(Model model) {
        model.addAttribute("moto", new Moto());
        return "cadastroMotos";
    }

    @PostMapping("/salvar")
    public ModelAndView processCadastro(@ModelAttribute @Valid Moto moto, Errors errors, @RequestParam("file") MultipartFile file) {

        if (errors.hasErrors()) {
            return new ModelAndView("cadastroMotos");
        }

        if (!file.isEmpty()) {
            String filename = file.getOriginalFilename();
            fileStorageService.save(file);
            moto.setImageUri(filename);
        }

        motoService.create(moto);

        ModelAndView modelAndView = new ModelAndView("principal");
        modelAndView.addObject("msg", "Cadastro realizado com sucesso");
        modelAndView.addObject("motos", motoService.findAll());
        return modelAndView;
    }

}