package com.config;

import com.batch.MovieFieldSetMapper;
import com.batch.RatingFieldSetMapper;
import com.batch.UserFieldSetMapper;
import com.model.Movie;
import javax.sql.DataSource;
import com.model.Rating;
import com.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Bean
  public FlatFileItemReader<Movie> movieItemReader(MovieFieldSetMapper movieFieldSetMapper) {
    return new FlatFileItemReaderBuilder<Movie>()
        .name("movieItemReader")
        .resource(new ClassPathResource("data/movies.dat"))
        .lineTokenizer(
            new DelimitedLineTokenizer("::") {
              {
                setNames("movieId", "title", "genres");
              }
            })
        .fieldSetMapper(movieFieldSetMapper)
        .linesToSkip(0)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<Movie> movieItemWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Movie>()
        .dataSource(dataSource)
        .sql("INSERT INTO movies (movie_id, title, genres) VALUES (?, ?, ?)")
        .itemPreparedStatementSetter(
            (movie, ps) -> {
              ps.setInt(1, movie.getMovieId());
              ps.setString(2, movie.getTitle());
              ps.setString(3, movie.getGenres());
            })
        .build();
  }

  @Bean
  public Step movieImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FlatFileItemReader<Movie> movieItemReader,
      JdbcBatchItemWriter<Movie> movieItemWriter) {
    return new StepBuilder("movieImportStep", jobRepository)
        .<Movie, Movie>chunk(100, transactionManager)
        .reader(movieItemReader)
        .writer(movieItemWriter)
        .build();
  }

  @Bean
  public FlatFileItemReader<User> userItemReader(UserFieldSetMapper userFieldSetMapper) {
    return new FlatFileItemReaderBuilder<User>()
        .name("userItemReader")
        .resource(new ClassPathResource("data/users.dat"))
        .lineTokenizer(
            new DelimitedLineTokenizer("::") {
              {
                setNames("userId", "gender", "age", "occupation", "zipCode");
              }
            })
        .fieldSetMapper(userFieldSetMapper)
        .linesToSkip(0)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<User> userItemWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<User>()
        .dataSource(dataSource)
        .sql(
            "INSERT INTO users (user_id, gender, age, occupation, zip_code) VALUES (?, ?, ?, ?, ?)")
        .itemPreparedStatementSetter(
            (user, ps) -> {
              ps.setInt(1, user.getUserId());
              ps.setString(2, user.getGender());
              ps.setInt(3, user.getAge());
              ps.setInt(4, user.getOccupation());
              ps.setString(5, user.getZipCode());
            })
        .build();
  }

  @Bean
  public Step userImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FlatFileItemReader<User> userItemReader,
      JdbcBatchItemWriter<User> userItemWriter) {
    return new StepBuilder("userImportStep", jobRepository)
        .<User, User>chunk(100, transactionManager)
        .reader(userItemReader)
        .writer(userItemWriter)
        .build();
  }

  @Bean
  public FlatFileItemReader<Rating> ratingItemReader(RatingFieldSetMapper ratingFieldSetMapper) {
    return new FlatFileItemReaderBuilder<Rating>()
        .name("ratingItemReader")
        .resource(new ClassPathResource("data/ratings.dat"))
        .lineTokenizer(
            new DelimitedLineTokenizer("::") {
              {
                setNames("userId", "movieId", "rating", "timestamp");
              }
            })
        .fieldSetMapper(ratingFieldSetMapper)
        .linesToSkip(0)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<Rating> ratingItemWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Rating>()
        .dataSource(dataSource)
        .sql("INSERT INTO ratings (user_id, movie_id, rating, timestamp) VALUES (?, ?, ?, ?)")
        .itemPreparedStatementSetter(
            (rating, ps) -> {
              ps.setInt(1, rating.getUserId());
              ps.setInt(2, rating.getMovieId());
              ps.setInt(3, rating.getRating());
              ps.setLong(4, rating.getTimestamp());
            })
        .build();
  }

  @Bean
  public Step ratingImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FlatFileItemReader<Rating> ratingItemReader,
      JdbcBatchItemWriter<Rating> ratingItemWriter) {
    return new StepBuilder("ratingImportStep", jobRepository)
        .<Rating, Rating>chunk(1000, transactionManager)
        .reader(ratingItemReader)
        .writer(ratingItemWriter)
        .build();
  }

  @Bean
  public Job importAllJob(
      JobRepository jobRepository,
      Step movieImportStep,
      Step userImportStep,
      Step ratingImportStep) {
    return new JobBuilder("importAllJob", jobRepository)
        .start(movieImportStep)
        .next(userImportStep)
        .next(ratingImportStep)
        .build();
  }
}
