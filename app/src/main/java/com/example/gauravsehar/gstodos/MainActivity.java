package com.example.gauravsehar.gstodos;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView todoListView;
    RadioButton todoDoneRadioButton;
    EditText addTodoEditText;
    Button addTodoButton;
    FloatingActionButton fab;

    ArrayList<Todo> mTodos;
    TodoAdapter mAdapter;
    TodoOpenHelper todoOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Actions
        defaultActions();

        //Finding All UI Elements
        uiElementsFinder();

        //Setting Adapter on ListView
        listViewAdapterSetter();


        //SETTING ADD FIELD GONE
        toggleAddTodoField(false);

        //UI Elements Listener Setter
        uiElementsListenerSetter();
    }

    private void defaultActions() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void uiElementsFinder() {
        //Finding All UI Elements
        todoListView = findViewById(R.id.todoListView);
        todoDoneRadioButton = findViewById(R.id.doneTodoRadioButton);
        addTodoEditText = findViewById(R.id.todoNameEditText);
        addTodoButton = findViewById(R.id.addTodoButton);
        fab = findViewById(R.id.fab);
        todoListView = findViewById(R.id.todoListView);
    }

    private void listViewAdapterSetter() {
        todoOpenHelper = TodoOpenHelper.getInstance(this);
//        mTodos = Todo.getDummyTodo(15);
        mTodos = fetchTodosFromDB();
        mAdapter = new TodoAdapter(this, mTodos);
        todoListView.setAdapter(mAdapter);
    }

    private ArrayList<Todo> fetchTodosFromDB() {
        ArrayList<Todo> todos = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = todoOpenHelper.getReadableDatabase();
        //todo columns optimizing
        Cursor cursor = sqLiteDatabase.query(Contract.Todo.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Todo.ID));
            String name = cursor.getString(cursor.getColumnIndex(Contract.Todo.NAME));
            int todoComplete = cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_COMPLETED));
            int alarmset = cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_ALARMSET));
            int description = cursor.getInt(cursor.getColumnIndex(Contract.Todo.IS_DESCRIPTION_NULL));
            // todo garbage run
            Todo todo = new Todo(id, name, null, Convert.intToBool(description), 0, 0, false, Convert.intToBool(todoComplete), Convert.intToBool(alarmset));
            todos.add(todo);
        }
        cursor.close();
        return todos;
    }

    private void toggleAddTodoField(boolean showAddTodoField) {
        if (showAddTodoField) {
            fab.setVisibility(View.GONE);
            todoDoneRadioButton.setVisibility(View.VISIBLE);
            addTodoEditText.setVisibility(View.VISIBLE);
            addTodoButton.setVisibility(View.VISIBLE);
        } else {
            todoDoneRadioButton.setVisibility(View.GONE);
            addTodoEditText.setVisibility(View.GONE);
            addTodoButton.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void uiElementsListenerSetter() {
        //ON CLICK LISTENER ON FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SETTING UP ADDING FACILITY
                toggleAddTodoField(true);

                //REQUESTING KEYBOARD FOCUS
                addTodoEditText.requestFocus();
                showSoftKeyboard(addTodoEditText);
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
//                if (inputMethodManager != null) {
//                    inputMethodManager.showSoftInput(addTodoEditText, InputMethodManager.SHOW_IMPLICIT);
//                } else
//                    Log.d("debugGS", "onClick: fabInputManager ERROR");
            }
        });

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Snackbar.make(view,"click handled",Snackbar.LENGTH_INDEFINITE).show();
//                Log.d("tag", "onItemClick: handled");
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ID_KEY, (int) id);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.DETAIL_ACTIVITY_REQUEST);
            }
        });

        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Todo?");
                builder.setMessage("Are you sure to delete this Todo? You cannot undo it.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
                        String[] ids = {id + ""};
                        database.delete(Contract.Todo.TABLE_NAME, Contract.Todo.ID + " = ?", ids);
                        mTodos.remove(position);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo todo = new Todo(addTodoEditText.getText().toString(), "", true, 0, 0, false, todoDoneRadioButton.isChecked(), false);
                SQLiteDatabase sqLiteDatabase = todoOpenHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.Todo.NAME, addTodoEditText.getText().toString());
                contentValues.put(Contract.Todo.DESCRIPTION, "");
                contentValues.put(Contract.Todo.IS_DESCRIPTION_NULL, true);
                contentValues.put(Contract.Todo.PRIORITY, 0);
                contentValues.put(Contract.Todo.DUE_TIME_IN_MILLIS, 0);
                contentValues.put(Contract.Todo.IS_COMPLETED, todoDoneRadioButton.isChecked());
                contentValues.put(Contract.Todo.IS_TAGGED, false);
                contentValues.put(Contract.Todo.IS_ALARMSET, false);
                long id = sqLiteDatabase.insert(Contract.Todo.TABLE_NAME, null, contentValues);
                todo.setId((int) id);
                Toast.makeText(MainActivity.this, String.valueOf(id), Toast.LENGTH_LONG).show();
                mTodos.add(todo);
                Log.d("TAG", todo.getName());
                mAdapter.notifyDataSetChanged();
                addTodoEditText.setText("");
                todoDoneRadioButton.setChecked(false);
            }
        });
    }

    public void showSoftKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case Constants.DETAIL_ACTIVITY_REQUEST:
                    if (resultCode == Constants.SAVE_SUCCESS_RESULT) {
                        mAdapter.notifyDataSetChanged();
//                        if (bundle != null) {
//                            int position = bundle.getInt(Constants.LISTVIEW_POSITION_KEY, -1);
//                            if (position >= 0) {
//                                Todo todo = getTodoFr(bundle);
//                                mExpenses.set(position,expense);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fab.getVisibility() == View.GONE) {
            toggleAddTodoField(false);
        } else
            super.onBackPressed();
    }
}
