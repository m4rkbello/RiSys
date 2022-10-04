package com.softwaresolution.water_irrigation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.R;

import com.softwaresolution.water_irrigation.Interactive.Loading;
import com.softwaresolution.water_irrigation.Pojo.UserAccount;
public class LoginForm extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "PersonLoginForm";
    private Button btn_login,btn_register;
    private EditText txt_email,txt_password;

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Loading loading;
    public static UserAccount userProfile;
    private static LoginForm context;

    private String person = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        person = getIntent().getStringExtra("person");
        loading = new Loading(LoginForm.this);
        context = LoginForm.this;
        if (!TextUtils.isEmpty(person)){
            TextView txt_person = (TextView)findViewById(R.id.txt_person);
            txt_person.setText(person.substring(0, 1).toUpperCase() + person.substring(1));
        }
        init();
    }

    private void init() {
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (btn_login == v){
            //LOGIN
            onLogin();
        }
        if (btn_register == v){
            //REGISTER
            onRegister();
        }
    }

    private void onRegister() {
        startActivity(new Intent(this,RegisterForm.class));
    }

    private void onLogin() {
        String email = txt_email.getText().toString();
        String pass = txt_password.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            loading.loadDialog.show();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                onStart();
                            }else {
                                Toast.makeText(LoginForm.this,task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                                loading.loadDialog.dismiss();
                            }
                        }
                    });
        }else{
            Toast.makeText(this,"Please fill all the data information",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        if (auth.getCurrentUser() != null){
            loading.loadDialog.show();
            GetUserProfile();
        }
        super.onStart();
    }

    public static void GetUserProfile(){
        db.collection("WaterIrrigation User account")
                .document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                loading.loadDialog.dismiss();
                userProfile = documentSnapshot.toObject(UserAccount.class);
                if(userProfile==null){
                    return;
                }
                Log.d("PersonLoginForm",new Gson().toJson(userProfile));
                context.finish();
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });
    }
}
