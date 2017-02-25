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

public class Teachers extends Fragment {

    private String title;
    private int page;
    public static ListView listView;
    public static ArrayAdapter<String> adapter;
    final String teachers[] = {"Валери Колев", "Валери Стефанов", "Венета Божинова", "Румяна Лазарова",
            "Нина Баячева", "Емилия Донкова", "Димчо Данов", "Иванка Манчева - 0893691523", "Иванка Ненчева",
            "Иво Цъклев", "Ина Георгиева - 0893690702", "Кирил Тончев - 0888141585", "Маргарита Гаврилова", "Гергана Муховска",
            "Мариана Маноева", "Неделчо Недялков", "Николай Сираков", "Огнян Наков", "Радка Йорданова",
            "Сийка Ценева", "Стефан Стефанов - 0887096474", "Стефан Ценов", "Трифон Трифонов", "Цеца Цолова",
            "Мария Петкова", "Паулина Цветкова - 0884784739", "Петя Мишева - 0893690821", "Боряна Димитрова", "Нели Сиракова",
            "Валя Владимирова", "Цветомир Георгиев", "Румен Трифонов", "Владимир Димитров", "Наталия Дончева",
            "Татяна Иванова - 0889320691", "Дарин Василев", "Лидия Рашкова", "Галя Божинова", "Пенка Томова", "Павлин Петков",
            "Венцислав Начев", "Явор Томов", "Даниела Гоцева", "Валентин Христов", "Красимир Илиев",
            "Петър Маринов", "Тенчо Гочев", "Милена Лазарова", "Момчил Петков", "Елена Първанова",
            "Костадин Костадинов", "Васил Цанов", "", "Николай Бошков"};

    public static Teachers newInstance(int page, String title) {
        Teachers fragmentFirst = new Teachers();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers, container, false);
        listView = (ListView) view.findViewById(R.id.tchs_listview);
        adapter = new ArrayAdapter<>(getContext(), R.layout.list, R.id.list_item, teachers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(getTeacherIndex(adapter.getItem(position)));
                String[] separated = teachers[getTeacherIndex(adapter.getItem(position))].split("-");
                separated[0].trim();
                Schedule.cls = separated[0];
                Schedule.thing = "Учител";
                Schedule.num = getTeacherIndex(adapter.getItem(position));
                startActivity(new Intent(getActivity(), Schedule.class));
            }
        });
        return view;
    }
    public int getTeacherIndex(String teacher) {
        for(int i=0; i<teachers.length; i++){
            if(teacher == teachers[i]){
                return i;
            }
        }
        return 0;
    }
}