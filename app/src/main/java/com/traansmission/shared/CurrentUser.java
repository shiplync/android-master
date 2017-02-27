package com.traansmission.shared;

import com.traansmission.models.User;

/**
 * Created by SAMBUCA on 3/8/16.
 */
public class CurrentUser {
    private static CurrentUser mInstance = null;
    private User user;

    private CurrentUser(){
        user = null;
    }

    public static CurrentUser getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentUser();
        }
        return mInstance;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public User getCurrentUser() {
        return this.user;
    }
}
