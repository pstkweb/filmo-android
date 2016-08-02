package com.sixfingers.filmo.dvdfrapi;

import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DVDFrService {
    String ENDPOINT = "http://www.dvdfr.com/api/";

    @GET("search.php")
    Call<SearchResult> searchByGencode(
            @Query("gencode") String barcode,
            @Query("produit") SupportType type
    );

    @GET("search.php")
    Call<SearchResult> searchByTitle(
            @Query("title") String title,
            @Query("produit") SupportType type
    );
}
