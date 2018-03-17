package com.example.gauravsehar.gstodos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gaurav Sehar on 18-Feb-18.
 */

public class TodoAdapter extends BaseAdapter {
    private Context mContext;
    TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(mContext);
    private ArrayList<Todo> mTodo;

    public TodoAdapter(Context mContext, ArrayList<Todo> mTodo) {
        this.mContext = mContext;
        this.mTodo = mTodo;
    }

    @Override
    public int getCount() {
        return mTodo.size();
    }

    @Override
    public Todo getItem(int position) {
        return mTodo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = convertView;
        if (convertView == null) {
            convertView = layoutInflater != null ? layoutInflater.inflate(R.layout.list_element_main, parent, false) : null;
            TodoViewHolder todoViewHolder = new TodoViewHolder(convertView);
            convertView.setTag(todoViewHolder);
        }
        final TodoViewHolder todoViewHolder = (TodoViewHolder) convertView.getTag();

        final Todo todo = getItem(position);

        if (todo.getCompleted()) {
            todoViewHolder.todoDoneCheckBox.setChecked(true);
            todoViewHolder.todoNameStrikeout(true);
        } else {
            todoViewHolder.todoDoneCheckBox.setChecked(false);
            todoViewHolder.todoNameStrikeout(false);
        }
        if (todo.getAlarmSet())
            todoViewHolder.todoAlarmSet.setVisibility(View.VISIBLE);
        else
            todoViewHolder.todoAlarmSet.setVisibility(View.INVISIBLE);
        if (todo.getDescriptionNull())
            todoViewHolder.todoDescription.setVisibility(View.INVISIBLE);
        else
            todoViewHolder.todoDescription.setVisibility(View.VISIBLE);
        todoViewHolder.todoNameTextView.setText(todo.getName());
        todoViewHolder.todoDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todoViewHolder.todoNameStrikeout(isChecked);
                dbUpdate(todo.getId(), isChecked);
            }
        });
        return convertView;

    }

    private void dbUpdate(int id, Boolean isCompleted) {

        SQLiteDatabase sqLiteDatabase = todoOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Todo.IS_COMPLETED, isCompleted);
        String ids[] = {id + ""};
        sqLiteDatabase.update(Contract.Todo.TABLE_NAME, contentValues, Contract.Todo.ID + " = ? ", ids);
//        todo.setId((int) id);
//        Toast.makeText(DetailActivity.this, String.valueOf(id), Toast.LENGTH_LONG).show();
    }

    class TodoViewHolder {

        TextView todoNameTextView;
        CheckBox todoDoneCheckBox;
        ImageView todoAlarmSet;
        ImageView todoDescription;

        TodoViewHolder(View rowLayout) {
            todoNameTextView = rowLayout.findViewById(R.id.todoNameTextView);
            todoDoneCheckBox = rowLayout.findViewById(R.id.todoDoneCheckBox);
            todoAlarmSet = rowLayout.findViewById(R.id.todoAlarmImageView);
            todoDescription = rowLayout.findViewById(R.id.todoTagImageView);
        }

        private void todoNameStrikeout(boolean setStrikeoutText) {
            if (setStrikeoutText)
                todoNameTextView.setPaintFlags(todoNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                todoNameTextView.setPaintFlags(todoNameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
