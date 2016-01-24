package com.xionces.StoreCallRecords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Tayfun CESUR on 24.01.2016.
 */
public class Records extends Fragment {
    public static File[] files;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab3.xml
        View view = inflater.inflate(R.layout.records, container, false);
        ArrayList<String> files = GetFiles(CallHelper.PATH);
        ListView list =(ListView) view.findViewById(R.id.listView);
        ListViewAdapter adapter = new ListViewAdapter(getContext(),files);
        list.setAdapter(adapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Player.class);
                Bundle b = new Bundle();
                b.putInt("aa", position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        return view;
    }



    private ArrayList<String> GetFiles(String path)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(path);
        files = file.listFiles();
        if (files.length == 0)
        {
            return null;
        }
        else
        {
            for (int i = 0; i < files.length;i++ )
            {
                arrayList.add(files[i].getName());
            }
        }

        return arrayList;


    }




}
