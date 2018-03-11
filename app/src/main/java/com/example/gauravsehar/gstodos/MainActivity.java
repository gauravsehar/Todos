package com.example.gauravsehar.gstodos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView todoListView;
    RadioButton addTodoDoneRadioButton;
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
        addTodoDoneRadioButton = findViewById(R.id.doneTodoRadioButton);
        addTodoEditText = findViewById(R.id.todoNameEditText);
        addTodoButton = findViewById(R.id.addTodoButton);
        fab = findViewById(R.id.fab);
        todoListView = findViewById(R.id.todoListView);
    }

    private void listViewAdapterSetter() {
        todoOpenHelper = TodoOpenHelper.getInstance(this);
        mTodos = Todo.getDummyTodo(15);
//        mTodos = fetchTodosFromDB();
        mAdapter = new TodoAdapter(this, mTodos);
        todoListView.setAdapter(mAdapter);
    }

    private void toggleAddTodoField(boolean showAddTodoField) {
        if (showAddTodoField) {
            fab.setVisibility(View.GONE);
            addTodoDoneRadioButton.setVisibility(View.VISIBLE);
            addTodoEditText.setVisibility(View.VISIBLE);
            addTodoButton.setVisibility(View.VISIBLE);
        } else {
            addTodoDoneRadioButton.setVisibility(View.GONE);
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
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivityForResult(intent, Constants.ADD_TODO_REQUEST);

//                //SETTING UP ADDING FACILITY
//                toggleAddTodoField(true);
//
//                //REQUESTING KEYBOARD FOCUS
//                addTodoEditText.requestFocus();
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

//                        SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
//                        String[] ids = {id + ""};
//                        database.delete(Contract.Todo.TABLE_NAME, Contract.Todo.ID + " = ?", ids);
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
