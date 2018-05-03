package com.weebly.bottleneckdevelopers.tkzy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.weebly.bottleneckdevelopers.tkzy.models.User;

@SuppressWarnings("FieldCanBeLocal")
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    //widgets
    private EditText mName, mEmail, mPassword, mConfirmPassword, mPhoneNumber;
    private Button mRegister;
    private ProgressBar mProgressBar;
    private Spinner mSemester, mBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.etNameRegisterActivity);
        mEmail = findViewById(R.id.etEmailRegisterActivity);
        mPassword = findViewById(R.id.etPasswordRegisterActivity);
        mConfirmPassword = findViewById(R.id.etPasswordAgainRegisterActivity);
        mRegister = findViewById(R.id.btRegisterOnRegisterActivity);
        mProgressBar = findViewById(R.id.progressBarRegisterActivity);
        mBranch = findViewById(R.id.spBranchSelectorRegisterActivity);
        mPhoneNumber = findViewById(R.id.etPhoneNumberRegisterActivity);
        mSemester = findViewById(R.id.spSemesterRegisterActivity);

        mProgressBar.setVisibility(View.INVISIBLE);

        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if (!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())
                        && !isEmpty(mName.getText().toString())
                        ) {

                    if (doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())) {
                        registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideSoftKeyboard();


    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Verification E-mail sent to " + mEmail.getText().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Verification E-mail Not sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void registerNewEmail(String email, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: onComplete: " + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();

                            User user = new User();
                            user.setBranch(mBranch.getSelectedItem().toString());
                            user.setEmail(mEmail.getText().toString());
                            user.setName(mName.getText().toString());
                            user.setPhone(mPhoneNumber.getText().toString());
                            user.setProfile_image("");
                            user.setSecurity_level("1");
                            user.setSemester(mSemester.getSelectedItem().toString());
                            user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.dbnode_users))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(RegisterActivity.this, "DB SUCCESS", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    redirectLoginScreen();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(RegisterActivity.this, "DB INSERTION FAILED", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    redirectLoginScreen();

                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Unable to Register", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                }
        );
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Return true if @param 's1' matches @param 's2'
     *
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }

    /**
     * Return true if the @param is null
     *
     * @param string
     * @return
     */
    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void redirectLoginActivityFromRegisterActivity(View view) {
        redirectLoginScreen();
    }

    private void redirectLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
