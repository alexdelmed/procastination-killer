package com.mmiranda96.procastinationKiller.sources.user;

import com.mmiranda96.procastinationKiller.models.User;

public interface UserSource {
    LoginAsyncTask newLoginAsyncTask(LoginAsyncTask.Listener listener);

    SignupAsyncTask newSignupAsyncTask(SignupAsyncTask.Listener listener);

    UpdateFirebaseTokenAsyncTask newUpdateFirebaseTokenAsyncTask(User user);

    UpdateUserAsyncTask newUpdateUserAsyncTask(UpdateUserAsyncTask.Listener listener, User user);
}
