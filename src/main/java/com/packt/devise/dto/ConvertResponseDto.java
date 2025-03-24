package com.packt.devise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertResponseDto {
  private String base_code;
  private String target_code;
  private Float conversion_rate;
  private Float conversion_result;
}
