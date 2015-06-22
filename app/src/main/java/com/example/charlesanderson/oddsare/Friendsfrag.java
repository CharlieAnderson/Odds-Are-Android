package com.example.charlesanderson.oddsare;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class Friendsfrag extends ListFragment {


    public static final String TAG = Friendsfrag.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.friendfrag_layout, container, false);
        final ListView listview = (ListView) rootView.findViewById(android.R.id.list);
        SearchView search = (SearchView) rootView.findViewById(R.id.searchView);

        listview.setTextFilterEnabled(true);

        search.setSubmitButtonEnabled(true);
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayAdapter adapter = (ArrayAdapter)listview.getAdapter();
                if (TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter(null);

                } else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        String[] friendsList = MainActivity.getFriendsList();
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, friendsList );
        listview.setAdapter(listAdapter);



        return rootView;
    }
}
