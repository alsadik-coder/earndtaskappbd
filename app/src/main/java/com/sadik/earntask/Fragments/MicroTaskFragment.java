package com.sadik.earntask.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sadik.earntask.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import okhttp3.ResponseBody;
import retrofit2.*;

public class MicroTaskFragment extends Fragment {

    RecyclerView rv;
    TaskAdapter adapter;
    List<TaskModel> list = new ArrayList<>();
    ApiInterface api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i,@Nullable ViewGroup c,@Nullable Bundle b){
        return i.inflate(R.layout.fragment_micro_task,c,false);
    }

    @Override
    public void onViewCreated(@NonNull View v,@Nullable Bundle b){
        ImageView back=v.findViewById(R.id.btnBack);
        FloatingActionButton fab=v.findViewById(R.id.fabAddTask);
        rv=v.findViewById(R.id.rvMicroTasks);

        api=ApiClient.getClient().create(ApiInterface.class);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new TaskAdapter(list,getContext());
        rv.setAdapter(adapter);

        back.setOnClickListener(vw->requireActivity().onBackPressed());
        fab.setOnClickListener(vw->openAdd());

        loadTasks();
    }

    void loadTasks(){
        api.getTasks().enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> c,Response<ResponseBody> r){
                try{
                    list.clear();
                    JSONArray arr=new JSONObject(r.body().string()).getJSONArray("tasks");
                    for(int i=0;i<arr.length();i++){
                        JSONObject o=arr.getJSONObject(i);
                        list.add(new TaskModel(o));
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e){Toast.makeText(getContext(),"Load error",Toast.LENGTH_SHORT).show();}
            }
            public void onFailure(Call<ResponseBody> c,Throwable t){Toast.makeText(getContext(),"Network error",Toast.LENGTH_SHORT).show();}
        });
    }

    void openAdd(){
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new AddMicroTaskFragment())
                .addToBackStack(null).commit();
    }




    public class TaskModel {
        public int id,reward;
        public String title,desc,url;

        public TaskModel(JSONObject o)throws Exception{
            id=o.getInt("id");
            title=o.getString("title");
            desc=o.getString("description");
            url=o.getString("task_url");
            reward=o.getInt("reward");
        }
    }


    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder>{

        List<TaskModel> list; Context c;

        public TaskAdapter(List<TaskModel> l,Context c){this.list=l;this.c=c;}

        public Holder onCreateViewHolder(ViewGroup p,int v){
            View view=LayoutInflater.from(c).inflate(R.layout.item_micro_task,p,false);
            return new Holder(view);
        }

        public void onBindViewHolder(Holder h,int i){
            TaskModel t=list.get(i);
            h.tvTitle.setText(t.title);
            h.tvReward.setText("৳"+t.reward);
            h.btnStart.setOnClickListener(v->{
                Intent it=new Intent(c,SubmitTaskActivity.class);
                it.putExtra("id",t.id);
                it.putExtra("task_id", String.valueOf(t.id));   // ✅ SAME KEY
                it.putExtra("title",t.title);
                it.putExtra("reward",t.reward);
                it.putExtra("url",t.url);
                c.startActivity(it);
            });
        }

        public int getItemCount(){return list.size();}

        class Holder extends RecyclerView.ViewHolder{
            TextView tvTitle,tvReward;
            AppCompatButton btnStart;
            Holder(View v){
                super(v);
                tvTitle=v.findViewById(R.id.tvTaskName);
                tvReward=v.findViewById(R.id.tvReward);
                btnStart=v.findViewById(R.id.btnDetails);
            }
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        loadTasks();
    }

}
