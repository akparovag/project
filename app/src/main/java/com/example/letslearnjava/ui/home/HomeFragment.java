package com.example.letslearnjava.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.letslearnjava.Adapters.CategoryAdapter;
import com.example.letslearnjava.DbQuery;
import com.example.letslearnjava.R;
import com.example.letslearnjava.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private GridView catView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        catView = view.findViewById(R.id.cat_Grid);

        //loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        catView.setAdapter(adapter);

        return view;
    }


}