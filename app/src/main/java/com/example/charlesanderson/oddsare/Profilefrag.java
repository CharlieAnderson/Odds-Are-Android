package com.example.charlesanderson.oddsare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class Profilefrag extends Fragment {


    public static final String TAG = Profilefrag.class.getSimpleName();
    private Drawable profile_img;
    private ArrayAdapter<String> listAdapter1, listAdapter2;
    private String profile_name;
    private ProfilePictureView profilePictureView;
    private String id;
    private ArrayList<String> stats, achievements;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profilefrag_layout, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.profile_img);
        TextView textView = (TextView) rootView.findViewById(R.id.profile_name);
        profilePictureView = new ProfilePictureView(getActivity());
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.profile_pager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabStrip);

        Bundle args = getArguments();

        // it's the user's profile
        if(args == null || args.isEmpty()){
            id = MainActivity.getUserId();
            profile_name = MainActivity.getUsername();
            profilePictureView.setProfileId(id);
            profilePictureView.setCropped(true);

        }
        // it's a friend's profile
        else{
            id = MainActivity.getFriendId(args.getString("name"));
            profile_name = args.getString("name");
            profilePictureView.setProfileId(id);
            profilePictureView.setCropped(true);
        }

        //set name
        textView.setText(profile_name);

        //set profile picture and crop it to a circle
        ImageView profileImg = ( ( ImageView)profilePictureView.getChildAt( 0));
        Bitmap profileBitMap = ( (BitmapDrawable) profileImg.getDrawable()).getBitmap();
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
            profileBitMap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        }
        catch( Exception e){
            Log.e(TAG, "Error getting profile picture");
        }
        profileBitMap = getCroppedBitmap(profileBitMap, 320);
        imageView.setImageBitmap(profileBitMap);


        //set adapter for tabs
        customPagerAdapter pagerAdapter = new customPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabStrip.setViewPager(viewPager);

        //get statistics and achievements from the cloud
        final HashMap<String, Object> params = new HashMap<>();
        params.put("objectId", ParseUser.getCurrentUser().getObjectId());
        ParseCloud.callFunctionInBackground("getAchievementsAndStatisticsForUser",
                params, new FunctionCallback< Map< String, ArrayList<String>>>() {
            @Override
            public void done(Map<String, ArrayList<String>> o, ParseException e) {
                if(e==null) {
                    stats = o.get("statistics");
                    achievements = o.get("achievements");
                    Log.d(TAG, o.get("statistics").toString());
                    Log.d(TAG, o.get("achievements").toString());
                }
                else
                    Log.e(TAG, "Parse Cloud Error: "+e.toString());
            }
        });

        ParseCloud.callFunctionInBackground("getAchievementsAndStatisticsForUser",
                params, new FunctionCallback< Map< String, ParseObject>>() {
                    @Override
                    public void done(Map<String, ParseObject> o, ParseException e) {
                        if(e==null) {

                        }
                        else
                            Log.e(TAG, "Parse Cloud Error: "+e.toString());
                    }
                });

        return rootView;
    }

    // circularly crops bitmaps
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

// custom adapter for the two profile tabs
    public class customPagerAdapter extends FragmentPagerAdapter {
        private final int[] ICONS = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };
        private final String[] Titles = {
                "Statistics",
                "Achievements"
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
            args.putStringArrayList("achievements", achievements);
            args.putStringArrayList("stats", stats);
            if (position < getCount()) {
                ProfileNestedfrag childFrag = new ProfileNestedfrag();
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