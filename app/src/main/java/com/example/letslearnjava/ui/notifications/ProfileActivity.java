package com.example.letslearnjava.ui.notifications;

import static com.example.letslearnjava.DbQuery.g_usersCount;
import static com.example.letslearnjava.DbQuery.g_usersList;
import static com.example.letslearnjava.DbQuery.myPerformance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.letslearnjava.DbQuery;
import com.example.letslearnjava.LoginActivity;
import com.example.letslearnjava.MainActivity;
import com.example.letslearnjava.MyCompletedListener;
import com.example.letslearnjava.MyProfileActivity;
import com.example.letslearnjava.R;
import com.example.letslearnjava.SplashActivity;
import com.example.letslearnjava.databinding.FragmentNotificationsBinding;
import com.example.letslearnjava.ui.dashboard.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class ProfileActivity extends Fragment {

    private LinearLayout logoutB;
    private TextView profile_img_text, name, score, rank;
    private LinearLayout leaderB, profileB,bookmarksB;
    private BottomNavigationView bottomNavigationView;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_notifications,container, false);
        
        initViews(view);

        String userName = DbQuery.myProfile.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0,1));
        name.setText(userName);

        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if (DbQuery.g_usersList.size() == 0){
            DbQuery.getTopUsers(new MyCompletedListener() {
                @Override
                public void onSuccess() {

                    if (myPerformance.getScore() != 0){
                        if(! DbQuery.isMeOnTopList){
                            calculateRank();
                        }
                        score.setText("Ұпай: " + myPerformance.getScore());
                        rank.setText("Pейтинг - " + myPerformance.getRank());
                    }
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "қате", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            score.setText("Ұпай: " + myPerformance.getScore());
            if (myPerformance.getScore() != 0)
                rank.setText("Pейтинг - " + myPerformance.getRank());
        }

        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    private void initViews(View view) {

        logoutB = view.findViewById(R.id.logoutB);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.name);
        score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        profileB = view.findViewById(R.id.profileB);

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