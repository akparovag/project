package com.example.letslearnjava;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.example.letslearnjava.Models.CategoryModel;
import com.example.letslearnjava.Models.LessonModel;
import com.example.letslearnjava.Models.ProfileModel;
import com.example.letslearnjava.Models.QuestionModel;
import com.example.letslearnjava.Models.RankModel;
import com.example.letslearnjava.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore g_firestore;
    public static List<CategoryModel> g_catList = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static List<TestModel> g_testList = new ArrayList<>();
    public static int g_selected_test_index = 0;
    public static List<LessonModel> g_lessonList = new ArrayList<>();
    public static List<QuestionModel> g_quesList = new ArrayList<>();
    public static List<RankModel> g_usersList = new ArrayList<>();
    public static int g_usersCount = 0;
    public static boolean isMeOnTopList = false;
    public static ProfileModel myProfile = new ProfileModel("NA",null, null);
    public static RankModel myPerformance = new RankModel( "NULL",0,-1);
    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;

    public static void createUserData(String email, String name, MyCompletedListener completedListener){
        Map<String , Object> userData = new ArrayMap<>();

        userData.put("EMAIL_ID",email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc,"COUNT", FieldValue.increment(1));

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completedListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completedListener.onFailure();
            }
        });
    }

    public static void saveProfileData(String name, String phone, MyCompletedListener completedListener){
        Map<String, Object> profileData = new ArrayMap<>();

        profileData.put("NAME", name);
        if (phone != null){
            profileData.put("PHONE", phone);
        }

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);

                        if (phone != null)
                            myProfile.setPhone(phone);

                        completedListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }

    public static void getUserData(final MyCompletedListener completedListener) {
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            myProfile.setName(documentSnapshot.getString("NAME"));
                            myProfile.getEmail(documentSnapshot.getString("EMAIL_ID"));

                            if (documentSnapshot.getString("PHONE") != null)
                                myProfile.setPhone(documentSnapshot.getString("PHONE"));

                            Long totalScoreLong = documentSnapshot.getLong("TOTAL_SCORE");
                            if (totalScoreLong != null) {
                                myPerformance.setScore(totalScoreLong.intValue());
                            }

                            myPerformance.setName(documentSnapshot.getString("NAME"));

                            completedListener.onSuccess();
                        } else {
                            completedListener.onFailure();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }


    public static void loadMyScores(MyCompletedListener completedListener){
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i=0; i<g_testList.size(); i++){
                            int top =0;
                            if (documentSnapshot.get(g_testList.get(i).getTestID()) != null){
                                top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }
                            g_testList.get(i).setTopScore(top);
                        }
                        completedListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }

    public static void getTopUsers(final MyCompletedListener completedListener){
        g_usersList.clear();
        String myUID = FirebaseAuth.getInstance().getUid();
        g_firestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            g_usersList.add(new RankModel(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));
                            if (myUID.compareTo(doc.getId()) == 0){
                                isMeOnTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;
                        }
                        completedListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }

    public static void getUsersCount(final MyCompletedListener completedListener){
        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_usersCount = documentSnapshot.getLong("COUNT").intValue();
                        completedListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }

    public static void saveResult(final int score, MyCompletedListener completedListener){
        WriteBatch batch = g_firestore.batch();

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        batch.update(userDoc, "TOTAL_SCORE", score);

        if(score > g_testList.get(g_selected_test_index).getTopScore()){
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");
            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestID(), score);

            batch.set(scoreDoc, testData, SetOptions.merge());
        }
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (score > g_testList.get(g_selected_test_index).getTopScore())
                    g_testList.get(g_selected_test_index).setTopScore(score);

                myPerformance.setScore(score);
                completedListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completedListener.onFailure();
            }
        });
    }

    public static void loadCategories(final MyCompletedListener completedListener){
        g_catList.clear();

        g_firestore.collection("QUIZ").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    docList.put(doc.getId(), doc);
                }
                QueryDocumentSnapshot catListDooc = docList.get("Categories");
                long catCount = catListDooc.getLong("COUNT");
                for (int i = 1; i <= catCount; i++){
                    String catID = catListDooc.getString("CAT" + String.valueOf(i) + "_ID");
                    QueryDocumentSnapshot catDoc = docList.get(catID);
                    int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();
                    String catName = catDoc.getString("NAME");
                    g_catList.add(new CategoryModel(catID, catName, noOfTest));
                }
                completedListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completedListener.onFailure();
            }
        });
    }

    public static void loadlessons(final MyCompletedListener completedListener){
        g_lessonList.clear();
        g_firestore.collection("Lessons")
                .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDovID())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            g_lessonList.add(new LessonModel(
                                    doc.getString("LESSONS")
                            ));
                        }
                        completedListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }

    public static void loadquestions(final MyCompletedListener completedListener){
        g_quesList.clear();
        g_firestore.collection("Questions")
                .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDovID())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            g_quesList.add(new QuestionModel(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(), -1, NOT_VISITED
                            ));
                        }
                        completedListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completedListener.onFailure();
                    }
                });
    }

    public static void loadTestData(final MyCompletedListener completedListener) {
        g_testList.clear();

        g_firestore.collection("QUIZ")
                .document(g_catList.get(g_selected_cat_index).getDovID())
                .collection("TESTS_LIST")
                .document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new LoadTestSuccessListener(completedListener))
                .addOnFailureListener(new LoadTestFailureListener(completedListener));
    }

    public static void loadData(MyCompletedListener compledtedListener){
        loadCategories(new MyCompletedListener() {
            @Override
            public void onSuccess() {
                getUsersCount(new MyCompletedListener() {
                    @Override
                    public void onSuccess() {
                        getUserData(compledtedListener);
                    }
                    @Override
                    public void onFailure() {
                        compledtedListener.onFailure();
                    }
                });
            }
            @Override
            public void onFailure() {
                compledtedListener.onFailure();
            }
        });
    }

    private static class LoadTestSuccessListener implements OnSuccessListener<DocumentSnapshot> {
        private final MyCompletedListener completedListener;

        public LoadTestSuccessListener(MyCompletedListener completedListener) {
            this.completedListener = completedListener;
        }

        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            int noOfTests = g_catList.get(g_selected_cat_index).getNoOfTests();
            for (int i = 1; i <= noOfTests; i++) {
                String testId = documentSnapshot.getString("TEST" + i + "_ID");
                Long testTimeLong = documentSnapshot.getLong("TEST" + i + "_TIME");
                int testTime = (testTimeLong != null) ? testTimeLong.intValue() : 0;
                g_testList.add(new TestModel(testId, 1, testTime));
            }
            completedListener.onSuccess();
        }
    }

    private static class LoadTestFailureListener implements OnFailureListener {
        private final MyCompletedListener completedListener;

        public LoadTestFailureListener(MyCompletedListener completedListener) {
            this.completedListener = completedListener;
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            completedListener.onFailure();
        }
    }
}
