package com.model;

import lombok.Data;

@Data
public class User {
  private int userId;
  private String gender;
  private int age;
  private int occupation;
  private String zipCode;
}
