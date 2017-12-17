package nyc.c4q.githubjobs.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import nyc.c4q.githubjobs.R;
import nyc.c4q.githubjobs.controller.JobAdapter;
import nyc.c4q.githubjobs.model.Job;
import nyc.c4q.githubjobs.network.GithubJobApiClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends Fragment {

    final static String TAG ="DATA CHECKS: ";
    GithubJobApiClient githubJobApiClient;
    List<Job> jobsList;
    final static String SHARED_PREF_KEY ="SharedPreferences";
    SharedPreferences storedTypeAndLocation;
    EditText typeInput;
    String type;
    String location;
    View rootView;

    EditText locationInput;
    CheckBox checkBox;
    Button getFeed;
    RecyclerView jobsRecyclerView;


    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        storedTypeAndLocation = getActivity().getSharedPreferences(SHARED_PREF_KEY,0);
        setViewReferences();
        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = typeInput.getText().toString();
                location = locationInput.getText().toString();
                getJobsListFromAPI();
                storePreferences();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }




    private void setViewReferences(){
        typeInput = (EditText) rootView.findViewById(R.id.type_edit_text);
        locationInput =(EditText) rootView.findViewById(R.id.location_edit_text);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkbox_preferences);
        getFeed = (Button) rootView.findViewById(R.id.get_feed_button);
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
                }, 3000
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

        jobsRecyclerView = rootView.findViewById(R.id.jobs_recyclerview);
        JobAdapter jobAdapter = new JobAdapter(jobsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        jobsRecyclerView.setAdapter(jobAdapter);
        jobsRecyclerView.setLayoutManager(linearLayoutManager);
    }

}
