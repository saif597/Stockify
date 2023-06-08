package com.example.stockify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.Console;
import java.util.HashMap;


public class Register extends AppCompatActivity {

    TextInputEditText emailEditText,passwordEditText,confirmPasswordEditText,nameEditText;
    TextView signInText;
    SpinKitView spinnerRegister;
    Button signUp;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailEditText=findViewById(R.id.registerEmail);
        passwordEditText=findViewById(R.id.registerPassword);
        spinnerRegister = findViewById(R.id.spinnerRegister);
        signInText=findViewById(R.id.signInText);
        signUp=findViewById(R.id.signUpBtn);
        confirmPasswordEditText=findViewById(R.id.registerConfirmPassword);
        nameEditText=findViewById(R.id.registerName);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerRegister.setIndeterminateDrawable(threeBounce);

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register.this,Login.class);
                startActivity(i);
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSpinnerVisible(true);
                String em,ps,cps,nm;
                em=String.valueOf(emailEditText.getText());
                ps=String.valueOf(passwordEditText.getText());
                cps=String.valueOf(confirmPasswordEditText.getText());
                nm=String.valueOf(nameEditText.getText());

                if (TextUtils.isEmpty(em)){
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ps)){
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(nm)){
                    Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cps)){
                    Toast.makeText(Register.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.equals(ps,cps)){
                    Toast.makeText(Register.this, "Passwords Don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(em,ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setSpinnerVisible(false);
                        if (task.isSuccessful()){
                            String ids=firebaseAuth.getCurrentUser().getUid().toString();
                            System.out.println("uiuid"+ids);
                            FirebaseDatabase rootNode;
                            DatabaseReference reference;
                            rootNode=FirebaseDatabase.getInstance();
                            reference=rootNode.getReference("Users");
                            UserHelperClass helperClass=new UserHelperClass(nm,em);
                            reference.child(ids).setValue(helperClass);
                            Toast.makeText(Register.this, "Registered", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Register.this,Login.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Toast.makeText(Register.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
    private void setSpinnerVisible(boolean visible) {
        if (visible) {
            spinnerRegister.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerRegister.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}