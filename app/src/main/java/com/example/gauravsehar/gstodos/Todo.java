package com.example.gauravsehar.gstodos;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gaurav Sehar on 18-Feb-18.
 */

public class Todo {
    private String name, description;
    private Boolean isCompleted, isTagged, isAlarmSet, isDescriptionNull;
    private int priority;
    private long dueTimeInMillis;
    private int id;

    public Todo(int id, String name, String description, boolean nullDescription, int priority, long dueTimeInMillis, boolean isTagged, boolean isCompleted, boolean isAlarmSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDescriptionNull = nullDescription;
        this.priority = priority;
        this.dueTimeInMillis = dueTimeInMillis;
        this.isTagged = isTagged;
        this.isCompleted = isCompleted;
        this.isAlarmSet = isAlarmSet;
    }

    public Todo(String name, String description, boolean nullDescription, int priority, long dueTimeInMillis, boolean isTagged, boolean isCompleted, boolean isAlarmSet) {
        this.name = name;
        this.description = description;
        this.isDescriptionNull = nullDescription;
        this.priority = priority;
        this.dueTimeInMillis = dueTimeInMillis;
        this.isTagged = isTagged;
        this.isCompleted = isCompleted;
        this.isAlarmSet = isAlarmSet;
    }

    static ArrayList<Todo> getDummyTodo(int size) {
        ArrayList<Todo> todoArrayList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Todo todo = new Todo("Title " + i, "Description " + i, false, 0, 1000, false, false, false);
            todoArrayList.add(todo);
            Log.d("DEBUG", "exe");

        }
        return todoArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getTagged() {
        return isTagged;
    }

    public void setTagged(Boolean tagged) {
        isTagged = tagged;
    }

    public Boolean getAlarmSet() {
        return isAlarmSet;
    }

    public void setAlarmSet(Boolean alarmSet) {
        isAlarmSet = alarmSet;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getDueTimeInMillis() {
        return dueTimeInMillis;
    }

    public void setDueTimeInMillis(long dueTimeInMillis) {
        this.dueTimeInMillis = dueTimeInMillis;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getDescriptionNull() {
        return isDescriptionNull;
    }

    public void setDescriptionNull(Boolean descriptionNull) {
        isDescriptionNull = descriptionNull;
    }
}
