package com.model;

import lombok.Data;

@Data
public class Rating {
  private int userId;
  private int movieId;
  private int rating;
  private long timestamp;
}
