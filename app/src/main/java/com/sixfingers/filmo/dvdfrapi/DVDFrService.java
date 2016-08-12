package com.sixfingers.filmo.dvdfrapi;

import com.sixfingers.filmo.dvdfrapi.models.SupportType;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DVDFrService {
    String ENDPOINT = "http://www.dvdfr.com/api/";

    @GET("search.php")
    Call<ResponseBody> searchByGencode(
            @Query("gencode") String barcode,
            @Query("produit") SupportType type
    );

    @GET("search.php")
    Call<ResponseBody> searchByTitle(
            @Query("title") String title,
            @Query("produit") SupportType type
    );
}
