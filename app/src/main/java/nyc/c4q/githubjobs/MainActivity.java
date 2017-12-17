package nyc.c4q.githubjobs;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import nyc.c4q.githubjobs.controller.JobAdapter;
import nyc.c4q.githubjobs.model.Job;
import nyc.c4q.githubjobs.network.GithubJobApi;
import nyc.c4q.githubjobs.network.GithubJobApiClient;

public class MainActivity extends AppCompatActivity {
    final static String TAG ="DATA CHECKS: ";
    GithubJobApiClient githubJobApiClient;
    List<Job> jobsList;
    final static String SHARED_PREF_KEY ="SharedPreferences";
    SharedPreferences storedTypeAndLocation;
    EditText typeInput;
    String type;
    String location;

    EditText locationInput;
    CheckBox checkBox;
    Button getFeed;
    RecyclerView jobsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storedTypeAndLocation = getSharedPreferences(SHARED_PREF_KEY,0);
        setViewReferences();


        //TODO set up recycler view using jobsList;

        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = typeInput.getText().toString();
                location = locationInput.getText().toString();
                getJobsListFromAPI();
                storePreferences();

            }
        });

    }

    private void setViewReferences(){
        typeInput = (EditText) findViewById(R.id.type_edit_text);
        locationInput =(EditText) findViewById(R.id.location_edit_text);
        checkBox = (CheckBox) findViewById(R.id.checkbox_preferences);
        getFeed = (Button) findViewById(R.id.get_feed_button);
        if (storedTypeAndLocation.getBoolean("remember", false)) {
            typeInput.setText(storedTypeAndLocation.getString("type", null));
            locationInput.setText(storedTypeAndLocation.getString("location", null));
            checkBox.setChecked(storedTypeAndLocation.getBoolean("remember", false));
        }

    }

    private void storePreferences(){
        SharedPreferences.Editor edit = storedTypeAndLocation.edit();
        if(checkBox.isChecked()){
            edit.putString("type",typeInput.getText().toString());
            edit.putString("location",locationInput.getText().toString());
            edit.putBoolean("remember",checkBox.isChecked());
            edit.commit();
        }else {
            edit.putBoolean("remember",checkBox.isChecked());
            edit.commit();
        }
    }

    private void getJobsListFromAPI(){

        Map<String,String> map = fetchJobsByTypeAndLocation(type,location);
        githubJobApiClient = new GithubJobApiClient();
        githubJobApiClient.start(map);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG,"STARTING TO SET VARIABLES");
                        setVariables();
                    }
                }, 2000
        );

    }

    private Map<String,String> fetchJobsByTypeAndLocation(String type, String location){
        Map<String,String> jobCall = new HashMap<>();
        jobCall.put("description",type);
        jobCall.put("location",location);
        return jobCall;
    }


    public void setVariables(){
        jobsList = githubJobApiClient.getJobsList();
        Log.d(TAG,"SIZE OF LIST:"+jobsList.size());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                setRecView();
            }
        });
    }

    public void setRecView(){
        jobsRecyclerView = findViewById(R.id.jobs_recyclerview);
        JobAdapter jobAdapter = new JobAdapter(jobsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        jobsRecyclerView.setAdapter(jobAdapter);
        jobsRecyclerView.setLayoutManager(linearLayoutManager);
    }


}
