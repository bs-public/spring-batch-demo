package com.batch;

import com.model.Rating;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class RatingFieldSetMapper implements FieldSetMapper<Rating> {

  @Override
  public Rating mapFieldSet(FieldSet fieldSet) {
    Rating rating = new Rating();
    rating.setUserId(fieldSet.readInt(0));
    rating.setMovieId(fieldSet.readInt(1));
    rating.setRating(fieldSet.readInt(2));
    rating.setTimestamp(fieldSet.readLong(3));
    return rating;
  }
}
