package com.sadik.earntask;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    AppCompatButton btnSignIn;
    TextView tvCreateAccount;
    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        pref = new PrefManager(this);

        if(pref.getToken()!=null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

        btnSignIn.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
                return;
            }

            api.login(email,pass).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        if(!response.isSuccessful()){
                            showError("HTTP Error: "+response.code());
                            return;
                        }

                        String raw = response.body().string();
                        JSONObject obj = new JSONObject(raw);

                        if(obj.getString("status").equals("success")){
                            pref.saveToken(obj.getString("token"));
                            pref.saveUser(
                                    obj.getString("uid"),
                                    obj.getString("name"),
                                    obj.getString("email"),
                                    obj.getString("code")
                            );

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            showError(obj.optString("msg","Invalid login"));
                        }

                    }catch(Exception e){
                        showError("Invalid Server Response"+response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showError("Network Error: "+t.getMessage());
                }
            });
        });

        tvCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
        );
    }

    private void showError(String msg){
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage(msg)
                .setPositiveButton("COPY",(d,i)->{
                    ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("error",msg));
                    Toast.makeText(this,"Copied",Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("OK",null)
                .show();
    }
}
