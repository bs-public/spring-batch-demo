package com.config;

import com.batch.MovieFieldSetMapper;
import com.model.Movie;
import javax.sql.DataSource;
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

  private final MovieFieldSetMapper movieFieldSetMapper;

  public BatchConfig(MovieFieldSetMapper movieFieldSetMapper) {
    this.movieFieldSetMapper = movieFieldSetMapper;
  }

  @Bean
  public FlatFileItemReader<Movie> movieItemReader() {
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
  public Job importMoviesJob(JobRepository jobRepository, Step movieImportStep) {
    return new JobBuilder("importMoviesJob", jobRepository).start(movieImportStep).build();
  }
}
