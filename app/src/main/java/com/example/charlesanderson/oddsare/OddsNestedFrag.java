package com.example.charlesanderson.oddsare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class OddsNestedFrag extends Fragment {


    public static final String TAG = OddsNestedFrag.class.getSimpleName();
    private ArrayAdapter<String> listAdapter1;
    public ArrayList<String> list;
    public String[] values1, values2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.oddschild_layout, container, false);

        ListView listview1 = (ListView) rootView.findViewById(R.id.listViewOdds);

        values1 = new String[]{
                "Current Challenge 1",
                "Current Challenge 2",
                "Current Challenge 3",
                "Current Challenge 4",
                "Current Challenge 5",
                "Current Challenge 6",
                "Current Challenge 7",
                "Current Challenge 8",
                "Current Challenge 9",
                "Current Challenge 1",
                "Current Challenge 2",
                "Current Challenge 3",
                "Current Challenge 4",
                "Current Challenge 5",
                "Current Challenge 6",
                "Current Challenge 7",
                "Current Challenge 8",
                "Current Challenge 9",
                "Current Challenge 10"
        };

        values2 = new String[]{
                "Past Challenge 1",
                "Past Challenge 2",
                "Past Challenge 3",
                "Past Challenge 4",
                "Past Challenge 5",
                "Past Challenge 6",
                "Past Challenge 7",
                "Past Challenge 8",
                "Past Challenge 9",
                "Past Challenge 1",
                "Past Challenge 2",
                "Past Challenge 3",
                "Past Challenge 4",
                "Past Challenge 5",
                "Past Challenge 6",
                "Past Challenge 7",
                "Past Challenge 8",
                "Past Challenge 9",
                "Past Challenge 10"

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