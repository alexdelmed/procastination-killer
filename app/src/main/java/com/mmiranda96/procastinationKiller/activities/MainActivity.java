package com.mmiranda96.procastinationKiller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mmiranda96.procastinationKiller.R;
import com.mmiranda96.procastinationKiller.adapters.TaskListAdapter;
import com.mmiranda96.procastinationKiller.models.Task;
import com.mmiranda96.procastinationKiller.models.User;
import com.mmiranda96.procastinationKiller.sources.task.GetTasksAsyncTask;
import com.mmiranda96.procastinationKiller.sources.task.TaskSource;
import com.mmiranda96.procastinationKiller.sources.task.TaskSourceFactory;
import com.mmiranda96.procastinationKiller.util.IntentExtras;
import com.mmiranda96.procastinationKiller.util.Server;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTasksAsyncTask.Listener {
    public static final int PUT_TASK_ACTIVITY_CODE = 0, ADD_PEOPLE_ACTIVITY_CODE = 1;

    private User currentUser;

    private ListView taskList;
    private TaskListAdapter adapter;
    private TaskSource taskSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        this.currentUser = (User) intent.getSerializableExtra(IntentExtras.USER);

        this.taskList = findViewById(R.id.listViewMainActivityTaskList);

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

        this.getTasks();

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
}
