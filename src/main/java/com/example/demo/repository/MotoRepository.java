package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.domain.*;

@Repository
public interface MotoRepository extends JpaRepository<Moto, String> {
}
