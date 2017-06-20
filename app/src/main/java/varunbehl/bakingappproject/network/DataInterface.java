package varunbehl.bakingappproject.network;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;
import varunbehl.bakingappproject.pojo.BakingData;


interface DataInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<BakingData>> getBakingData();

}
