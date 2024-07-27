package com.example.demo.service;
import org.springframework.stereotype.Service;
import com.example.demo.domain.Moto;
import com.example.demo.repository.MotoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MotoService {

    private final MotoRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(MotoService.class);

    public MotoService(MotoRepository repository) {
        this.repository = repository;
    }

    public Optional<Moto> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        Optional<Moto> moto = repository.findById(id);
        moto.ifPresent(m -> {
            m.setIsDeleted(LocalDateTime.now());
            repository.save(m);
        });
    }

    public Moto update(Moto m) {
        return repository.saveAndFlush(m);
    }

    public Moto create(Moto m) {
        m.regrasDeNegocioParaCadastro();
        return repository.save(m);
    }

    public List<Moto> findAll() {
        return repository.findAll();
    }

    public List<Moto> findAllNotDeleted() {
        List<Moto> motos = repository.findAll().stream()
                .filter(moto -> moto.getIsDeleted() == null)
                .toList();

        logger.info("Motos encontradas: {}", motos);

        return motos;
    }
}
