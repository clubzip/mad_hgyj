package com.example.project1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter implements Filterable {



    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList. (원본 데이터 리스트)
    public ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
    private ArrayList<ListViewItem> filteredItemList = listViewItemList ;
    Filter listFilter;

    public ListViewAdapter() {

    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = listViewItemList ;
                results.count = listViewItemList.size() ;
            } else {
                ArrayList<ListViewItem> itemList = new ArrayList<ListViewItem>() ;

                for (ListViewItem item : listViewItemList) {
                    if (item.getName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getNumber().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }
                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredItemList = (ArrayList<ListViewItem>) results.values ;
            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }
        return listFilter ;
    }


    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitemrow, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView textView1 = (TextView) convertView.findViewById(R.id.name);
        TextView textView2 = (TextView) convertView.findViewById(R.id.mobile);
        ImageView imageView1 = (ImageView)  convertView.findViewById(R.id.imageView1);
        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = filteredItemList.get(position);

        textView1.setText(listViewItem.getName());
        textView2.setText(listViewItem.getNumber());
        String profile =listViewItem.getProfile();
        if(profile.equals("default_image")) {
            imageView1.setImageResource(R.drawable.default_image);
        } else {
//            InputStream in = null;
            try {
                File f=new File(context.getFilesDir(), profile);
                Bitmap img = BitmapFactory.decodeStream(new FileInputStream(f));
//                in = context.getContentResolver().openInputStream(Uri.parse(profile));
//                Bitmap img = BitmapFactory.decodeStream(in);
//                in.close();
                // 이미지 표시
                imageView1.setImageBitmap(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            imageView1.setImageResource(profile_id);
        }
        return convertView;
    }

    public ListViewItem addItem(String name, String number, String profile) {
        ListViewItem item = new ListViewItem();
        item.setName(name);
        item.setNumber(number);
        item.setProfile(profile);

        listViewItemList.add(item);
        return item;
    }

    public void clearItem(){
        listViewItemList.clear();
    }

}