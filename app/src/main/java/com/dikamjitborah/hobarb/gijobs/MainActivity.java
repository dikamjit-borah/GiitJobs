package com.dikamjitborah.hobarb.gijobs;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.dikamjitborah.hobarb.gijobs.Adapters.JobsAdapter;
import com.dikamjitborah.hobarb.gijobs.ApiHandling.ApiCalls;
import com.dikamjitborah.hobarb.gijobs.ApiHandling.RetrofitInstanceClass;
import com.dikamjitborah.hobarb.gijobs.Model.JobSchema;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    JobsAdapter jobsAdapter;
    ArrayList<JobSchema> jobSchema;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lv_main);

        ApiCalls service = RetrofitInstanceClass.getRetrofitInstance().create(ApiCalls.class);
        Call<List<JobSchema>> call = null;
        try {

            //call = service.getChapterNames(Helper.getToken(FilterActivity.this),subId);
            call = service.getJobs(1);
            call.enqueue(new Callback<List<JobSchema>>() {
                @Override
                public void onResponse(Call<List<JobSchema>> call, Response<List<JobSchema>> response) {
                    jobSchema = (ArrayList<JobSchema>) response.body();
                    jobsAdapter = new JobsAdapter(getApplicationContext(), jobSchema);
                    listView.setAdapter(jobsAdapter);


                }

                @Override
                public void onFailure(Call<List<JobSchema>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "OnFailure: "+ t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Exception: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }





    }
}