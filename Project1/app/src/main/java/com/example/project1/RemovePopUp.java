package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RemovePopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.remove_popup);
    }

    //제거 버튼 클릭
    public void mOnRemove(View v) throws IOException, JSONException {
        //데이터 받아오기
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");

        //JSON파일에서 해당라인 지우기
        Resources res = getResources();
        FileInputStream fis = openFileInput("contact.json");
        InputStreamReader isr= new InputStreamReader(fis);
        BufferedReader reader= new BufferedReader(isr);

        StringBuffer buffer= new StringBuffer();
        String line= reader.readLine();
        while (line!=null){ // name을 포함하지 않는 라인만 buffer에 넣기.
            if(line.contains(name)) {
                line=reader.readLine();
                continue;
            }
            buffer.append(line+"\n");
            line=reader.readLine();
        }
        fis.close();
        String jsonData= buffer.toString();
        if(jsonData.contains(",\n]")){
            jsonData = jsonData.replace(",\n]","\n]");
        }
        FileOutputStream fos= openFileOutput("contact.json",MODE_PRIVATE);
        fos.write(jsonData.getBytes());
        fos.close();

        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    //취소 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}

