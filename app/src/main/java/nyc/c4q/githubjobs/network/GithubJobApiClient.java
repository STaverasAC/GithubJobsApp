package nyc.c4q.githubjobs.network;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nyc.c4q.githubjobs.model.Job;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shant on 12/12/2017.
 */

public class GithubJobApiClient implements Callback<List<Job>> {

    final static String BASE_URL = "https://jobs.github.com";
    final static String TAG = "JOBS RESULT:";
    List<Job> jobsList;

    public void start(Map<String, String> map) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubJobApi githubJobApi = retrofit.create(GithubJobApi.class);
        Call<List<Job>> call = githubJobApi.getJobs(map);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
        if(response.isSuccessful()){
            jobsList = response.body();
            for(Job j:jobsList){
                Log.d(TAG,j.getTitle());
            }
        }
    }

    @Override
    public void onFailure(Call<List<Job>> call, Throwable t) {
        t.printStackTrace();
    }

    public List<Job> getJobsList() {
        return jobsList;
    }
}
