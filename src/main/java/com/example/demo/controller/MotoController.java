package com.example.demo.controller;

import com.example.demo.domain.Moto;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.MotoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        visitaCookie.setMaxAge(24 * 60 * 60);
        visitaCookie.setPath("/");
        response.addCookie(visitaCookie);

        model.addAttribute("motos", motos);

        List<Moto> carrinho = (List<Moto>) session.getAttribute("carrinho");

        if (carrinho != null) {
            model.addAttribute("quantidadeCarrinho", carrinho.size());
        } else {
            model.addAttribute("quantidadeCarrinho", 0);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
        } else {
            model.addAttribute("username", null);
        }

        return "principal";
    }

    @GetMapping("/cadastro")
    public String getCadastroPage(Model model) {
        model.addAttribute("moto", new Moto());
        return "cadastroMotos";
    }

    @PostMapping("/salvar")
    public String processCadastro(@ModelAttribute @Valid Moto moto, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "cadastroMotos";
        }
    
        if (!file.isEmpty()) {
            String uniqueFilename = fileStorageService.save(file);
            moto.setImageUri(uniqueFilename);
        }
    
        if (moto.getId() != null && !moto.getId().isEmpty()) {
            motoService.update(moto);
        } else {
            motoService.create(moto);
        }
    
        redirectAttributes.addFlashAttribute("msg", "Alteração realizada com sucesso");
        return "redirect:/admin";
    }
    

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<Moto> motos = motoService.findAllNotDeleted();
        model.addAttribute("motos", motos);
        return "admin";
    }

    @GetMapping("/deletar")
    public String deleteMoto(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        motoService.delete(id);
        redirectAttributes.addFlashAttribute("msg", "Moto removida com sucesso");
        return "redirect:/index";
    }

    @GetMapping("/editar")
    public String getEditarPage(@RequestParam("id") String id, Model model) {
        Optional<Moto> moto = motoService.findById(id);
        if (moto.isPresent()) {
            model.addAttribute("moto", moto.get());
            return "editarMoto";
        } else {
            model.addAttribute("msg", "Moto não encontrada");
            return "redirect:/index";
        }
    }

    private void addUserInfoToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
        } else {
            model.addAttribute("username", null);
        }
    }
}