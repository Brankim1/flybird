package com.example.flybird;

import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {
    ListView listView;
    ArrayAdapter<String>adapter;
    List<String> dataList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
     View view=inflater.inflate(R.layout.historylist,container,false);
     listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        dataList.clear();
        List<Score> scores= LitePal.select("score").order("score desc").find(Score.class);
        for(Score scor:scores){
            dataList.add(String.valueOf(scor.getScore()));
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }
}
