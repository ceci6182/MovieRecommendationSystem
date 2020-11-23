/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.be;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pgn
 */
public class Movie
{

    private final int id;
    private String title;
    private int year;
    private int score = 0;
    private List<Integer> scores;

    public Movie(int id, int year, String title)
    {
        this.id = id;
        this.title = title;
        this.year = year;
        scores = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    @Override
    public String toString()
    {
        String returnString = id + ":" + title + "(" + year + ")";
        if (score != 0) {
            returnString += " " + score;
        }
        return returnString;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        scores.add(score);
        int tempScore = 0;
        if (scores.size() == 1) {
            this.score = score;
        }
        else {
            for(int i = 0; i < scores.size(); i++) {
                tempScore+= scores.get(i);
            }
            this.score= tempScore/2;
        }
    }
}
