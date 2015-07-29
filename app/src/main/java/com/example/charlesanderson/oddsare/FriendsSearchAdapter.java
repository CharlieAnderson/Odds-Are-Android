package com.example.charlesanderson.oddsare;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by charlesanderson on 7/21/15.
 */
public class FriendsSearchAdapter extends ArrayAdapter<String> {

    public FriendsSearchAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
