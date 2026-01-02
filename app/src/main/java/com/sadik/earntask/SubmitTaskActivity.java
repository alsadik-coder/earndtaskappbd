package com.sadik.earntask;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.*;

public class SubmitTaskActivity extends AppCompatActivity {

    EditText etScreenshot;
    Button btnSubmit;
    ApiInterface api;
    PrefManager pref;

    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskId = getIntent().getStringExtra("task_id");

        etScreenshot = findViewById(R.id.etScreenshot);
        btnSubmit = findViewById(R.id.btnSubmit);

        api = ApiClient.getClient().create(ApiInterface.class);
        pref = new PrefManager(this);

        // 👇 TEST TOAST
        String uid = pref.getUserId();
        Toast.makeText(this,
                "USER ID : " + uid + "\nTASK ID : " + taskId,
                Toast.LENGTH_LONG).show();

        btnSubmit.setOnClickListener(v -> submit());
    }

    private void submit(){

        String proof = etScreenshot.getText().toString().trim();

        if(proof.isEmpty()){
            Toast.makeText(this,"Paste your proof link",Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = pref.getUserId();
        if(uid.isEmpty()){
            Toast.makeText(this,"Session expired, login again",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        api.submitTask(uid,taskId,proof).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> r) {
                try{
                    if(!r.isSuccessful()){
                        Toast.makeText(SubmitTaskActivity.this,"HTTP "+r.code(),Toast.LENGTH_LONG).show();
                        return;
                    }

                    String raw = r.body().string();
                    JSONObject o = new JSONObject(raw);

                    if(o.getString("status").equals("success")){
                        Toast.makeText(SubmitTaskActivity.this,"Task Submitted Successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(SubmitTaskActivity.this,o.getString("msg"),Toast.LENGTH_LONG).show();
                    }

                }catch(Exception e){
                    Toast.makeText(SubmitTaskActivity.this,"Invalid Server Response",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SubmitTaskActivity.this,"Network Error",Toast.LENGTH_LONG).show();
            }
        });
    }
}