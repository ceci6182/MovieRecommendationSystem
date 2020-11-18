/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
    public TextField txtMovieTitle;
    public TextField txtReleaseYear;
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
        setMovieSelection();
        
    }

    /**
     * Sets up the movie selection of the list of all movers.
     */
    private void setMovieSelection()
    {
        //I do this to receive updates when a new movie is selected from the list of all movies:
        lstMovies.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lstMovies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>()
        {

            @Override
            public void changed(ObservableValue<? extends Movie> observable, Movie oldValue, Movie newValue)
            {
                if (newValue != null)
                {
                    txtMovieTitle.setText(newValue.getTitle());
                    txtReleaseYear.setText(newValue.getYear() + "");
                }
            }

        });

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
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null)
        {
                String title = txtMovieTitle.getText();
                int year = Integer.parseInt(txtReleaseYear.getText());
                selectedMovie.setTitle(title);
                selectedMovie.setYear(year);
                movieModel.update(selectedMovie);

        }
    }

    public void btnDeleteMovie(ActionEvent actionEvent) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        movieModel.delete(selectedMovie);
    }

    public void btnAddMovie(ActionEvent actionEvent) throws IOException {
        String movieTitle = txtMovieTitle.getText().trim();
        int releaseYear = Integer.parseInt(txtReleaseYear.getText().trim());
        movieModel.add(releaseYear,movieTitle);
    }

    public void btnClear(ActionEvent actionEvent) {
        txtMovieTitle.clear();
        txtReleaseYear.clear();
    }
}