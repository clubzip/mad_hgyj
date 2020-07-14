package com.example.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ContactFrag extends Fragment {

    public ContactFrag() {

    }


    View view;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<ListViewItem> data = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        listView =(ListView) view.findViewById(R.id.listView);
        data = new ArrayList<>();
        listViewAdapter = new ListViewAdapter();
        EditText editTextFilter = (EditText)view.findViewById(R.id.editSearch) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                ((ListViewAdapter)listView.getAdapter()).getFilter().filter(filterText) ;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

        Context context = getContext();
        Resources res = context.getResources();


        //내부 저장소의 json 파일 읽기 위한 InputStream
        try {
            FileInputStream fis = getActivity().openFileInput("contact.json");
            InputStreamReader isr= new InputStreamReader(fis);
            BufferedReader reader= new BufferedReader(isr);

            StringBuffer buffer= new StringBuffer();
            String line= reader.readLine();
            while (line!=null){
                buffer.append(line+"\n");
                line=reader.readLine();
            }

            String jsonData= buffer.toString();

            //json 데이터가 []로 시작하는 배열일때..
            JSONArray jsonArray= new JSONArray(jsonData);

            for(int i=0; i<jsonArray.length();i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");
                String mobile = jo.getString("mobile");
                String profile = jo.getString("profile");
//                int drawableResId = res.getIdentifier(profile, "drawable", context.getPackageName());
                //어뎁터에 아이템 추가 및 data에 추가
//                data.add(listViewAdapter.addItem(name, mobile,drawableResId));
                data.add(listViewAdapter.addItem(name, mobile,profile));
            }

            //리스트뷰에 어뎁터 set
            listView.setAdapter(listViewAdapter);

            //Item 클릭 시 이벤트
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position,long id) {
                    Intent intent = new Intent(getActivity(), ItemClicked.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("profile", data.get(position).getProfile());
                    intent.putExtra("name", data.get(position).getName());
                    intent.putExtra("number", data.get(position).getNumber());

                    startActivityForResult(intent,10001);
                }
            });
    } catch (IOException e) {e.printStackTrace();} catch (JSONException e) {e.printStackTrace();}
    // Inflate the layout for this fragment
    //버튼 뷰 추가
    Button addButton = (Button)view.findViewById(R.id.add);
    addButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*Intent intent = new Intent(getActivity(), ContactAdd.class);
            startActivity(intent);*/
            Intent intent = new Intent(getActivity(), ContactAdd.class);
            startActivityForResult(intent, 10001);
        }
    });
    // 버튼 클릭 시 ContactAdd액티비티 실행 - (해당 액티비티 manifest에 추가하기)
    return view;
}}