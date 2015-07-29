package com.example.charlesanderson.oddsare;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charlesanderson.oddsare.RoundKnob.KnobSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class Challengefrag extends android.support.v4.app.Fragment {


    public static final String TAG = Challengefrag.class.getSimpleName();
    KnobSingleton m_Inst = KnobSingleton.getInstance();
    private ListView listview;
    private EditText challenge;
    private String challenge_text;
    private CursorAdapter cursorAdapter;
    private EditText recipient;
    private List<String> friendsList = new ArrayList<String>();
    private FriendsSearchAdapter searchAdapter;
    private ArrayList<String> friends;
    private ListView list;
    private TextView button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.challengefrag_layout, container, false);
        challenge = (EditText) rootView.findViewById(R.id.editText);
        list = (ListView)rootView.findViewById(R.id.searchViewResult);
        recipient = (EditText) rootView.findViewById(R.id.challenged);
        button = (TextView) rootView.findViewById(R.id.button);
        //button.setImageDrawable(getResources().getDrawable(R.drawable.checkmark));


        friends = new ArrayList<String>();
        friends.addAll(Arrays.asList(MainActivity.getFriendsList()));
        friendsList.addAll(friends);

        searchAdapter = new FriendsSearchAdapter(getActivity(), android.R.layout.simple_list_item_1, friendsList);
        list.setAdapter(searchAdapter);

        Search();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recipient.setText(list.getItemAtPosition(position).toString());
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               /* if(getActivity().getCurrentFocus() != null){
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }*/
                recipient.clearFocus();
                list.setVisibility(View.INVISIBLE);
                challenge.setVisibility(View.VISIBLE);
                challenge.requestFocus();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(challenge.getText()!=null){

                    //for closing keyboard on button click
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null){
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    // Create new fragment and transaction
                    android.support.v4.app.Fragment oddsfrag = new SetOddsfrag();

                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.content_frame, oddsfrag);
                    // Commit the transaction
                    transaction.commit();
                }
            }
        });
        return rootView;
    }




    private void Search() {
        recipient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = recipient.getText().toString();
                filterFriends(text);
            }
        });

        recipient.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    list.setVisibility(View.VISIBLE);
                    challenge.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                }
                else {
                    list.setVisibility(View.INVISIBLE);
                    challenge.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    public void filterFriends(String query) {
        friendsList.clear();
            if(query.length()==0) {
            friendsList.addAll(friends);
        }
        else {
            for(String name : friends) {
                if (name.toLowerCase().contains(query.toLowerCase())) {
                    friendsList.add(name);
                }
            }
        }
        ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();
    }

}
