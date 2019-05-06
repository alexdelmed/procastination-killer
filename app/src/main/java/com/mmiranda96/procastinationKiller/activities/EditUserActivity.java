package com.mmiranda96.procastinationKiller.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mmiranda96.procastinationKiller.R;
import com.mmiranda96.procastinationKiller.models.User;
import com.mmiranda96.procastinationKiller.sources.user.UpdateUserAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UserSource;
import com.mmiranda96.procastinationKiller.sources.user.UserSourceFactory;
import com.mmiranda96.procastinationKiller.util.IntentExtras;
import com.mmiranda96.procastinationKiller.util.Server;

import static android.content.RestrictionsManager.RESULT_ERROR;

public class EditUserActivity extends AppCompatActivity implements UpdateUserAsyncTask.Listener{

    private User currentUser;
    private EditText nameEditText;
    private EditText emailEditText;
    private User updatedUser;
    private UserSource userSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent = getIntent();
        this.currentUser = (User) intent.getSerializableExtra(IntentExtras.USER);

        this.nameEditText = findViewById(R.id.editTextEditUserActivityName);
        this.emailEditText = findViewById(R.id.editTextEditUserActivityEmail);
        this.userSource = UserSourceFactory.newSource(UserSourceFactory.REMOTE, Server.URL);
    }

    public void onOkButtonClick(View view){
        this.updatedUser = new User(this.emailEditText.getText().toString(), this.currentUser.getPassword()); //TODO user model has no name attribute
        UpdateUserAsyncTask asyncTask = this.userSource.newUpdateUserAsyncTask(this, this.updatedUser);
        asyncTask.execute(this.currentUser);
    }

    @Override
    public void updateUserAsyncTaskDone(Integer result) {
        finishActivity(result);
    }

    private void finishActivity(Integer result) {
        Intent intent = getIntent();
        intent.putExtra(IntentExtras.USER, this.updatedUser);
        switch (result) {
            case UpdateUserAsyncTask.SUCCESS:
                Toast.makeText(getApplicationContext(), "Your profile has been updated", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, intent);
                break;
            default:
                Toast.makeText(getApplicationContext(), "An error ocurred", Toast.LENGTH_SHORT).show();
                setResult(RESULT_ERROR);
                break;
        }
        finish();
    }
}
