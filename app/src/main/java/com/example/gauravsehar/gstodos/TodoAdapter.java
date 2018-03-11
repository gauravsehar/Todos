package com.example.gauravsehar.gstodos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gaurav Sehar on 18-Feb-18.
 */

public class TodoAdapter extends BaseAdapter {
    private Context mContext;
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
        View view = convertView;
        if (view == null) {
            view = layoutInflater != null ? layoutInflater.inflate(R.layout.list_element_main, parent, false) : null;
            TodoViewHolder todoViewHolder = new TodoViewHolder(view);
            view.setTag(todoViewHolder);
        }
        TodoViewHolder todoViewHolder = (TodoViewHolder) view.getTag();
        Todo todo = getItem(position);

        todoViewHolder.todoNameTextView.setText(todo.getName());
        return view;

    }

    class TodoViewHolder {

        TextView todoNameTextView;

        TodoViewHolder(View rowLayout) {
            todoNameTextView = rowLayout.findViewById(R.id.todoNameTextView);
        }
    }
}
