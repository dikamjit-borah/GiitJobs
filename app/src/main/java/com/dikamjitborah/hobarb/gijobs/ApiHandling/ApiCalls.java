package com.dikamjitborah.hobarb.gijobs.ApiHandling;

import com.dikamjitborah.hobarb.gijobs.Model.JobSchema;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCalls {
    @GET("positions.json")
    Call<List<JobSchema>> getJobs(@Query("page") int page);

}
