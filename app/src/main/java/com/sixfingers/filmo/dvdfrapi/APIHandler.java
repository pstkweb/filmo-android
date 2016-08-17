package com.sixfingers.filmo.dvdfrapi;

import com.sixfingers.filmo.dvdfrapi.models.Errors;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;

public class APIHandler {
    private DVDFrService service;
    private Errors errors;

    public APIHandler() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        service = new Retrofit.Builder()
                .baseUrl(DVDFrService.ENDPOINT)
                .client(httpClient.build())
                .build()
                .create(DVDFrService.class);

        errors = null;
    }

    public SearchResult searchByGencode(String barcode, SupportType mediaType) throws IOException {
        Response<ResponseBody> response = service.searchByGencode(barcode, mediaType).execute();
        Serializer serializer = new Persister();

        try {
            errors = serializer.read(Errors.class, response.body().string());
        } catch (Exception e) {
            try {
                return serializer.read(SearchResult.class, response.body().string());
            } catch (Exception ignored) { }
        }

        return null;
    }

    public Errors getErrors() {
        return errors;
    }
}
