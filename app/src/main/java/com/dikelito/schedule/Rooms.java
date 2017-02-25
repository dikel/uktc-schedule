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

public class Rooms extends Fragment {

    private String title;
    private int page;
    public static ListView listView;
    String rooms[] = {"220", "221", "316", "317", "207", "208", "209", "210", "211", "212", "213",
            "214", "215", "219", "222", "223", "309", "310", "311", "312", "313", "314", "315",
            "319", "403", "404", "405", "406", "407", "408", "409", "410", "411", "412", "114"};

    public static Rooms newInstance(int page, String title) {
        Rooms fragmentFirst = new Rooms();
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
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        listView = (ListView) view.findViewById(R.id.rooms_listview);
        listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list, rooms));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Schedule.cls = rooms[position];
                Schedule.thing = "Стая";
                startActivity(new Intent(getActivity(), Schedule.class));
            }
        });
        return view;
    }
}