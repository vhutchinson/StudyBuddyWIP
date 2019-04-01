package com.studybuddy.studybuddy;


import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToDoubleBiFunction;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private ArrayList<String> mCourses = new ArrayList<>();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    CourseRecyclerAdapter adapter = new CourseRecyclerAdapter(this, mCourses);

    ArrayList<NavItem> mNavItems = new ArrayList<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initCourses();
        initRecyclerView();

        mNavItems.add(new NavItem("Home", "Meetup destination", R.drawable.ic_action_home));
        mNavItems.add(new NavItem("Preferences", "Change your preferences", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("About", "Get to know about us", R.drawable.ic_action_about));
        mNavItems.add(new NavItem("Logout", "Eject to reality", R.drawable.ic_action_logout));

        mDrawerLayout = findViewById(R.id.drawerLayout);

        // Populate the Navigation Drawer with options
        mDrawerPane = findViewById(R.id.drawerPane);
        mDrawerList = findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
    }

    /*
     * Called when a particular item from the navigation drawer
     * is selected.
     * */
    private void selectItemFromDrawer(int position) {

        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            default:
                return;
        }

        Toast.makeText(this, "Item was clicked", Toast.LENGTH_SHORT).show();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    public void initCourses() {

        // Read from the database
        myRef.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap values = (HashMap) dataSnapshot.getValue();
                Iterator it = values != null ? values.entrySet().iterator() : null;
                if (it != null) {
                    mCourses.clear();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        mCourses.add(pair.getKey().toString());
                        adapter.notifyDataSetChanged();
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                }
                Log.d(TAG, "Value is: " + values);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Values just for testing
//        mCourses.add("Biology");
//        myRef.setValue()
//        mCourses.add("Intro to Programming");
//        mCourses.add("Chemistry");
//        mCourses.add("History");
//        mCourses.add("English");
//        mCourses.add("Data Structures");
//        mCourses.add("Algorithms");
//        mCourses.add("Calculus I");
//        mCourses.add("Spanish");
//        mCourses.add("Automotive Repair");
//        mCourses.add("Java Basics");
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.courseRecycleView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addClass(View view) {
        //TODO - Add some kind of user feedback when they select a color for their pencil

        View mView = getLayoutInflater().inflate(R.layout.add_class_layout, null);
        final EditText editText = mView.findViewById(R.id.editTextClass);
        int maxLength = 22;
        //Filter user's class name input to maxLength: user will not be able to enter more characters
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        Button button = mView.findViewById(R.id.btnAddClass);

        final AlertDialog builder = new AlertDialog.Builder(HomeActivity.this).create();
        builder.setView(mView);
        builder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Class name empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String text = editText.getText().toString();
                    mCourses.add(text);
                    //TODO - Add the color of the pencil as the value of the key
                    myRef.child("Courses").child(text).setValue(text);
                    adapter.notifyDataSetChanged();
                    builder.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Handle button click here
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                impromptuToast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void impromptuToast(){
        Toast.makeText(HomeActivity.this, "INSERT MENU HERE!", Toast.LENGTH_LONG).show();
    }

    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = view.findViewById(R.id.title);
            TextView subtitleView = view.findViewById(R.id.subTitle);
            ImageView iconView = view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}


