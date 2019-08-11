package vn.mac.gnam.blinkchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout textName;
    private TextInputLayout textEmail;
    private TextInputLayout textPass;
    private TextInputLayout textRePass;
    private Button btnRgister;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        toolBar();
        // Initialize Firebase Auth
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void toolBar() {
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Register");
        toolBar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        textName = (TextInputLayout) findViewById(R.id.textName);
        textEmail = (TextInputLayout) findViewById(R.id.textEmail);
        textPass = (TextInputLayout) findViewById(R.id.textPass);
        textRePass = (TextInputLayout) findViewById(R.id.textRePass);
        btnRgister = (Button) findViewById(R.id.btnRgister);
        btnRgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = textName.getEditText().getText().toString().trim();
                String email = textEmail.getEditText().getText().toString().trim();
                String password = textPass.getEditText().getText().toString().trim();
                String rePass = textRePass.getEditText().getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (name.length() == 0 || email.length() == 0 || password.length() == 0 || rePass.length() == 0){
                    Toast.makeText(RegisterActivity.this, "Field cannot be empty.", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(rePass)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)){
                    Toast.makeText(RegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please wait while we create your account !");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register(email, password);
                }
            }
        });
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
