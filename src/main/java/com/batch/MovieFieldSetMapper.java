package com.batch;

import com.model.Movie;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

@Component
public class MovieFieldSetMapper implements FieldSetMapper<Movie> {

  @Override
  public Movie mapFieldSet(FieldSet fieldSet) {
    Movie movie = new Movie();
    movie.setMovieId(fieldSet.readInt(0));
    movie.setTitle(fieldSet.readString(1));
    movie.setGenres(fieldSet.readString(2));
    return movie;
  }
}
