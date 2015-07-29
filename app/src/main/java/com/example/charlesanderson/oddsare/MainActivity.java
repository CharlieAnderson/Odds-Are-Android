package com.example.charlesanderson.oddsare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
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
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;


public class MainActivity extends AppCompatActivity { //AppCompatActivity?


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager pager;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerTitles = {"News Feed", "Give the Challenge", "Friends", "", "Settings", "Logging out"};
    private CharSequence title;
    private static String username;
    private static String userId;
    private List<String> items;
    private Menu menu;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static String[] friendsList = new String[]{};
    public static Hashtable<String, String> friendIdList= new Hashtable<>();
    public Drawer drawer;
    private ParseUser currentUser;
    private Bitmap profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = "Profile Name";
        currentUser = ParseUser.getCurrentUser();

        if ((currentUser != null) && currentUser.isAuthenticated() && AccessToken.getCurrentAccessToken()!=null) {
            makeMeRequest();
            makeFriendRequest();
            if (currentUser.has("profile")) {
                JSONObject userProfile = currentUser.getJSONObject("profile");

                try {
                    if(userProfile.has("facebookId")) {
                        try {
                            URL imageURL = new URL("https://graph.facebook.com/" + userProfile.getString("facebookId") + "/picture?type=large");
                            profilePic = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                        }
                        catch(Exception e)
                        {
                            Log.e(TAG, e.toString());
                        }

                    }


                    if (userProfile.has("name")) {
                        username = userProfile.getString("name");
                    } else {
                        username = "";
                    }


                } catch (JSONException e) {
                    Log.d(TAG, "Error parsing saved user data.");
                }
            }
        }


        android.support.v7.widget.Toolbar supporttoolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
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
                        new ProfileDrawerItem().withName(username).withSelectable(true).withIdentifier(5).withIcon(profilePic)//R.drawable.profile_white
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
        IDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(getResources().getDrawable(R.drawable.power_icon)).withIdentifier(4).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.main);

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
                        news, odds, friends, divider, settings, logout
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


        if (getSupportActionBar() != null) {
            getSupportActionBar().setShowHideAnimationEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if ((drawer.isDrawerOpen()) && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        //react on the keyboard
        drawer.keyboardSupportEnabled(this, true);
    }


    @Override
    public void onResume() {
        super.onResume();

        ParseUser currentUser = ParseUser.getCurrentUser();
        makeMeRequest();
        makeFriendRequest();

        if (currentUser == null) {
            navigateToLogin();
        }
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
            else if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            super.onBackPressed();
        }

    }


    private Drawer.OnDrawerItemClickListener listen = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

            if (drawerItem != null && getSupportActionBar() != null && drawer != null) {
                if (position < 0)
                    position = 0;

                getSupportActionBar().setTitle(drawerTitles[position]);
                drawer.closeDrawer();
                final android.support.v4.app.Fragment newfrag;

                switch (position) {
                    case 0:
                        newfrag = new Newsfrag();
                        break;

                    case 1:
                        newfrag = new OddsListfrag();
                        break;

                    case 2:
                        newfrag = new Friendsfrag();
                        break;

                    case 4:
                        return false;

                    case 5:
                        ParseUser.logOut();
                        navigateToLogin();
                        return false;

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
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    public static String[] getFriendsList() {
        return friendsList;
    }

    private void navigateToLogin() {
        //locationManager.removeUpdates(locationListener);
        ParseUser.logOut();
        currentUser = null;
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            JSONObject userProfile = new JSONObject();

                            try {
                                userProfile.put("facebookId", jsonObject.getLong("id"));
                                userProfile.put("name", jsonObject.getString("name"));
                                userId = jsonObject.getString("id");

                                if (jsonObject.getString("gender") != null)
                                    userProfile.put("gender", jsonObject.getString("gender"));

                                if (jsonObject.getString("email") != null)
                                    userProfile.put("email", jsonObject.getString("email"));

                                if (jsonObject.getString("picture") != null)
                                    userProfile.put("picture", jsonObject.getString("picture"));


                                // Save the user profile info in a user property

                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                                // Show the user info
                            } catch (JSONException e) {
                                Log.d(OddsAreApp.TAG,
                                        "Error parsing returned user data. " + e);
                            }
                        } else if (graphResponse.getError() != null) {
                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:
                                    Log.d(OddsAreApp.TAG,
                                            "Authentication error: " + graphResponse.getError());
                                    break;

                                case TRANSIENT:
                                    Log.d(OddsAreApp.TAG,
                                            "Transient error. Try again. " + graphResponse.getError());
                                    break;

                                case OTHER:
                                    Log.d(OddsAreApp.TAG,
                                            "Some other error: " + graphResponse.getError());
                                    break;
                            }
                        }
                    }
                });

        request.executeAsync();
    }

    private void makeFriendRequest() {
        GraphRequest request = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                        Log.i("INFO", graphResponse.toString());
                        if (jsonArray != null) {
                            friendsList = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    friendsList[i] = jsonArray.getJSONObject(i).getString("name");
                                    friendIdList.put(friendsList[i], jsonArray.getJSONObject(i).getString("id"));
                                } catch (Exception e) {
                                    Log.e(TAG, "error with the friends request stuff");
                                }
                            }
                        }

                    }
                });
        request.executeAsync();
    }



    public static String[] getFriends() {
        return friendsList;
    }

    public static String getFriendId(String name) {
        return friendIdList.get(name);
    }
    public static String getUsername(){
        return username;
    }
    public static String getUserId(){
        return userId;
    }
}