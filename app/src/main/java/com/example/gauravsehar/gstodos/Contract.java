package com.example.gauravsehar.gstodos;

/**
 * Created by Gaurav Sehar on 10-Mar-18.
 */

public class Contract {
    public static final String DATABASE_NAME = "gstodos_db";
    public static final int VERSION = 1;

    static class Todo {
        public static final String TABLE_NAME = "todo";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String IS_DESCRIPTION_NULL = "descriptionNull";
        public static final String PRIORITY = "priority";
        public static final String DUE_TIME_IN_MILLIS = "epochTime";
        public static final String IS_COMPLETED = "completed";
        public static final String IS_TAGGED = "tagged";
        public static final String IS_ALARMSET = "alarm";
    }

    static class Tags {
        public static final String TABLE_NAME = "tag";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    static class TagsHolder {
        public static final String TABLE_NAME = "tagsHolder";
        public static final String ID = "id";
        public static final String ID_OF_TAGS = "tagsID";
        public static final String ID_OF_TODO = "todoID";
    }
}
