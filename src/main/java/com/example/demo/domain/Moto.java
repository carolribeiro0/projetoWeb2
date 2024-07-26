package com.example.demo.domain;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "moto_tbl")
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Marca não pode estar vazia")
    private String marca;

    @NotBlank(message = "Modelo não pode estar vazio")
    private String modelo;

    @Positive(message = "O ano precisa ser um número positivo")
    @NotNull(message = "Ano não pode ser nulo")
    private Integer ano;

    @Positive(message = "Preço tem que ser um número positivo")
    @NotNull(message = "Preço não pode ser nulo")
    private Double preco;

    @NotBlank(message = "A capacidade do motor não pode estar vazia")
    private String capacidadeMotor;
    
    private LocalDateTime isDeleted;

    private String imageUri;
   
    public void regrasDeNegocioParaCadastro() {
        marca.toUpperCase();
    }
    
    public void setIsDeleted() {
        this.isDeleted = LocalDateTime.now();
    }
    
    public LocalDateTime getIsDeleted() {
        return isDeleted;
    }
}
