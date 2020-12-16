package com.dikamjitborah.hobarb.gijobs.Adapters;



import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dikamjitborah.hobarb.gijobs.Model.JobSchema;
import com.dikamjitborah.hobarb.gijobs.R;
import com.dikamjitborah.hobarb.gijobs.WebViewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class JobsAdapter extends ArrayAdapter<JobSchema> {

    Context df_context;
    ArrayList<JobSchema> jobSchema;
    ArrayList<JobSchema> jobSchema_all;

    public JobsAdapter(Context c, ArrayList<JobSchema> jobSchema){
        super(c, R.layout.rows_main, R.id.title_rm, jobSchema);
        this.df_context = c;
        this.jobSchema = jobSchema;
//        jobSchema_all = new ArrayList<>(jobSchema);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        JobSchema jobData = jobSchema.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.rows_main, parent, false);
        TextView rTitle, rType, rCreatedAt, rCompany, rLocation, rDescription;
        ImageView rImg;
        rImg = row.findViewById(R.id.img_rm);
      //Glide.with(df_context).load(jobData.getCompany_logo()).placeholder(R.drawable.loading).fitCenter().into(rImg);
        Glide.with(getContext()).load(jobData.getCompany_logo())
                .thumbnail(Glide.with(getContext()).load(R.drawable.loading))
                .fitCenter()
                .into(rImg);

      /*  Glide.with(df_context)
                .load(jobData.getCompany_logo())
                .transition(withCrossFade())
                .apply(new RequestOptions().override(100,100)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading).fitCenter()
                )
                .into(rImg);*/

        rTitle = row.findViewById(R.id.title_rm);
        rCreatedAt = row.findViewById(R.id.created_at_rm);
        rType = row.findViewById(R.id.type_rm);
        rCompany = row.findViewById(R.id.company_rm);
        rLocation = row.findViewById(R.id.location_rm);


        rTitle.setText(jobData.getTitle());
        rCreatedAt.setText(jobData.getCreated_at());
        rCompany.setText(jobData.getCompany());
        rType.setText(jobData.getType());
        rLocation.setText(jobData.getLocation());




        rDescription = row.findViewById(R.id.description_rm);



        rDescription.setText(jobData.getDescription().replaceAll( "<[^>]*>", ""));
       rDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent indisplay = new Intent(df_context, WebViewActivity.class);
                indisplay.putExtra("link",jobData.getUrl());
                indisplay.putExtra("id",jobData.getId());
                indisplay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                df_context.startActivity(indisplay);
            }
        });



        return row;



    }

    public void addListItemToAdapter(List<JobSchema> list){
        jobSchema.addAll(list);
        this.notifyDataSetChanged();


    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<JobSchema> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filtered.addAll(jobSchema_all);
            } else {
                String filter_pattern = charSequence.toString().toLowerCase().trim();

                for (JobSchema itemsBean : jobSchema_all) {
                    if (itemsBean.getTitle().toLowerCase().contains(filter_pattern)) {
                        filtered.add(itemsBean);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            jobSchema.clear();
            jobSchema.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


}
