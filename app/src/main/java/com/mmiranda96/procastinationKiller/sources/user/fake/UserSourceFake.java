package com.mmiranda96.procastinationKiller.sources.user.fake;

import com.mmiranda96.procastinationKiller.models.User;
import com.mmiranda96.procastinationKiller.sources.user.LoginAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.SignupAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UpdateFirebaseTokenAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UpdateUserAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UserSource;

public class UserSourceFake implements UserSource {

    @Override
    public LoginAsyncTask newLoginAsyncTask(LoginAsyncTask.Listener listener) {
        return new LoginAsyncTaskFake(listener);
    }

    @Override
    public SignupAsyncTask newSignupAsyncTask(SignupAsyncTask.Listener listener) {
        return new SignupAsyncTaskFake(listener);
    }

    @Override
    public UpdateFirebaseTokenAsyncTask newUpdateFirebaseTokenAsyncTask(User user) {
        return new UpdateFirebaseTokenAsyncTaskFake();
    }

    @Override
    public UpdateUserAsyncTask newUpdateUserAsyncTask(UpdateUserAsyncTask.Listener listener, User user) {
        return null;
    }
}
