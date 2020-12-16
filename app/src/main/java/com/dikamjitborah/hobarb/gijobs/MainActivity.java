package com.dikamjitborah.hobarb.gijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dikamjitborah.hobarb.gijobs.Adapters.JobsAdapter;
import com.dikamjitborah.hobarb.gijobs.ApiHandling.ApiCalls;
import com.dikamjitborah.hobarb.gijobs.ApiHandling.RetrofitInstanceClass;
import com.dikamjitborah.hobarb.gijobs.Model.JobSchema;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ListView listView;
    JobsAdapter jobsAdapter;
    ArrayList<JobSchema> jobSchema;
    Toolbar toolbar1;
    public int currentPage = 1;
    public View pb_load_view;
    public boolean pb_loading = false;
    Handler pb_handler;
    Boolean apihasbeencalled = false;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar1 = findViewById(R.id.tb_main);

        setSupportActionBar(toolbar1);

        toolbar1.setTitleTextAppearance(this, R.style.font_default);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        pb_load_view = layoutInflater.inflate(R.layout.loading_footer, null);
        pb_handler = new loadingHandler();
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        listView = findViewById(R.id.lv_main);
         floatingActionButton = findViewById(R.id.fab_main);
         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(MainActivity.this, "Refreshing!", Toast.LENGTH_SHORT).show();
                 currentPage = 1;
                 callApi();
             }
         });




        try {
          callApi();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Exception: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }






    }

    private void callApi() {
        showProgressBar(this);
        Call<List<JobSchema>> call = null;
        ApiCalls service = RetrofitInstanceClass.getRetrofitInstance().create(ApiCalls.class);
        call = service.getJobs(currentPage);
        currentPage++;
        call.enqueue(new Callback<List<JobSchema>>() {
            @Override
            public void onResponse(Call<List<JobSchema>> call, Response<List<JobSchema>> response) {
                jobSchema = (ArrayList<JobSchema>) response.body();
                jobsAdapter = new JobsAdapter(getApplicationContext(), jobSchema);
                //Toast.makeText(MainActivity.this, "" + currentPage, Toast.LENGTH_SHORT).show();
                if(response.body().equals("[]"))
                {
                    currentPage = 1;
                    Toast.makeText(MainActivity.this, "No more new jobs! Press Refresh", Toast.LENGTH_SHORT).show();
                    return;
                }
                    
                if(apihasbeencalled==false)
                {

                    listView.setAdapter(jobsAdapter);
                }
                
                else {
                    
                    jobsAdapter.addListItemToAdapter(jobSchema);
                }
                hideProgressBar();
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                        if(absListView.getLastVisiblePosition() == jobSchema.size()-1 && pb_loading==false)
                        {
                            pb_loading=true;
                            Thread thread = new threadGetMoreData();
                            thread.start();
                        }

                    }
                });


            }

            @Override
            public void onFailure(Call<List<JobSchema>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "OnFailure: "+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb1_menu, menu);
        MenuItem menuItem1 = menu.findItem(R.id.search_tb1_menu);
        SearchView searchView = (SearchView) menuItem1.getActionView();

       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                discussionForumAdapter.getFilter().filter(s);
                return true;
            }
        });*/
        return true;
    }

    public class loadingHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    listView.addFooterView(pb_load_view);
                    break;
                case 1:
                    jobsAdapter.addListItemToAdapter((ArrayList<JobSchema>) msg.obj);
                    listView.removeFooterView(pb_load_view);
                    pb_loading = false;
                    break;
                default:
                    break;
            }
        }
    }

    public class threadGetMoreData extends Thread{
        @Override
        public void run() {
            pb_handler.sendEmptyMessage(0);
            ArrayList<JobSchema> halves_got = getmoredata();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = pb_handler.obtainMessage(1, halves_got);
            pb_handler.sendMessage(msg);

        }
    }

    private ArrayList<JobSchema> getmoredata(){
        ArrayList<JobSchema> halves = new ArrayList<>();
        callApi();

        return halves;
    }

}