package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    private GalleryFrag galleryFrag;
    private ContactFrag contactFrag;
    private CalenderFrag calenderFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent0 = new Intent(this, SplashActivity.class);
        startActivity(intent0);

//        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tap_layout);

        galleryFrag = new GalleryFrag();
        contactFrag = new ContactFrag();
        calenderFrag = new CalenderFrag();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment(galleryFrag, "Gallery");
        viewPagerAdapter.addFragment(contactFrag, "Contact");
        viewPagerAdapter.addFragment(calenderFrag, "Calendar");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_collections_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_contact_phone_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_calendar_today_24);

        //contact.json파일이 존재하지 않으면
        File contact = new File(getFilesDir(), "contact.json");
//        contact.delete();
        if(!contact.exists()){
            try {
                contact.createNewFile();
                FileOutputStream fos = openFileOutput("contact.json",MODE_APPEND);
                fos.write("[\n]".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}