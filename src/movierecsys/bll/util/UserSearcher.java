/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import movierecsys.be.Movie;
import movierecsys.be.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pgn
 */
public class UserSearcher
{
    public List<User> search(List<User> searchBase, String query)
    {
        List<User> results = new ArrayList<>();
        for (User user : searchBase)
        {
            String searchUser = user.getName().toLowerCase().replaceAll("\\s+","");
            if (searchUser.contains(query.toLowerCase().replaceAll("\\s+","")) || ("" + user.getId()).contains(query))
            {
                results.add(user);
            }
        }
        return results;
    }
    
}
