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

import org.json.JSONArray;
import org.json.JSONException;

public class Rooms extends Fragment {

    public static Rooms newInstance(int page, String title) {
        Rooms fragmentFirst = new Rooms();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        if (MainActivity.jsonObject != null) {
            try {
                final JSONArray jsonArray = MainActivity.jsonObject.getJSONArray("roomsArray");

                int length = jsonArray.length();
                String[] stringsArray = new String[length];
                for (int i = 0; i < length; i++) {
                    if(jsonArray.getString(i).equals("assemble")) {
                        stringsArray[i] = "Зала за асемблиране";
                    } else {
                        stringsArray[i] = jsonArray.getString(i).replace("r", "");
                    }
                }

                ListView listView = view.findViewById(R.id.rooms_listview);
                listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list, stringsArray));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (MainActivity.jsonObject != null) {
                            Intent intent = new Intent(getActivity(), Schedule.class);
                            intent.putExtra("type", "rooms");
                            try {
                                intent.putExtra("name", jsonArray.getString(position));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }
                    } // Display message!
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}