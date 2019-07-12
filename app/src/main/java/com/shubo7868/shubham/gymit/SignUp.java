package com.shubo7868.shubham.gymit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText nEmailField;
    private EditText nPasswordField;
    private EditText nConfirmPasswordField;

    private TextView nSigneIn;
    private Button nSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        nEmailField = findViewById(R.id.signUp_emailField);
        nPasswordField = findViewById(R.id.signUp_passwordField);
        nConfirmPasswordField = findViewById(R.id.signUp_confirm);

        nSignUp = findViewById(R.id.button_signUp);

        nSigneIn = findViewById(R.id.signUp_tv);


        nSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount();

            }
        });

        nSigneIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
                finish();
            }
        });




    }

    private void createAccount(){
        String email = nEmailField.getText().toString();
        String password = nPasswordField.getText().toString();
        String confirmPassword = nConfirmPasswordField.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignUp.this, "Field are empty", Toast.LENGTH_LONG).show();
        }else if(!isValidEmail(email)){
            Toast.makeText(SignUp.this, "Enter a valid email address.", Toast.LENGTH_LONG).show();
        }else if(!password.equals(confirmPassword)) {
            Toast.makeText(SignUp.this, "Password doesn't match!!", Toast.LENGTH_LONG).show();

        } else {
                mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(SignUp.this, "Account created successfully",Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(SignUp.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                }

                // ...
            }
        });
        }

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }





}
