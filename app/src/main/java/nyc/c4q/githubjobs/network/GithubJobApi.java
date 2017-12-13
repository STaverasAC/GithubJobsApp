package nyc.c4q.githubjobs.network;

import java.util.List;
import java.util.Map;

import nyc.c4q.githubjobs.model.Job;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Shant on 12/12/2017.

 */

public interface GithubJobApi {

    final static String BASE_URL = "https://jobs.github.com";

    @GET("/positions.json")
    /*The call creates a List of Job classes based on the result from the callback
    A querymap allows you to modify multiple queries for the api call
    we will be passing a query for description and location to get jobs of a specified type such as skill
    and location to specify place*/
    Call<List<Job>> getJobs(@QueryMap Map<String,String> type);



}
