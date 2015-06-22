package com.example.charlesanderson.oddsare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class ProfileNestedfrag extends Fragment {


    public static final String TAG = ProfileNestedfrag.class.getSimpleName();
    private ArrayAdapter<String> listAdapter1;
    public ArrayList<String> list;
    public String[] values1, values2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profilechild_layout, container, false);

        ListView listview1 = (ListView) rootView.findViewById(R.id.listView1);

        TextView sample = new TextView(this.getActivity());
        sample.setText("Testing profile...");
        values1 = new String[]{
                "Achievement 1",
                "Achievement 2",
                "Achievement 3",
                "Achievement 4",
                "Achievement 5",
                "Achievement 6",
                "Achievement 7",
                "Achievement 8",
                "Achievement 9",
                "Achievement 10"
        };

        values2 = new String[]{
                "Challenge 1",
                "Challenge 2",
                "Challenge 3",
                "Challenge 4",
                "Challenge 5",
                "Challenge 6",
                "Challenge 7",
                "Challenge 8",
                "Challenge 9",
                "Challenge 10"

        };

        int arg = (int) getArguments().get("key");

        if(arg == 0) {
            list = new ArrayList<String>(Arrays.asList(values1));
        }
        else
            list = new ArrayList<String>(Arrays.asList(values2));


        listAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listview1.setAdapter(listAdapter1);


        return rootView;
    }

}