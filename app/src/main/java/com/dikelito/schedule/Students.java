package com.dikelito.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Students extends Fragment {

    private String title;
    private int page;
    public static ListView listView;
    String students[] = {"171", "172", "173", "174", "175", "161", "162", "163", "164", "165", "151",
            "152", "153", "154", "155", "141", "142", "143", "144", "131", "132", "133", "134"};

    public static Students newInstance(int page, String title) {
        Students fragmentFirst = new Students();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);
        listView = (ListView) view.findViewById(R.id.std_listview);
        listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list, students));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Schedule.cls = students[position];
                Schedule.thing = "Курс";
                startActivity(new Intent(getActivity(), Schedule.class));
            }
        });
        return view;
    }
}