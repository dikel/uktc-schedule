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

public class Teachers extends Fragment {

    public static ArrayAdapter<String> adapter;

    public static Teachers newInstance(int page, String title) {
        Teachers fragmentFirst = new Teachers();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers, container, false);
        if (MainActivity.jsonObject != null) {
            try {
                final JSONArray teachersNames = MainActivity.jsonObject.getJSONArray("teachersNames");
                final JSONArray teachersArray = MainActivity.jsonObject.getJSONArray("teachersArray");

                final int length = teachersArray.length();
                final String[] stringsArray = new String[length];
                for (int i = 0; i < length; i++) {
                    stringsArray[i] = teachersNames.getString(i);
                }

                ListView listView = view.findViewById(R.id.tchs_listview);
                adapter = new ArrayAdapter<>(getContext(), R.layout.list, R.id.list_item, stringsArray);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (MainActivity.jsonObject != null) {
                            Intent intent = new Intent(getActivity(), Schedule.class);
                            intent.putExtra("type", "teachers");
                            try {
                                int teacherIndex = -1;
                                String clickedItem = adapter.getItem(position);
                                for (int i = 0; i < length; i++) {
                                    if (clickedItem.equals(stringsArray[i])) {
                                        teacherIndex = i;
                                        break;
                                    }
                                }
                                intent.putExtra("name", teachersArray.getString(teacherIndex));
                                intent.putExtra("teacherName", teachersNames.getString(teacherIndex));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        } // Display message!
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}