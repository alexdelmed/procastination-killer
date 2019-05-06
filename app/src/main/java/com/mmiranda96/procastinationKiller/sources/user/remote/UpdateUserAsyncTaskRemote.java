package com.mmiranda96.procastinationKiller.sources.user.remote;

import com.mmiranda96.procastinationKiller.models.User;
import com.mmiranda96.procastinationKiller.sources.user.UpdateUserAsyncTask;
import com.mmiranda96.procastinationKiller.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateUserAsyncTaskRemote extends UpdateUserAsyncTask {

    private String server;

    protected UpdateUserAsyncTaskRemote(String server, User user, Listener listener) {
        super(listener, user);
        this.server = server;
    }

    @Override
    protected Integer doInBackground(User... users) {
        int result = UpdateUserAsyncTask.ERROR;

        try {
            User user = users[0];

            URL url = new URL(this.server + "/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String authentication = user.buildAuthentication();
            conn.setRequestProperty("Authorization", "Basic " + authentication);
            conn.setRequestProperty(Server.CONTENT_TYPE_KEY, Server.CONTENT_TYPE_VALUE);
            conn.setRequestMethod("PUT");

            OutputStream os = conn.getOutputStream();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.newUser.getUsername());
            jsonObject.put("email", this.newUser.getUsername());
            os.write(jsonObject.toString().getBytes());

            os.close();

            int code = conn.getResponseCode();
            switch (code) {
                case HttpURLConnection.HTTP_OK:
                    result = SUCCESS;
                    break;
                default:
                    result = ERROR;
                    break;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }
}
