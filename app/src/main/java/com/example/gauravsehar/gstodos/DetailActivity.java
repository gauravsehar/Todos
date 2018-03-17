package com.example.gauravsehar.gstodos;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
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
    TodoOpenHelper todoOpenHelper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        //Finding All UI Elements
        uiElementsFinder();

        //Activity Intent Finder
        intentFinder();

        //UI Elements Listener Setter
        uiElementsListenerSetter();
    }

    private void intentFinder() {
        Intent intent = getIntent();
        bundle = intent.getExtras();
        //If Bundle Find Display Bundle Values
        if (bundle != null)
            populateDataFromDB();
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
                todoNameStrikeout(isChecked);
            }
        });
    }

    private void todoNameStrikeout(boolean setStrikeoutText) {
        if (setStrikeoutText)
            todoNameEditText.setPaintFlags(todoNameEditText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            todoNameEditText.setPaintFlags(todoNameEditText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void populateDataFromDB() {
        id = bundle.getInt(Constants.ID_KEY, -1);
        Toast.makeText(DetailActivity.this, String.valueOf(id), Toast.LENGTH_LONG).show();
        if (id > 0) {
            TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(this);
            SQLiteDatabase sqLiteDatabase = todoOpenHelper.getReadableDatabase();
            String[] selectionArgs = {id + ""};
            Cursor cursor = sqLiteDatabase.query(Contract.Todo.TABLE_NAME, null, Contract.Todo.ID + " = ?", selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(Contract.Todo.NAME));
                String desc = cursor.getString(cursor.getColumnIndex(Contract.Todo.DESCRIPTION));
                int priority = cursor.getInt(cursor.getColumnIndex(Contract.Todo.PRIORITY));
                long epochTime = cursor.getLong(cursor.getColumnIndex(Contract.Todo.DUE_TIME_IN_MILLIS));
                boolean done = Convert.intToBool(cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_COMPLETED)));
                boolean alarmSet = Convert.intToBool(cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_ALARMSET)));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(epochTime);
                String date = simpleDateFormat.format(calendar.getTime()).substring(0, 11);
                String time = simpleDateFormat.format(calendar.getTime()).substring(12, 20);

                if (done)
                    todoNameStrikeout(true);
                todoNameEditText.setText(name);
                if (epochTime != 0) {
                    todoDueDateTextView.setText(date);
                    todoDueTimeTextView.setText(time);
                }
                todoDoneCheckBox.setChecked(done);
                todoPrioritySpinner.setSelection(priority, true);
                todoDescriptionEditText.setText(desc);
                todoAlarmSwitch.setChecked(alarmSet);
            }
            cursor.close();
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
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

    private long dateTimeInMillis() {
        String dateTime = todoDueDateTextView.getText().toString() +
                " " + todoDueTimeTextView.getText().toString();
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(DetailActivity.this, "dateTimeInMillis Parsing Error.", Toast.LENGTH_LONG).show();
        }
        return date != null ? date.getTime() : 0;
    }

    @Override
    public void onBackPressed() {
//        +odo +odo = new +odo(addTodoEditText.getText().toString(), "", true, 0, 0, false, todoDoneRadioButton.isChecked(), false);
        id = bundle.getInt(Constants.ID_KEY, -1);

        if (todoAlarmSwitch.isChecked()) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(DetailActivity.this, AlarmReceiver.class);
            intent.putExtra(Constants.NOTIFICATION_KEY, Constants.NOTIFICATION_CHANNEL_KEY);
            intent.putExtra(Constants.NAME_KEY, todoNameEditText.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(DetailActivity.this, 1, intent, 0);
            Toast.makeText(DetailActivity.this, String.valueOf(dateTimeInMillis()), Toast.LENGTH_LONG).show();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dateTimeInMillis(), 60000, pendingIntent);
        }

        todoOpenHelper = TodoOpenHelper.getInstance(this);
        SQLiteDatabase sqLiteDatabase = todoOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Todo.NAME, todoNameEditText.getText().toString());
        contentValues.put(Contract.Todo.DESCRIPTION, todoDescriptionEditText.getText().toString());

        contentValues.put(Contract.Todo.PRIORITY, todoPrioritySpinner.getSelectedItemPosition());

        contentValues.put(Contract.Todo.IS_COMPLETED, todoDoneCheckBox.isChecked());
        contentValues.put(Contract.Todo.IS_TAGGED, false);
        contentValues.put(Contract.Todo.IS_ALARMSET, todoAlarmSwitch.isChecked());

        if (todoDescriptionEditText.getText().toString().matches(""))
            contentValues.put(Contract.Todo.IS_DESCRIPTION_NULL, true);
        else
            contentValues.put(Contract.Todo.IS_DESCRIPTION_NULL, false);

        contentValues.put(Contract.Todo.DUE_TIME_IN_MILLIS, dateTimeInMillis());
        String ids[] = {id + ""};
        long id = sqLiteDatabase.update(Contract.Todo.TABLE_NAME, contentValues, Contract.Todo.ID + " = ? ", ids);
//        todo.setId((int) id);
        Toast.makeText(DetailActivity.this, String.valueOf(id), Toast.LENGTH_LONG).show();
        setResult(Constants.SAVE_SUCCESS_RESULT);
        super.onBackPressed();
    }
}
