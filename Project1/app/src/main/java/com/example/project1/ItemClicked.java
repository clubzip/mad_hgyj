package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;




// 이 액티비티도 ContactFrag에서 startActivityForResult로 열어야 하고
// 팝업액티비티도 이 액티비티에서 startActivityForResult로 열어야 함.

//팝업액티비티에서 확인 또는 취소를 누를경우 result를 전송하고 finish

// result가 '확인 버튼을 클릭한 경우'라면 JSON파일에서 해당 라인을 삭제하고 결과 code를 10001로 set한 후 액티비티를 finish
// 취소라면 아무것도 안하면 됨.

public class ItemClicked extends AppCompatActivity {

    public void OnClickCall(View v){
        TextView number = (TextView) findViewById(R.id.tv_pn);
        String call_number = "tel:"+number.getText();
        Intent intent_call = new Intent(Intent.ACTION_VIEW, Uri.parse(call_number));
        startActivity(intent_call);
    }
    public void OnClickMessage(View v){
        TextView number = (TextView) findViewById(R.id.tv_pn);
        String call_number = "smsto:"+number.getText();
        Intent intent_call = new Intent(Intent.ACTION_VIEW, Uri.parse(call_number));
        startActivity(intent_call);
    }
    public void OnClickEdit(View v){
        /*Intent intent = new Intent(this, RemovePopUp.class);
        TextView name = (TextView) findViewById(R.id.tv_name);
        TextView number = (TextView) findViewById(R.id.tv_pn);
        intent.putExtra("name",name.getText());
        intent.putExtra("number",number.getText());
        startActivityForResult(intent, 1);*/
        //startforResult써야할 듯.

    }
    public void OnClickRemove(View v){
        //팝업메세지 띄우기
        Intent intent = new Intent(this, RemovePopUp.class);
        TextView name = (TextView) findViewById(R.id.tv_name);
        intent.putExtra("name",name.getText());
        startActivityForResult(intent, 1);
        //팝업메세지 onclick_yes : json파일에서 해당 text 정보 담고 있는 라인 삭제.
            //item clicked도 result로 실행해야 할듯
            // 팝업 액티비티 확인 버튼 누르면 setresult후 팝업과 더불어 이 액티비티도 같이 닫힘.
            // 팝업 액티비티 취소버튼 누르면 팝업 액티비티만 닫힘.
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // RESULT_OK인 경우 제거했다는 뜻이므로 제거됐다고 result set(ContactFrag에서 갱신하기 위해)하고 finish
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_clicked);

        Intent intent = getIntent();

        TextView name = (TextView) findViewById(R.id.tv_name);
        TextView number = (TextView) findViewById(R.id.tv_pn);
        ImageView imageView1 = (ImageView) findViewById(R.id.profile);

        name.setText(intent.getStringExtra("name"));
        number.setText(intent.getStringExtra("number"));
        String profile =intent.getStringExtra("profile");
        if(profile.equals("default_image")) {
            imageView1.setImageResource(R.drawable.default_image);
        } else {
//            InputStream in = null;
            File mypath=new File(getFilesDir(),profile);
            try {
                File f=new File(getFilesDir(), profile);
                Bitmap img = BitmapFactory.decodeStream(new FileInputStream(f));
                // 이미지 표시
                imageView1.setImageBitmap(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }


}
