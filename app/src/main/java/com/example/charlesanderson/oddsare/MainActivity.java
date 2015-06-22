package com.example.charlesanderson.oddsare;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.KeyboardUtil;


public class MainActivity extends AppCompatActivity { //AppCompatActivity?



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager pager;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerTitles = {"News Feed","Give the Challenge", "Friends"};
    private CharSequence title;
    private String username;


    public static String[] friendsList = new String[] {
        "Friend 1",
        "Friend 2",
        "Friend 3",
        "Friend 4",
        "Friend 5",
        "Friend 6",
        "Friend 7",
        "Friend 8",
        "Friend 9",
        "Friend 10",
        "Friend 11",
        "Friend 12",
        "Friend 13",
        "Friend 14",
        "Friend 15"
    };
    public Drawer drawer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    //    Parse.initialize(this, "IXkfPyf1UKOPZIqV84ysnNu2agSgDWlP4xXpE4lB", "4vjrGGx9S0viKyobmMHwAHJ9zq6YYKN0BvT70gdG");

        username = "Profile Name";


        android.support.v7.widget.Toolbar supporttoolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(supporttoolbar);
        setTitle("Odds Are");


        final AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.color.main)
                .withTextColor(getTitleColor())
                .withProfileImagesClickable(true)
                .withSelectionListEnabledForSingleProfile(false)
                .withDrawer(drawer)
                .addProfiles(
                        new ProfileDrawerItem().withName(username).withSelectable(true).withIdentifier(5).withIcon(getResources().getDrawable(R.drawable.profile_white))
                )
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile iProfile) {
                        gotoProfile();
                        return false;
                    }
                })
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        gotoProfile();
                        return false;
                    }
                })
                .build();


        IDrawerItem news = new PrimaryDrawerItem().withName("News Feed").withIcon(getResources().getDrawable(R.drawable.newsfeed)).withIdentifier(0).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.main);
        IDrawerItem odds = new PrimaryDrawerItem().withName("Odds").withIcon(getResources().getDrawable(R.drawable.odds_icon)).withIdentifier(1).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.main);
        IDrawerItem friends = new PrimaryDrawerItem().withName("Friends").withIcon(getResources().getDrawable(R.drawable.friends_icon)).withIdentifier(2).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.main);
        IDrawerItem divider = new DividerDrawerItem();
        IDrawerItem settings = new PrimaryDrawerItem().withName("Settings").withIcon(getResources().getDrawable(R.drawable.ic_settings)).withIdentifier(3).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.main);

            drawer = new DrawerBuilder().withActivity(this)
                    .withToolbar(supporttoolbar)
                    .withAccountHeader(drawerHeader)
                    .withAnimateDrawerItems(true)
                    .withHeaderDivider(true)
                    .withTranslucentStatusBar(true)
                    .withCloseOnClick(true)
                    .withActionBarDrawerToggle(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .addDrawerItems(
                            news, odds, friends, divider, settings
                    )
                    .withOnDrawerItemClickListener(listen)

                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {
                            KeyboardUtil.hideKeyboard(MainActivity.this);
                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                        }

                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                        }
                    })
                    .withFireOnInitialOnClick(true)
                    .withSavedInstance(savedInstanceState)
                    .build();

        drawer.setSelectionByIdentifier(0, true);




        if(getSupportActionBar()!=null) {
            getSupportActionBar().setShowHideAnimationEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if((drawer.isDrawerOpen()) && getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        //react on the keyboard
        drawer.keyboardSupportEnabled(this, true);



    }

    private void gotoProfile() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
            drawer.closeDrawer();
            final android.support.v4.app.Fragment profrag = new Profilefrag();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, profrag)
                            .commit();
                }
            }, 200);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity

            try {
                if (drawer.isDrawerOpen())
                    drawer.closeDrawer();
            }
            catch(Exception e) {
                System.out.println("Error " + e.getMessage());
                super.onBackPressed();

            }
    }



     private Drawer.OnDrawerItemClickListener listen = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

            if (drawerItem != null && getSupportActionBar() != null && drawer!=null) {
                if(position<0)
                    position = 0;

                getSupportActionBar().setTitle(drawerTitles[position]);
                drawer.closeDrawer();
                final android.support.v4.app.Fragment newfrag;

                switch (position) {

                    case 0:
                         newfrag = new Newsfrag();

                        break;

                    case 1:
                        newfrag = new Challengefrag();

                        break;

                    case 2:
                        newfrag = new Friendsfrag();

                        break;

                    default:
                        return false;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, newfrag)
                                .commit();
                    }
                }, 200);

            }
            return false;
        }
    };


    public void changeTitle(String title) {
        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle(title);
    }

    public static String[] getFriendsList() {
        return friendsList;
    }
}
