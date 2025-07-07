package com.batch;

import com.model.Rating;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class RatingItemProcessor implements ItemProcessor<Rating, Rating> {

  @Override
  public Rating process(Rating rating) {
    if (rating.getRating() >= 1 && rating.getRating() <= 5) {
      return rating;
    }
    // Skip this item
    return null;
  }
}
