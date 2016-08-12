package com.sixfingers.filmo.dvdfrapi;

import com.sixfingers.filmo.dvdfrapi.models.Errors;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

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
        SimpleXmlConverterFactory factory = SimpleXmlConverterFactory.create();

        try {
            errors = (Errors) factory.responseBodyConverter(
                    Errors.class,
                    Errors.class.getAnnotations(),
                    null
            ).convert(response.body());

            return null;
        } catch (RuntimeException e) {
            return (SearchResult) factory.responseBodyConverter(
                    SearchResult.class,
                    SearchResult.class.getAnnotations(),
                    null
            ).convert(response.body());
        }
    }

    public Errors getErrors() {
        return errors;
    }
}
