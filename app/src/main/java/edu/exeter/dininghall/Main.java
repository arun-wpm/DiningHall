package edu.exeter.dininghall;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import org.json.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Main extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textViewDay;
    private Button Elm, Weth;
    private ImageButton prevDay, nextDay;
    //http://www.json.org/
    //http://stleary.github.io/JSON-java/index.html
    int done = 0;
    String raw;
    JSONArray MenuArray;

    JSONObject MenuObject;
    int DaySelected;
    int MealSelected;
    int whichDHall = 0;
    String[] myDataset;

    private String Cleanup(String str)
    {
        str = str.replaceAll("\\r\\n","");
        str = str.replaceAll("<.{1,3}>","");
        str = str.replaceAll("&nbsp;","");
        str = str.replaceAll("&amp;","&");
        str = str.replaceAll("&#232;","è");
        Log.e("TAG", str);
        return str;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //TODO: delete the "" menus
            switch (item.getItemId()) {
                case R.id.navigation_breakfast:
                    MealSelected = 0;
                    if (done == 0) return true;
                    try {
                        MenuObject = MenuArray.getJSONObject(DaySelected);
                        myDataset = MenuObject.getString("Breakfast").split("</div>");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.navigation_lunch:
                    MealSelected = 1;
                    if (done == 0) return true;
                    try {
                        MenuObject = MenuArray.getJSONObject(DaySelected);
                        myDataset = MenuObject.getString("Lunch").split("</div>");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.navigation_dinner:
                    MealSelected = 2;
                    if (done == 0) return true;
                    try {
                        MenuObject = MenuArray.getJSONObject(DaySelected);
                        myDataset = MenuObject.getString("Dinner").split("</div>");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    return false;
            }
            for (int i = 0; i < myDataset.length; i++)
                myDataset[i] = Cleanup(myDataset[i]);
            mAdapter = new MyAdapter(myDataset, whichDHall);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        }
    };

    public Main() throws JSONException {
        //TODO: error handling
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.e("TAG", "just show me something in the log pls");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        textViewDay = (TextView) findViewById(R.id.Date);
        prevDay = (ImageButton) findViewById(R.id.prevDay);
        nextDay = (ImageButton) findViewById(R.id.nextDay);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new Download().execute(getString(R.string.source));
//        Log.e("TAG", raw);

        //TODO: settings menu

        //day tabs
        prevDay.setVisibility(View.INVISIBLE);
        displayDay();
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaySelected--;
                if (DaySelected == 0) {
                    prevDay.setVisibility(View.INVISIBLE);
                    nextDay.setVisibility(View.VISIBLE);
                }
                else if (DaySelected == 6) {
                    prevDay.setVisibility(View.VISIBLE);
                    nextDay.setVisibility(View.INVISIBLE);
                }
                else
                {
                    prevDay.setVisibility(View.VISIBLE);
                    nextDay.setVisibility(View.VISIBLE);
                }
                displayDay();
                switch (MealSelected) {
                    case 0:
                        if (done == 0) break;
                        try {
                            MenuObject = MenuArray.getJSONObject(DaySelected);
                            myDataset = MenuObject.getString("Breakfast").split("</div>");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        if (done == 0) break;
                        try {
                            MenuObject = MenuArray.getJSONObject(DaySelected);
                            myDataset = MenuObject.getString("Lunch").split("</div>");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        if (done == 0) break;
                        try {
                            MenuObject = MenuArray.getJSONObject(DaySelected);
                            myDataset = MenuObject.getString("Dinner").split("</div>");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                for (int i = 0; i < myDataset.length; i++)
                    myDataset[i] = Cleanup(myDataset[i]);
                mAdapter = new MyAdapter(myDataset, whichDHall);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaySelected++;
                if (DaySelected == 0) {
                    prevDay.setVisibility(View.INVISIBLE);
                    nextDay.setVisibility(View.VISIBLE);
                }
                else if (DaySelected == 6) {
                    prevDay.setVisibility(View.VISIBLE);
                    nextDay.setVisibility(View.INVISIBLE);
                }
                else
                {
                    prevDay.setVisibility(View.VISIBLE);
                    nextDay.setVisibility(View.VISIBLE);
                }
                displayDay();
                switch (MealSelected) {
                    case 0:
                        if (done == 0) break;
                        try {
                            MenuObject = MenuArray.getJSONObject(DaySelected);
                            myDataset = MenuObject.getString("Breakfast").split("</div>");
                        } catch (JSONException e) {
                            myDataset = new String[0];
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        if (done == 0) break;
                        try {
                            MenuObject = MenuArray.getJSONObject(DaySelected);
                            myDataset = MenuObject.getString("Lunch").split("</div>");
                        } catch (JSONException e) {
                            myDataset = new String[0];
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        if (done == 0) break;
                        try {
                            MenuObject = MenuArray.getJSONObject(DaySelected);
                            myDataset = MenuObject.getString("Dinner").split("</div>");
                        } catch (JSONException e) {
                            myDataset = new String[0];
                            e.printStackTrace();
                        }
                        break;
                }
                for (int i = 0; i < myDataset.length; i++)
                    myDataset[i] = Cleanup(myDataset[i]);
                mAdapter = new MyAdapter(myDataset, whichDHall);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        //Elm/Weth
        Elm = (Button) findViewById(R.id.buttonElm);
        Weth = (Button) findViewById(R.id.buttonWeth);
        Elm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichDHall = 0;
                mAdapter = new MyAdapter(myDataset, whichDHall);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        Weth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichDHall = 1;
                mAdapter = new MyAdapter(myDataset, whichDHall);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        //TODO: rating

        //TODO: special message
    }

    // https://developer.android.com/training/articles/perf-anr
    public class Download extends AsyncTask<String, Void, Integer> {
        // Do the long-running work in here
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Integer doInBackground(String... strings) {
            try {
                raw = readStringFromURL(strings[0]);
                Log.e("TAG", raw);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onPostExecute(Integer i) {
            Log.e("TAG","done!");
            done = 1;
            try {
                MenuArray = new JSONArray(raw);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Display according to navigation bar / meal selected
            //https://developer.android.com/guide/topics/ui/layout/recyclerview
            //https://www.androidhive.info/2016/01/android-working-with-recycler-view/
            switch(MealSelected) {
                case 0:
                    try {
                        MenuObject = MenuArray.getJSONObject(DaySelected);
                        myDataset = MenuObject.getString("Breakfast").split("</div>");
                    } catch (JSONException e) {
                        myDataset = new String[0];
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        MenuObject = MenuArray.getJSONObject(DaySelected);
                        myDataset = MenuObject.getString("Lunch").split("</div>");
                    } catch (JSONException e) {
                        myDataset = new String[0];
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        MenuObject = MenuArray.getJSONObject(DaySelected);
                        myDataset = MenuObject.getString("Dinner").split("</div>");
                    } catch (JSONException e) {
                        myDataset = new String[0];
                        e.printStackTrace();
                    }
            }
            for (i = 0; i < myDataset.length; i++)
                myDataset[i] = Cleanup(myDataset[i]);
            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(myDataset, whichDHall);
            mRecyclerView.setAdapter(mAdapter);
        }

        //https://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public String readStringFromURL(String requestURL) throws IOException
        {
            try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                    StandardCharsets.UTF_8.toString()))
            {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        }
    }

    public void displayDay()
    {
        Calendar now;
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, DaySelected);
        switch(DaySelected)
        {
            case 0:
                textViewDay.setText("Today, "+ now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+ " " + now.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " " + now.get(Calendar.DAY_OF_MONTH));
                break;
            case 1:
                textViewDay.setText("Tomorrow, "+ now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+ " " + now.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " " + now.get(Calendar.DAY_OF_MONTH));
                break;
            default:
                textViewDay.setText(now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+ " " + now.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " " + now.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
}