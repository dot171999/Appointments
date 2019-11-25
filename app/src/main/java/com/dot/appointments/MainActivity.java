package com.dot.appointments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dot.appointments.custom.Event;
import com.dot.appointments.logic.MyComparator;
import com.dot.appointments.logic.stEvent;
import com.dot.appointments.logic.stRect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import static com.dot.appointments.custom.Tools.dpToPx;
import static com.dot.appointments.custom.Tools.getDay;
import static com.dot.appointments.custom.Tools.getWeek;
import static com.dot.appointments.custom.Tools.pxToDp;
import static com.dot.appointments.custom.Tools.timeToAmPm;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    int optionClicked = 0;
    ProgressBar progressBar;

    RequestQueue queue;
    String URL = "https://recruiter-static-content.s3.ap-south-1.amazonaws.com/json_responses_for_tests/test.json";
    String[] timeLine;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private static final int MINUTES_IN_A_HOUR = 12 * 60;
    private static List<Event> mainEvents;

    private RelativeLayout rlCalendarRoot;

    static int screenHeight;

    private static int EVENT_START = 0, EVENT_STOP = 1;

    TextView tvWeek,tvDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvWeek = findViewById(R.id.toolbar_week);
        tvWeek.setText(getWeek());
        tvDay = findViewById(R.id.toolbar_day);
        tvDay.setText(getDay());

        rlCalendarRoot = (RelativeLayout)findViewById(R.id.rl_calendar_root);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        draw(false);

        getData();
    }

    void getData(){
        queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mainEvents = new ArrayList<>();
                try{
                    // Loop through the array elements
                    for(int i=0;i<response.length();i++){
                        // Get current json object
                        JSONObject event = response.getJSONObject(i);

                        // Get the current (json object) data
                        String title = event.getString("title");
                        String start = event.getString("start");
                        String end = event.getString("end");
                        if(Integer.parseInt(start.substring(0,2)) < 9 || Integer.parseInt(start.substring(0,2)) > 20
                                || Integer.parseInt(end.substring(0,2)) > 20){
                            continue;
                        }
                        mainEvents.add(new Event(i, "Event "+title,start,end));
                    }
                    drawEvents(mainEvents);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(jsonArrayRequest);
    }

    void draw(final boolean flag){
        rlCalendarRoot.post(new Runnable() {
            @Override
            public void run() {
                if(flag){
                    rlCalendarRoot.removeAllViews();
                    if(optionClicked == 2)
                    drawEvents(mainEvents);
                }
                timeLine = getResources().getStringArray(R.array.timeLine);
                mAdapter = new MyAdapter(timeLine,rlCalendarRoot.getHeight());
                recyclerView.setAdapter(mAdapter);
                Log.e("$$$$","hello");
            }
        });
    }

    private static void arrangeRectangles(ArrayList<stRect>rectArray, int length, int overlapLimit, int containerWidth){

        PriorityQueue<stEvent> eventQueue = new PriorityQueue<>(new MyComparator());
        Queue<stEvent> regionQueue = new LinkedList<>();

        for (int i = 0; i < length; i++) {
            stEvent startEvent = new stEvent(rectArray.get(i).topY,EVENT_START, i);
            eventQueue.add(startEvent);
            stEvent stopEvent = new stEvent(rectArray.get(i).bottomY,EVENT_STOP, i);
            eventQueue.add(stopEvent);
        }

        while (!eventQueue.isEmpty()){
            int overlap = 0;
            int maxOverlap = 0;
            stEvent event;

            while (!eventQueue.isEmpty()){ // take from the event queue
                event = eventQueue.remove();

                regionQueue.add(event);    // save in the region queue

                if (event.type == EVENT_START)
                    overlap++;
                else
                    overlap--;

                if (overlap == 0)          // reached the end of a region
                    break;

                if (overlap > maxOverlap)
                    maxOverlap = overlap;
            }

            // limit the overlap as specified by the function parameter
            if (maxOverlap > overlapLimit)
                maxOverlap = overlapLimit;

            // compute the width to be used for rectangles in this region
            int width = containerWidth / maxOverlap;

            int usedColumns[] = new int[maxOverlap];
            for (int i = 0; i < maxOverlap; i++)
                usedColumns[i] = -1;

            while (!regionQueue.isEmpty()){
                event = regionQueue.remove();

                if (event.type == EVENT_START) {
                    // find an available column for this rectangle, and assign the X values
                    for (int column = 0; column < maxOverlap; column++){
                        if (usedColumns[column] < 0) {
                            usedColumns[column] = event.rectID;
                            rectArray.get(event.rectID).leftX = column * width;
                            rectArray.get(event.rectID).rightX = (column+1) * width;
                            break;
                        }
                    }
                }else {
                    // free the column that's being used for this rectangle
                    for (int i = 0; i < maxOverlap; i++){
                        if (usedColumns[i] == event.rectID)
                        {
                            usedColumns[i] = -1;
                            break;
                        }
                    }
                }
            }
        }
        eventQueue.clear();
        regionQueue.clear();
    }

    public void drawEvents(List<Event> events) {
        if(events != null && !events.isEmpty()) {
            ArrayList<stRect> inputArray = new ArrayList<>();

            screenHeight = rlCalendarRoot.getHeight();

            for(int i=0;i<events.size();i++){
                inputArray.add(new stRect( minutesToPixels(screenHeight, events.get(i).getStartTimeInMinutes())
                        ,minutesToPixels(screenHeight, events.get(i).getEndTimeInMinutes())
                        ,-1,-1,events.get(i).getId(),events.get(i).getName()));
            }

            //Collections.sort(events, new TimeComparator());
            int screenWidth = rlCalendarRoot.getWidth() - dpToPx(70);

            arrangeRectangles(inputArray, inputArray.size(), 50, screenWidth);



            //List<Crowd> crowds = createCrowds(createGroups(events));


            for (stRect event : inputArray) {
                int eventWidth = event.rightX-event.leftX;
                int leftMargin = dpToPx(65) + ((event.leftX));
                int eventHeight = event.bottomY-event.topY;
                int topMargin = event.topY;

                TextView eventView = new TextView(this);
                eventView.setOnClickListener(this);

                eventView.setId(event.getId());
                eventView.setGravity(Gravity.CENTER);
                eventView.setText(event.name);
                eventView.setTextColor(Color.BLACK);
                eventView.setBackgroundResource(R.color.amber_300);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(eventWidth - dpToPx(5), eventHeight);
                params.setMargins(leftMargin + dpToPx(5), topMargin, 0, 0);
                rlCalendarRoot.addView(eventView, params);
            }

        }
    }

    private int minutesToPixels(int screenHeight, int minutes){
        return (screenHeight * minutes) / MINUTES_IN_A_HOUR;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Event eventTemp = null;
        final Dialog dialog= new Dialog(MainActivity.this,R.style.PauseDialog);
        for(int i = 0; i< mainEvents.size(); i++){
            if(mainEvents.get(i).getId() == id){
                eventTemp = mainEvents.get(i);
            }
        }
        dialog.setTitle(eventTemp.getName());
        dialog.setContentView(R.layout.botton_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView textView = dialog.findViewById(R.id.popup_text);
        textView.setText(timeToAmPm(eventTemp.getStartTime()+"") +" to "+timeToAmPm(eventTemp.getEndTime()+""));

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            optionClicked = 1;
            progressBar.setVisibility(View.VISIBLE);
            draw(true);
            getData();
            return true;
        }else if(id == R.id.action_add) {
            optionClicked = 2;
            addRandomEvent();
            draw(true);
            drawEvents(mainEvents);
            Toast.makeText(this,"Random Event generated", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    void addRandomEvent(){
        Random random = new Random();
        String start = "1";
        String end = "1";
        start = start +""+ random.nextInt(9) +""+ random.nextInt(4) +""+ random.nextInt(4);
        end = end +""+ random.nextInt(9) +""+ random.nextInt(4) +""+ random.nextInt(4);

        if(Integer.parseInt(start)>Integer.parseInt(end)){
            mainEvents.add(new Event(mainEvents.size()+1, "Event "+(mainEvents.size()+1),end,start));
        }else{
            mainEvents.add(new Event(mainEvents.size()+1, "Event "+(mainEvents.size()+1),start,end));
        }
    }

}
