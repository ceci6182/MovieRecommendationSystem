/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.ArrayList;
import java.util.List;
import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieSearcher
{
    public List<Movie> search(List<Movie> searchBase, String query)
    {
        List<Movie> results = new ArrayList<>();
        for (Movie movie : searchBase)
        {
            String searchMovie = movie.getTitle().toLowerCase().replaceAll("\\s+","");
            if (searchMovie.contains(query.toLowerCase().replaceAll("\\s+","")) || ("" + movie.getYear()).contains(query))
            {
                results.add(movie);
            }
        }
        return results;
    }
    
}
