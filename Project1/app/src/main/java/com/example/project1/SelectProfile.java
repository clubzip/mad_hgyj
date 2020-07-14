package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SelectProfile extends AppCompatActivity {
    String profile_name = "default_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_profile);

        //각 사진 이미지뷰 onclick함수
        //선택된 이미지 버튼의 파일명을 profile_name으로 바꾸기
        //해당 이미지뷰 겉에 선택됐다는 테두리 출력.
    }

    public void OnClickHandle(View view) // 확인버튼 누르면 할 것.
    {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("profile", profile_name ); // profile 파일명. Contact Add에서

        setResult(10002, resultIntent);
        finish();
    }
}
