/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import movierecsys.be.Movie;
import movierecsys.gui.model.MovieModel;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable
{
    MovieModel movieModel = new MovieModel();

    /**
     * The TextField containing the URL of the targeted website.
     */
    @FXML
    private TextField txtMovieSearch;

    /**
     * The TextField containing the query word.
     */
    @FXML
    private ListView<Movie> lstMovies;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       lstMovies.setItems(movieModel.getMovies());
        
    }

    public void handleMovieSearch(KeyEvent keyEvent) {
        String queue = txtMovieSearch.getText().trim();
        if (queue.isEmpty() || queue.isBlank()) {
            lstMovies.setItems(movieModel.getMovies());
        }
        else {
            movieModel.search(queue);
            lstMovies.setItems(movieModel.getSearchMovies());
        }
    }

    public void btnUpdateMovie(ActionEvent actionEvent) {
    }

    public void btnDeleteMovie(ActionEvent actionEvent) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        movieModel.delete(selectedMovie);
    }

    public void btnAddMovie(ActionEvent actionEvent) {
    }
}