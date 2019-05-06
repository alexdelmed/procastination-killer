package com.mmiranda96.procastinationKiller.sources.user.remote;

import com.mmiranda96.procastinationKiller.models.User;
import com.mmiranda96.procastinationKiller.sources.user.LoginAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.SignupAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UpdateFirebaseTokenAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UpdateUserAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UserSource;

public class UserSourceRemote implements UserSource {

    private String server;

    public UserSourceRemote(String server) {
        this.server = server;
    }

    @Override
    public LoginAsyncTask newLoginAsyncTask(LoginAsyncTask.Listener listener) {
        return new LoginAsyncTaskRemote(this.server, listener);
    }

    @Override
    public SignupAsyncTask newSignupAsyncTask(SignupAsyncTask.Listener listener) {
        return new SignupAsyncTaskRemote(this.server, listener);
    }

    @Override
    public UpdateFirebaseTokenAsyncTask newUpdateFirebaseTokenAsyncTask(User user) {
        return new UpdateFirebaseTokenAsyncTaskRemote(this.server, user);
    }

    @Override
    public UpdateUserAsyncTask newUpdateUserAsyncTask(UpdateUserAsyncTask.Listener listener, User user) {
        return new UpdateUserAsyncTaskRemote(this.server, user, listener);
    }
}
