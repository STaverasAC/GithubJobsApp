package nyc.c4q.githubjobs.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nyc.c4q.githubjobs.R;
import nyc.c4q.githubjobs.model.Job;
import nyc.c4q.githubjobs.view.JobViewHolder;

/**
 * Created by Shant on 12/12/2017.
 */
//TODO Make adapter
public class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {

    private List<Job> jobsList;

    public JobAdapter(List<Job> jobsList) {
        this.jobsList = jobsList;

    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobs_card, parent, false);
        return new JobViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job jobs = jobsList.get(position);
        holder.onBind(jobs);

    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }
}
