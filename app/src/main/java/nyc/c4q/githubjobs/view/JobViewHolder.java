package nyc.c4q.githubjobs.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nyc.c4q.githubjobs.R;
import nyc.c4q.githubjobs.model.Job;
import retrofit2.http.Url;

/**
 * Created by Shant on 12/12/2017.
 */

//TODO Make View Holder
public class JobViewHolder extends RecyclerView.ViewHolder{

    private TextView title;
    private TextView company;
    private TextView location;
    private TextView type;
    private Button apply;

    public JobViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_view);
        company = itemView.findViewById(R.id.company_view);
        location = itemView.findViewById(R.id.location_view);
        type = itemView.findViewById(R.id.type_view);
        apply = itemView.findViewById(R.id.apply_view);

    }

    public void onBind(final Job job){
        title.setText(job.getTitle());
        company.setText(job.getCompany());
        location.setText(job.getLocation());
        type.setText(job.getType());
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goJobURL = new Intent(Intent.ACTION_VIEW, Uri.parse(job.getUrl()));
                view.getContext().startActivity(goJobURL);
            }
        });

    }





}
