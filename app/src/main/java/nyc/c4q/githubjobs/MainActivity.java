package nyc.c4q.githubjobs;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nyc.c4q.githubjobs.model.Job;
import nyc.c4q.githubjobs.network.GithubJobApi;
import nyc.c4q.githubjobs.network.GithubJobApiClient;

public class MainActivity extends AppCompatActivity {
    List<Job> jobsList;
    final static String SHARED_PREF_KEY ="SharedPreferences";
    SharedPreferences storedTypeAndLocation;
    EditText typeInput;
    EditText locationInput;
    CheckBox checkBox;
    Button getFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storedTypeAndLocation = getSharedPreferences(SHARED_PREF_KEY,0);
        setViewReferences();
        getJobsListFromAPI();

        //TODO set up recycler view using jobsList;




        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        Map<String,String> map = fetchJobsByTypeAndLocation("python","NewYork");
        GithubJobApiClient githubJobApiClient = new GithubJobApiClient();
        githubJobApiClient.start(map);
        jobsList = githubJobApiClient.getJobsList();
    }

    private Map<String,String> fetchJobsByTypeAndLocation(String type, String location){
        Map<String,String> jobCall = new HashMap<>();
        jobCall.put("description",type);
        jobCall.put("location",location);
        return jobCall;
    }
}
