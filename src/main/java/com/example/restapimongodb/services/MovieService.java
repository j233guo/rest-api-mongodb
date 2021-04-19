package com.example.restapimongodb.services;

import com.example.restapimongodb.models.Movie;
import com.example.restapimongodb.models.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Movie> getMovies () {
        return repository.findAll();
    }

    public List<Movie> getMoviesWithRating (String rating) {
        // business logic
        Query query = new Query();
        query.addCriteria(Criteria.where("rating").is(rating));
        List<Movie> movies = mongoTemplate.find(query, Movie.class);
        return movies;
    }

    public Optional<Movie> getAMovie(String id) throws Exception{
        Optional<Movie> movie = repository.findById(id);
        if (!movie.isPresent()) {
            throw new Exception("Movie with " + id + " is not found");
        }
        return movie;
    }

    public void deleteAMovie(String id) {
        repository.deleteById(id);
    }

    public void insertIntoMovies(Movie movie) {
        repository.insert(movie);
    }

    public Movie editMovie(String id, Movie newMovieData) {

        // Get the resource based on the id
        Optional<Movie> movie = repository.findById(id);

        // update the found resource with the new data
        movie.get().setTitle(newMovieData.getTitle());
        movie.get().setDescription(newMovieData.getDescription());
        movie.get().setRating(newMovieData.getRating());

        // commit the update by saving it
        Movie updatedMovie = repository.save(movie.get());
        return updatedMovie;
    }
}
