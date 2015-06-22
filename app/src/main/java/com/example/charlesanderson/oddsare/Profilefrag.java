package com.example.charlesanderson.oddsare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profilefrag_layout, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.profile_img);
        TextView textView = (TextView) rootView.findViewById(R.id.profile_name);

        profile_name = "Profile Name";
        textView.setText(profile_name);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.profile_pager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabStrip);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_full);
        bitmap = getCroppedBitmap(bitmap, 320);
        imageView.setImageBitmap(bitmap);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile_));

        customPagerAdapter pagerAdapter = new customPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabStrip.setViewPager(viewPager);




        return rootView;
    }


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


    public class customPagerAdapter extends FragmentPagerAdapter {


        private final int[] ICONS = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };

        private final String[] Titles = {
                "Achievements",
                "Challenges"
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