package com.example.charlesanderson.oddsare;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charlesanderson.oddsare.RoundKnob.KnobSingleton;
import com.example.charlesanderson.oddsare.RoundKnob.RoundKnobButton;
import com.example.charlesanderson.oddsare.RoundKnob.RoundKnobButton.RoundKnobButtonListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class SetOddsfrag extends ListFragment {


    public static final String TAG = SetOddsfrag.class.getSimpleName();
    KnobSingleton m_Inst = KnobSingleton.getInstance();
    public int percent = 100;
    private ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.setoddsfrag_layout, container, false);
        listview = (ListView) rootView.findViewById(android.R.id.list);
        TextView button = (TextView) rootView.findViewById(R.id.button);
        final TextView input = (TextView)rootView.findViewById(R.id.input);


        m_Inst.InitGUIFrame(getActivity());

        getActivity().setTitle("Give the Odds");

        input.setText("100");
        View panel = (RelativeLayout)rootView.findViewById(R.id.odds);

        RoundKnobButton rv = new RoundKnobButton(getActivity(), R.drawable.rotor2, R.drawable.dial, R.drawable.dial,
                                                 m_Inst.Scale(475), m_Inst.Scale(475));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout)panel).addView(rv, lp);

        rv.setRotorPercentage(100);
        rv.SetListener(new RoundKnobButtonListener() {
            public void onStateChange(boolean newstate) {
                Toast.makeText(getActivity(), "Odds Chosen: " + percent, Toast.LENGTH_LONG).show();
            }

            public void onRotate(final int percentage) {

                input.post(new Runnable() {
                    public void run() {
                        input.setText(""+(percentage+1)+"");
                        percent = percentage+1;
                    }
                });
            }
        });







        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title

    }
}
