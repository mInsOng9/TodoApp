package com.song.tp05todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.song.tp05todoapp.databinding.ActivityIntroBinding;
import com.song.tp05todoapp.MainActivity;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;

    //프로필이미지경로, 이름 데이터 변수
    String profileImage="";
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //시작하기 버튼 클릭 이벤트 처리
        binding.btn.setOnClickListener(view -> {
            saveData();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        binding.civProfile.setOnClickListener(view -> {
            //갤러리 or 사진 앱을 실행
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            resultLauncher.launch(intent);
        });

        loadData(); //저장된 프로필정보와 이름을 가져와서 보여주는 기능 호출

    }//onCreate

    //SharedPreference에서 프로필이미지와 이름을 가져와 화면에 보여주기
    void loadData(){
        SharedPreferences pref=getSharedPreferences("account",MODE_PRIVATE);
        profileImage=pref.getString("profile",""); //두번째 파라미터: 없을때 기본 string
        name=pref.getString("name","");

        Glide.with(this).load(profileImage).error(R.drawable.profle).into(binding.civProfile);
        binding.etName.setText(name);
    }//loadData

    //SharedPreference에 프로필이미지와 이름을 영구히 저장
    void saveData(){
        name=binding.etName.getText().toString();

        SharedPreferences pref=getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();

        editor.putString("profile",profileImage);
        editor.putString("name",name);

        editor.commit();

    }//saveData

    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()!=RESULT_CANCELED){
                Intent intent=result.getData();
                Uri uri=intent.getData(); //선택한 사진의 경로 uri 데이터를 받기

                profileImage=uri.toString();

                Glide.with(IntroActivity.this).load(profileImage).error(R.drawable.profle).into(binding.civProfile);
            }
        }
    });

}//IntroActivity