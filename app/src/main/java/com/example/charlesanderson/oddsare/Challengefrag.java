package com.example.charlesanderson.oddsare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.charlesanderson.oddsare.RoundKnob.KnobSingleton;

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
    private EditText text;
    private String challenge_text;
    private CursorAdapter cursorAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.challengefrag_layout, container, false);
        final EditText text = (EditText) rootView.findViewById(R.id.editText);
        ImageButton button = (ImageButton) rootView.findViewById(R.id.button);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView2);


        button.setImageDrawable(getResources().getDrawable(R.drawable.checkmark));

        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO: figure out a good way to filter the friends from the challenge page, adding a listview hides the challenge text
                /*
                ArrayAdapter adapter = (ArrayAdapter)listview.getAdapter();
                if (TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter(null);

                } else {
                    adapter.getFilter().filter(newText);
                }
                */
                return true;

            }
        });

        String[] friendsList = MainActivity.getFriendsList();
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, friendsList );


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText()!=null){

                    //for closing keyboard on button click
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null){
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    // Create new fragment and transaction
                    android.support.v4.app.Fragment oddsfrag = new Oddsfrag();

                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.content_frame, oddsfrag);
                    transaction.addToBackStack(null); //perhaps add a TAG?
                    // Commit the transaction
                    transaction.commit();
                }
            }
        });





        return rootView;
    }
}
