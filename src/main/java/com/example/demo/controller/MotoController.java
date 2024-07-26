package com.example.demo.controller;

import com.example.demo.domain.Moto;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.Errors;
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

    @GetMapping("/")
    public String listAll(Model model) {
        model.addAttribute("motos", motoService.findAllNotDeleted());
        return "principal";
    }

    @GetMapping("/cadastro")
    public String getCadastroPage(Model model) {
        model.addAttribute("moto", new Moto());
        return "cadastroMotos";
    }

    @PostMapping("/processCadastro")
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