package com.example.stockify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;



public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail,editTextPassword;
    Button signIn;
    TextView signUp;
     SpinKitView spinner;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    String name,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inside your activity or fragment class


// Inside onCreate() or onCreateView() method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail=findViewById(R.id.emailInput);
        editTextPassword=findViewById(R.id.passwordInput);
        signIn=findViewById(R.id.signInBtn);
        spinner = findViewById(R.id.spinner);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinner.setIndeterminateDrawable(threeBounce);
        signUp=findViewById(R.id.signUpText);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Login.this,Register.class);
                startActivity(i);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSpinnerVisible(true);
                // Show the progress indicator

                String em,ps;
                em=String.valueOf(editTextEmail.getText());
                ps=String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(em)){
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(ps)){
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(em,ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String ids=firebaseAuth.getCurrentUser().getUid().toString();
                            String userId = ids;

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                      String n = dataSnapshot.child("name").getValue(String.class);
                                      String e = dataSnapshot.child("email").getValue(String.class);
                                      name=n;
                                      email=e;
                                        Intent i=new Intent(Login.this,MainActivity.class);
                                        i.putExtra("userId",ids);
                                        System.out.println("12xdown "+name+email);
                                        i.putExtra("name",name);
                                        i.putExtra("email",email);
                                        setSpinnerVisible(false);
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    setSpinnerVisible(false);
                                    // Handle any errors that occur
                                }
                            });

                        }
                        else {
                            setSpinnerVisible(false);
                            Toast.makeText(Login.this, "Authentication Failed , Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }
    private void setSpinnerVisible(boolean visible) {
        if (visible) {
            spinner.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinner.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }



}