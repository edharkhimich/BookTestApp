package com.appleeeee.bookstestapp.api;

import com.appleeeee.bookstestapp.model.Books;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/books/v1/volumes")
    Call<Books> getBooks(@Query("q") String q,
                         @Query("startIndex") int startIndex,
                         @Query("key") String key);

}
