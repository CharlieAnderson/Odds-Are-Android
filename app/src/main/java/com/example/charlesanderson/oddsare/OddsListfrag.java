package com.example.charlesanderson.oddsare;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.widget.ProfilePictureView;
import com.github.alexkolpa.fabtoolbar.FabToolbar;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class OddsListfrag extends Fragment {


    public static final String TAG = OddsListfrag.class.getSimpleName();
    private Drawable profile_img;
    private ArrayAdapter<String> listAdapter1, listAdapter2;
    private String profile_name;
    private ProfilePictureView profilePictureView;
    private FabToolbar fabToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.oddslist_layout, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.odds_pager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabStripOdds);
        final TextView actionText = (TextView) rootView.findViewById(R.id.attachText);
        final FabToolbar fabToolbar = ((FabToolbar) rootView.findViewById(R.id.fab_toolbar));
        ImageView actionButton = (ImageView) rootView.findViewById(R.id.attach);
        View dummy = rootView.findViewById(R.id.dummyView);

        actionText.setTextSize(24);
        actionText.setTextColor(Color.WHITE);

        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabToolbar.hide();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fabToolbar.isShown()) {
                    //for closing keyboard on button click
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null){
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    // Create new fragment and transaction
                    android.support.v4.app.Fragment challenge = new Challengefrag();

                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.content_frame, challenge);
                    transaction.addToBackStack(null); //perhaps add a TAG?
                    // Commit the transaction
                    transaction.commit();
                } else {
                    fabToolbar.show();
                }
            }
        });




         customPagerAdapter pagerAdapter = new customPagerAdapter(getChildFragmentManager());
         viewPager.setAdapter(pagerAdapter);
         tabStrip.setViewPager(viewPager);

        return rootView;
    }


    public class customPagerAdapter extends FragmentPagerAdapter {


        private final int[] ICONS = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };

        private final String[] Titles = {
                "Current Odds",
                "Past Odds"
        };

        public customPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return ICONS.length;
        }


        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt("key", position);

            if (position < getCount()) {
                OddsNestedFrag childFrag = new OddsNestedFrag();
                childFrag.setArguments(args);
                return childFrag;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }


    }
}
