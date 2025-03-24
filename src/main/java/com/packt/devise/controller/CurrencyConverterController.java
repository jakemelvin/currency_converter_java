package com.packt.devise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.packt.devise.dto.ConvertRequestDto;
import com.packt.devise.dto.ConvertResponseDto;
import com.packt.devise.exceptions.ApiConnectionException;
import com.packt.devise.exceptions.InvalidCurrencyException;
import com.packt.devise.model.ExchangeRateResult;
import com.packt.devise.response.ApiResponse;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RequestMapping("${api.prefix}/currencies")
@RestController
public class CurrencyConverterController {
  @Value("${API_PUBLIC_KEY}")
  private String apiKey;
  @Autowired
  private WebClient.Builder webClientBuilder;

  @PostMapping
  public ResponseEntity<ApiResponse> convertAmountAndCurrency(@Valid @RequestBody ConvertRequestDto dto) {
    try {
      String apiUrl = buildApiUrl(dto);

      ExchangeRateResult result = webClientBuilder.build()
          .get()
          .uri(apiUrl)
          .retrieve()
          .onStatus(status -> status.is4xxClientError(),
              response -> Mono.error(new InvalidCurrencyException("Combinaison de devises invalide")))
          .onStatus(status -> status.is5xxServerError(),
              response -> Mono.error(new ApiConnectionException("Service de conversion indisponible")))
          .bodyToMono(ExchangeRateResult.class)
          .block();

      validateConversionResult(result);

      return ResponseEntity.ok(new ApiResponse("Conversion successful", buildSuccessResponse(result)));

    } catch (ApiConnectionException e) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(new ApiResponse(e.getMessage(), null));
    } catch (InvalidCurrencyException e) {
      return ResponseEntity.badRequest()
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  private String buildApiUrl(ConvertRequestDto dto) {

    return "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/"
        + dto.getSourceCurrency().toUpperCase()
        + "/" + dto.getDesiredCurrency().toUpperCase()
        + "/" + dto.getAmount();
  }

  private void validateConversionResult(ExchangeRateResult result) {
    if ("error".equalsIgnoreCase(result.getResult())) {
      throw new InvalidCurrencyException("Les devises fournies sont invalides");
    }

    if (result.getConversion_result() == null || result.getConversion_rate() == null) {
      throw new ApiConnectionException("Réponse inattendue de l'API de conversion");
    }
  }

  private ConvertResponseDto buildSuccessResponse(ExchangeRateResult result) {
    ConvertResponseDto response = new ConvertResponseDto();
    response.setBase_code(result.getBase_code());
    response.setTarget_code(result.getTarget_code());
    response.setConversion_rate(result.getConversion_rate());
    response.setConversion_result(result.getConversion_result());

    return response;
  }
}
