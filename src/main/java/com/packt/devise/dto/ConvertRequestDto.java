package com.packt.devise.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertRequestDto {
  @NotBlank(message = "La devise source est obligatoire")
  @Size(min = 3, max = 3, message = "La devise doit être un code ISO 3 lettres")
  private String sourceCurrency;

  @NotBlank(message = "La devise cible est obligatoire")
  @Size(min = 3, max = 3, message = "La devise doit être un code ISO 3 lettres")
  private String desiredCurrency;

  @NotBlank(message = "Le montant ne peut être vide")
  @Min(value = 0, message = "Le montant ne peut pas être négatif")
  private Float amount;
}
