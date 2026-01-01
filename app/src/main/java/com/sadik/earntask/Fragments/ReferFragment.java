package com.sadik.earntask.Fragments;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.sadik.earntask.*;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.*;

public class ReferFragment extends Fragment {

    ImageView btnBack,btnCopyCode,btnCopyLink;
    TextView tvReferralCode,tvReferralLink,tvReferrals,tvEarnings;
    EditText etSubmit;
    CardView cvSubmit;

    ApiInterface api;
    PrefManager pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf,@Nullable ViewGroup c,@Nullable Bundle b) {
        View v=inf.inflate(R.layout.fragment_refer,c,false);

        btnBack=v.findViewById(R.id.btnBack);
        btnCopyCode=v.findViewById(R.id.btnCopyCode);
        btnCopyLink=v.findViewById(R.id.btnCopyLink);
        tvReferralCode=v.findViewById(R.id.tvReferralCode);
        tvReferralLink=v.findViewById(R.id.tvReferralLink);
        tvReferrals=v.findViewById(R.id.tvReferrals);
        tvEarnings=v.findViewById(R.id.tvEarnings);
        etSubmit=v.findViewById(R.id.etSubmitReferralCode);
        cvSubmit=v.findViewById(R.id.cvSubmitReferralCard);

        pref=new PrefManager(getContext());
        api=ApiClient.getClient().create(ApiInterface.class);

        tvReferralCode.setText(pref.getMyCode());
        tvReferralLink.setText("https://earntaka.com/referral/"+pref.getMyCode());

        btnCopyCode.setOnClickListener(vw->copy(tvReferralCode.getText().toString()));
        btnCopyLink.setOnClickListener(vw->copy(tvReferralLink.getText().toString()));
        btnBack.setOnClickListener(vw->requireActivity().onBackPressed());

        v.findViewById(R.id.btnSubmitReferral).setOnClickListener(vw->submit());

        setupShare(v);
        return v;
    }

    private void submit(){
        String code=etSubmit.getText().toString().trim();
        if(code.isEmpty()){toast("Enter referral code");return;}

        api.claimReferral(pref.getUserId(),code,getDeviceHash())
                .enqueue(new Callback<ResponseBody>() {
                    public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                        try{
                            String raw=r.body().string();
                            JSONObject o=new JSONObject(raw);

                            if(o.getString("status").equals("success")){
                                cvSubmit.setVisibility(View.GONE);
                                Snackbar.make(getView(),
                                        "You got ৳"+o.getInt("you_got")+" & your friend got ৳"+o.getInt("referrer_got"),
                                        Snackbar.LENGTH_LONG).show();
                                etSubmit.setText("");
                            }
                            else{
                                showError(o.getString("msg"));
                            }
                        }catch(Exception e){
                            showError("Invalid Server Response");
                        }
                    }
                    public void onFailure(Call<ResponseBody> c, Throwable t){toast("Network error");}
                });
    }

    private String getDeviceHash(){
        return Settings.Secure.getString(requireContext().getContentResolver(),Settings.Secure.ANDROID_ID);
    }

    private void copy(String txt){
        ClipboardManager cm=(ClipboardManager)requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("copy",txt));
        toast("Copied");
    }

    private void toast(String m){Toast.makeText(getContext(),m,Toast.LENGTH_SHORT).show();}

    private void setupShare(View v){
        String shareTxt="Earn with my referral code "+tvReferralCode.getText()+"\n"+tvReferralLink.getText();
        v.findViewById(R.id.shareWhatsApp).setOnClickListener(b->share("com.whatsapp",shareTxt));
        v.findViewById(R.id.shareMessenger).setOnClickListener(b->share("com.facebook.orca",shareTxt));
        v.findViewById(R.id.shareTelegram).setOnClickListener(b->share("org.telegram.messenger",shareTxt));
        v.findViewById(R.id.shareEmail).setOnClickListener(b->startActivity(new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"))));
        v.findViewById(R.id.shareSMS).setOnClickListener(b->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"))));
    }

    private void share(String pkg,String txt){
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,txt);
        i.setPackage(pkg);
        try{startActivity(i);}catch(Exception e){toast("App not installed");}
    }

    private void showError(String msg){
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Referral Failed")
                .setMessage(msg)
                .setPositiveButton("COPY",(d,i)->{
                    ClipboardManager cm=(ClipboardManager)requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("error",msg));
                    Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("OK",null)
                .show();
    }

}
