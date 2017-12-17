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
import nyc.c4q.githubjobs.fragments.JobsFragment;
import nyc.c4q.githubjobs.model.Job;
import nyc.c4q.githubjobs.network.GithubJobApi;
import nyc.c4q.githubjobs.network.GithubJobApiClient;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         JobsFragment jobsFragment = new JobsFragment();
         getSupportFragmentManager().beginTransaction().replace(R.id.jobs_container,jobsFragment).commit();

    }


}
