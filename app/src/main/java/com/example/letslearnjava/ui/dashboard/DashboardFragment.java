package com.example.letslearnjava.ui.dashboard;

import static com.example.letslearnjava.DbQuery.g_usersCount;
import static com.example.letslearnjava.DbQuery.g_usersList;
import static com.example.letslearnjava.DbQuery.myPerformance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letslearnjava.Adapters.RankAdapter;
import com.example.letslearnjava.DbQuery;
import com.example.letslearnjava.MyCompletedListener;
import com.example.letslearnjava.R;
import com.example.letslearnjava.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private TextView totalUsersTV, myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        View  view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initViews(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.g_usersList);
        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompletedListener() {  //загрузка данных из firebase
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                if (myPerformance.getScore() != 0){
                    if(! DbQuery.isMeOnTopList){
                        calculateRank();
                    }
                    myScoreTV.setText("Ұпай: " + myPerformance.getScore());
                    myRankTV.setText("Pейтинг - " + myPerformance.getRank());
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "қате", Toast.LENGTH_SHORT).show();
            }
        });

        totalUsersTV.setText("Жалпы пайдаланушылар: " + DbQuery.g_usersCount);
        myImgTextTV.setText(myPerformance.getName().toUpperCase().substring(0,1));

        return view;
    }

    private void initViews(View view) {
        totalUsersTV = view.findViewById(R.id.total_users);
        myImgTextTV = view.findViewById(R.id.img_text);
        myScoreTV = view.findViewById(R.id.total_score);
        myRankTV = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.users_view);
    }

    private void calculateRank(){
        int lowTopScore = g_usersList.get(g_usersList.size() - 1).getScore();
        int remaining_slots = g_usersCount - 20;
        int myslot = (myPerformance.getScore()*remaining_slots) / lowTopScore;
        int rank;
        if (lowTopScore != myPerformance.getScore()){
            rank = g_usersCount - myslot;
        } else {
            rank = 21;
        }
        myPerformance.setRank(rank);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}