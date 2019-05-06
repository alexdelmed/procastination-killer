package com.mmiranda96.procastinationKiller.sources.user;

import android.os.AsyncTask;

import com.mmiranda96.procastinationKiller.models.User;

public abstract class UpdateUserAsyncTask extends AsyncTask<User, Void, Integer> {
    public static final int SUCCESS = 0, ERROR = 1;
    protected UpdateUserAsyncTask.Listener listener;
    protected User newUser;

    protected UpdateUserAsyncTask(UpdateUserAsyncTask.Listener listener, User user) {
        this.listener = listener;
        this.newUser = user;
    }

    @Override
    protected void onPostExecute(Integer result) {
        this.listener.updateUserAsyncTaskDone(result);
    }

    public interface Listener {
        void updateUserAsyncTaskDone(Integer result);
    }
}
