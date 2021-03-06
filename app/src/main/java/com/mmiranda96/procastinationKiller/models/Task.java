package com.mmiranda96.procastinationKiller.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {
    private double latitude, longitude;
    private Integer id;
    private String title, description;
    private Date due;
    private ArrayList<String> subtasks;

    public Task(int id, String title, String description, Date due, ArrayList<String> subtasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.due = due;
        this.subtasks = new ArrayList<>();
        this.subtasks.addAll(subtasks);
    }

    // Only new tasks can have their location set
    public Task(String title, String description, Date due, ArrayList<String> subtasks, LatLng location) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.due = due;
        this.subtasks = new ArrayList<>();
        this.subtasks.addAll(subtasks);
        this.latitude = location.latitude;
        this.longitude= location.longitude;
    }

    public Task(JSONObject jsonTask){
        try {
            this.id = jsonTask.getInt("id");
            this.title = jsonTask.getString("title");
            this.description = jsonTask.optString("description");
            long epoch = jsonTask.getLong("due") * 1000;
            this.due = new Date(epoch);
            ArrayList<String> subtasks = new ArrayList<>();
            JSONArray subtasksJSON = jsonTask.getJSONArray("subtasks");
            for(int i = 0; i < subtasksJSON.length(); i++){
                subtasks.add(subtasksJSON.getString(i));
            }
            this.subtasks = subtasks;
            JSONArray location = jsonTask.getJSONArray("coords");
            this.latitude = location.getDouble(0);
            this.longitude = location.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task(int id, String title, String description, Date due) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.due = due;
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(String subtask) {
        this.subtasks.add(subtask);
    }

    public int getId() {
        return this.id == null ? 0 : this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getDue() {
        return this.due;
    }

    public ArrayList<String> getSubtasks() {
        return new ArrayList<>(this.subtasks);
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            if (this.id != null) {
                result.put("id", this.id);
            }
            result.put("title", this.title);
            result.put("description", this.description);
            result.put("due", this.due.getTime() / 1000);

            JSONArray subtasks = new JSONArray();
            for (String subtask : this.subtasks) {
                subtasks.put(subtask);
            }
            result.put("subtasks", subtasks);

            JSONArray coords = new JSONArray();
            coords.put(this.latitude);
            coords.put(this.longitude);
            result.put("coords", coords);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public LatLng location() {
        return new LatLng(this.latitude, this.longitude);
    }
}
