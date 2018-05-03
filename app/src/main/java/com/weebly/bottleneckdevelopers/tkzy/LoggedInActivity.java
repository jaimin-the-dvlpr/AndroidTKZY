package com.weebly.bottleneckdevelopers.tkzy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.weebly.bottleneckdevelopers.tkzy.models.User;

@SuppressWarnings("FieldCanBeLocal")
public class LoggedInActivity extends AppCompatActivity {

    private TextView mWelcomeTV, mLogOut, mBranchName, mCurrentSem;
    private ProgressBar mProgress;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mWelcomeTV = findViewById(R.id.tvWelcomeLoggedInActivity);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mLogOut = findViewById(R.id.tvLogOut);
        mBranchName = findViewById(R.id.tvBranchNameLoggedInActivity);
        mCurrentSem = findViewById(R.id.tvCurrentSemesterLoggedInActivity);
        mProgress = findViewById(R.id.progressBarLoggedInActivity);

        mWelcomeTV.setVisibility(View.INVISIBLE);
        mBranchName.setVisibility(View.INVISIBLE);
        mCurrentSem.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);

        if (user != null) {
            getUserAccountData();
        }

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LoggedInActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getUserAccountData() {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

        /*
            -------- Query Method 1 --------
         */
        Query query1 = dbReference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    User user = singleSnapshot.getValue(User.class);
                    mWelcomeTV.setText(getString(R.string.welcome) + " " + user.getName());
                    mBranchName.setText(user.getBranch());
                    mCurrentSem.setText("Semester: " + user.getSemester());

                    mWelcomeTV.setVisibility(View.VISIBLE);
                    mBranchName.setVisibility(View.VISIBLE);
                    mCurrentSem.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
            -------- Query Method 2 --------
         */
        /*
        Query query2 = dbReference.child(getString(R.string.dbnode_users))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    User user = singleSnapshot.getValue(User.class);
                    mWelcomeTV.setText(getString(R.string.welcome) + " " + user.getName());
                    mBranchName.setText(user.getBranch());
                    mCurrentSem.setText("Semester: " + user.getSemester());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }

    private void checkAuthenticationState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(LoggedInActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

}
