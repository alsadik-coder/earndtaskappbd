package com.sadik.earntask.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.sadik.earntask.*;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.*;

public class AddMicroTaskFragment extends Fragment {

    EditText etName,etReward,etDesc,etUrl;
    ImageView btnClose;
    Button btnAdd;
    ApiInterface api;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater i,@Nullable ViewGroup c,@Nullable Bundle b){
        return i.inflate(R.layout.fragment_add_micro_task,c,false);
    }

    public void onViewCreated(@NonNull View v,@Nullable Bundle b){
        etName=v.findViewById(R.id.etTaskName);
        etReward=v.findViewById(R.id.etReward);
        etDesc=v.findViewById(R.id.etDescription);
        etUrl=v.findViewById(R.id.etUrl);
        btnClose=v.findViewById(R.id.btnClose);
        btnAdd=v.findViewById(R.id.btnAddTask);

        api=ApiClient.getClient().create(ApiInterface.class);

        btnClose.setOnClickListener(bk->requireActivity().onBackPressed());
        btnAdd.setOnClickListener(vw->submit());
    }

    void submit(){
        String name=etName.getText().toString().trim();
        String reward=etReward.getText().toString().trim();
        String desc=etDesc.getText().toString().trim();
        String url=etUrl.getText().toString().trim();

        if(name.isEmpty()||reward.isEmpty()){
            toast("Fill required fields"); return;
        }

        api.addMicroTask(name,desc,reward,url).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> c,Response<ResponseBody> r){
                try{
                    JSONObject o=new JSONObject(r.body().string());
                    if(o.getString("status").equals("success")){
                        toast("Task added successfully");
                        requireActivity().onBackPressed();
                    }else toast(o.getString("msg"));
                }catch(Exception e){toast("Server error");}
            }
            public void onFailure(Call<ResponseBody> c,Throwable t){toast("Network error");}
        });
    }

    void toast(String m){Toast.makeText(getContext(),m,Toast.LENGTH_SHORT).show();}
}
