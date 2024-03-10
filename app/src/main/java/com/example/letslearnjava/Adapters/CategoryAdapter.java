package com.example.letslearnjava.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.letslearnjava.DbQuery;
import com.example.letslearnjava.Models.CategoryModel;
import com.example.letslearnjava.R;
import com.example.letslearnjava.TestActivity;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> cat_list;

    public CategoryAdapter(List<CategoryModel> cat_list){
        this.cat_list = cat_list;
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View myView;

        if (view == null){
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout,viewGroup, false);
        }else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbQuery.g_selected_cat_index = position;
                Intent intent = new Intent(view.getContext(), TestActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        TextView catName = myView.findViewById(R.id.catName);

        catName.setText(cat_list.get(position).getName());
        return myView;
    }
}
