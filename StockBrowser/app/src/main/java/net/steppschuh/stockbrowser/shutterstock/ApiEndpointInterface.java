package net.steppschuh.stockbrowser.shutterstock;

import retrofit.http.GET;
import rx.Observable;

public interface ApiEndpointInterface {

    public static final String API_VERSION = "/v2";

    @GET(API_VERSION + "/images/collections/featured")
    Observable<CollectionList> getFeaturedCollections();

    //TODO: add more endpoints

}
