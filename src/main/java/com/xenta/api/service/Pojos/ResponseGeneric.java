package com.xenta.api.service.Pojos;

import lombok.Data;

@Data
public class ResponseGeneric <T>{

  private T data;
  private String message;
  private String error;

  public ResponseGeneric(T data, String message, String error) {
    this.data = data;
    this.message = message;
    this.error = error;
  }
}
