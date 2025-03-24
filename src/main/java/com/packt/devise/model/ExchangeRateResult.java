package com.packt.devise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResult {
  private String result;
  private String documentation;
  private String terms_of_use;
  private int time_last_update_unix;
  private String time_last_update_utc;
  private int time_next_update_unix;
  private String time_next_update_utc;
  private String base_code;
  private String target_code;
  private Float conversion_rate;
  private Float conversion_result;
}
