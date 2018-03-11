package com.example.gauravsehar.gstodos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText todoNameEditText, todoTagsEditText, todoDescriptionEditText;
    CheckBox todoDoneCheckBox;
    TextView todoDueDateTextView, todoDueTimeTextView;
    Spinner todoPrioritySpinner;
    Switch todoAlarmSwitch;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity Intent Finder
        intentFinder();

        setContentView(R.layout.activity_detail);

        //Finding All UI Elements
        uiElementsFinder();

        //UI Elements Listener Setter
        uiElementsListenerSetter();
    }

    private void intentFinder() {
        Intent intent = getIntent();
        bundle = intent.getExtras();
        //If Bundle Find Display Bundle Values
        if (bundle != null)
            populateDataFromBundle();
        else
            bundle = new Bundle();
    }

    private void uiElementsFinder() {
        todoNameEditText = findViewById(R.id.todoNameEditText);
        todoTagsEditText = findViewById(R.id.todoTagsEditText);
        todoDescriptionEditText = findViewById(R.id.todoDescriptionEditText);
        todoDoneCheckBox = findViewById(R.id.todoDoneCheckBox);
        todoDueDateTextView = findViewById(R.id.todoDateEditText);
        todoDueTimeTextView = findViewById(R.id.todoTimeEditText);
        todoPrioritySpinner = findViewById(R.id.todoPrioritySpinner);
        todoAlarmSwitch = findViewById(R.id.todoAlarmSwitch);
    }

    private void uiElementsListenerSetter() {
        todoDueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        todoDueTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time Picker");
            }
        });

        todoDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    todoNameEditText.setPaintFlags(todoNameEditText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                else
                    todoNameEditText.setPaintFlags(todoNameEditText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });


    }

    private void populateDataFromBundle() {
        int id = bundle.getInt(Constants.ID_KEY, -1);
        if (id > 0) {
            TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(this);
            SQLiteDatabase sqLiteDatabase = todoOpenHelper.getReadableDatabase();
            String[] selectionArgs = {id + ""};
            Cursor cursor = sqLiteDatabase.query(Contract.Todo.TABLE_NAME, null, Contract.Todo.ID + " = ?", selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(Contract.Todo.NAME));
//                String desc = cursor.getString(cursor.getColumnIndex(Contract.Todo.DESCRIPTION));
//                int priority = cursor.getInt(cursor.getColumnIndex(Contract.Todo.PRIORITY));
//                long epochTime = cursor.getLong(cursor.getColumnIndex(Contract.Todo.DUE_TIME_IN_MILLIS));
//                boolean done = Convert.intToBool(cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_COMPLETED)));
//                boolean alarmset = Convert.intToBool(cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_ALARMSET)));
//
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(epochTime);
//                String date = simpleDateFormat.format(calendar.getTime()).substring(0,11);
//                String time = simpleDateFormat.format(calendar.getTime()).substring(12,20);

                todoNameEditText.setText(name);
//                todoDueDateTextView.setText(date);
//                todoDueTimeTextView.setText(time);
//                todoDoneCheckBox.setChecked(done);
//                todoPrioritySpinner.setSelection(priority, true);
//                todoDescriptionEditText.setText(desc);
//                todoAlarmSwitch.setChecked(alarmset);
            }
            cursor.close();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        String today = formatter.format(date);
        todoDueDateTextView.setText(today);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String AM_PM = (hourOfDay >= 12) ? "PM" : "AM";
        Date time = calendar.getTime();
        DateFormat hourFormat = new SimpleDateFormat("hh");
        String newHour = hourFormat.format(time);
        String formattedMinute = String.format("%02d", minute);
        String displayString = newHour + ":" + formattedMinute + " " + AM_PM;
        todoDueTimeTextView.setText(displayString);
    }
}
