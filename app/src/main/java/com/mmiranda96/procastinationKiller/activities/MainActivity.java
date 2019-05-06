package com.mmiranda96.procastinationKiller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mmiranda96.procastinationKiller.R;
import com.mmiranda96.procastinationKiller.adapters.TaskListAdapter;
import com.mmiranda96.procastinationKiller.models.Task;
import com.mmiranda96.procastinationKiller.models.User;
import com.mmiranda96.procastinationKiller.sources.task.GetTasksAsyncTask;
import com.mmiranda96.procastinationKiller.sources.task.TaskSource;
import com.mmiranda96.procastinationKiller.sources.task.TaskSourceFactory;
import com.mmiranda96.procastinationKiller.sources.user.UpdateFirebaseTokenAsyncTask;
import com.mmiranda96.procastinationKiller.sources.user.UserSource;
import com.mmiranda96.procastinationKiller.sources.user.UserSourceFactory;
import com.mmiranda96.procastinationKiller.util.IntentExtras;
import com.mmiranda96.procastinationKiller.util.Server;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTasksAsyncTask.Listener, OnCompleteListener<InstanceIdResult> {
    public static final int PUT_TASK_ACTIVITY_CODE = 0, ADD_PEOPLE_ACTIVITY_CODE = 1;

    private User currentUser;
    private String firebaseToken;

    private ListView taskList;
    private TaskListAdapter adapter;
    private TaskSource taskSource;
    private UserSource userSource;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        this.currentUser = (User) intent.getSerializableExtra(IntentExtras.USER);

        this.taskList = findViewById(R.id.listViewMainActivityTaskList);
        this.editProfileButton = findViewById(R.id.buttonMainActivityEditProfile);

        ArrayList<Task> tasks = new ArrayList<>();
        this.adapter = new TaskListAdapter(this, this.currentUser, tasks);
        this.taskList.setAdapter(adapter);
        this.taskList.setOnItemClickListener(adapter);


        // TODO: use a dependency injection framework. Meanwhile, change REMOTE to FAKE if needed
        this.taskSource = TaskSourceFactory.newSource(
                TaskSourceFactory.REMOTE,
                this.currentUser,
                Server.URL
        );
        this.userSource = UserSourceFactory.newSource(
                UserSourceFactory.REMOTE,
                Server.URL
        );

        this.getTasks();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(this);

        Toast.makeText(getApplicationContext(), "Hello " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
    }

    public void addActivity(View view){
        Intent intent = new Intent(getApplicationContext(), PutTaskActivity.class);
        intent.putExtra(IntentExtras.USER, this.currentUser);
        startActivityForResult(intent, PUT_TASK_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PUT_TASK_ACTIVITY_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                case Activity.RESULT_CANCELED:
                    getTasks();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "An error occurred, please try later", Toast.LENGTH_SHORT).show();
                    getTasks();
                    break;
            }
        }
    }

    public void getTasks() {
        GetTasksAsyncTask asyncTask = this.taskSource.newGetTasksAsyncTask(this);
        asyncTask.execute();
    }

    @Override
    public void getTasksAsyncTaskDone(ArrayList<Task> result) {
        this.adapter.update(result);
    }

    public void onComplete(com.google.android.gms.tasks.Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
            Log.e("Firebase", "getInstanceId failed", task.getException());
            return;
        }

        // Get new Instance ID token
        String token = task.getResult().getToken();
        onNewToken(token);
    }

    public void onNewToken(String token) {
        Log.d("Firebase", "Token: " + token);
        UpdateFirebaseTokenAsyncTask asyncTask = this.userSource.newUpdateFirebaseTokenAsyncTask(this.currentUser);
        asyncTask.execute(token);
    }

    public void onEditProfileButtonClick(View view){
        Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
        intent.putExtra(IntentExtras.USER, this.currentUser);
        startActivity(intent);
    }
}
