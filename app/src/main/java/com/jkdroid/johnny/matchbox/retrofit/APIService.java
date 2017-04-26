package com.jkdroid.johnny.matchbox.retrofit;

import com.jkdroid.johnny.matchbox.bean.PhoneResult;
import com.jkdroid.johnny.matchbox.bean.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

import java.util.Map;

/**
 * Created by johnny on 2017/3/13.
 */

public interface APIService {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Call<UserInfo> login(@Field("username") String username, @Field("password") String password);

    /**
     * 查询号码归属地 http://apis.baidu.com/apistore/mobilenumber/mobilenumber
     * baseUrl:http://apis.baidu.com/
     *
     * @param apikey
     * @param phone
     * @return
     */
    @GET("http://apis.baidu.com/apistore/mobilenumber/mobilenumber")
    Call<PhoneResult> getPhoneAddress(@Header("apikey") String apikey, @Query("phone") String phone);

/**
     * 查询号码归属地(RxJava) http://apis.baidu.com/apistore/mobilenumber/mobilenumber
     * baseUrl:http://apis.baidu.com/
     *
     * @param apikey
     * @param phone
     * @return
     */
    @GET("http://apis.baidu.com/apistore/mobilenumber/mobilenumber")
    Observable<PhoneResult> getPhoneAddressByRxJava(@Header("apikey") String apikey, @Query("phone") String phone);

    /**
     * 直接请求某一地址
     * baseUrl:http://apis.baidu.com/
     *
     * @return
     */
    @GET("getRecord")
    Call<PhoneResult> getResult();

    /**
     * 组合请求某一地址
     * baseUrl:http://apis.baidu.com/
     *
     * @param id
     * @return
     */
    @GET("getRecord/{id}")
    Call<PhoneResult> getResult(@Path("id") String id);

    /**
     * 12306的查询接口
     * baseUrl:https://kyfw.12306.cn/
     *
     * @param purpose_codes
     * @param queryDate
     * @param from_station
     * @param to_station
     * @return
     */
    @GET("otn/lcxxcx/query")
    Call<PhoneResult> query(@Query("purpose_codes") String purpose_codes,
                            @Query("queryDate") String queryDate,
                            @Query("from_station") String from_station,
                            @Query("to_station") String to_station);

    /**
     * 更新某个账户信息
     * 接口地址为/info,需要带的Header有设备信息device,系统版本version,还要带请求参数要更新账户的id
     *
     * @param device
     * @param version
     * @param id
     * @return
     */
    @POST("info")
    Call<Object> updateInfo(@Header("device") String device,
                            @Header("version") String version,
                            @Field("id") String id);

    /**
     * Get请求
     * @param url
     * @param maps
     * @return
     */
    @GET("{url}")
    Call<ResponseBody> get(@Path("url") String url, @QueryMap Map<String,String> maps);

//    /**
//     * POST请求
//     * @param url
//     * @param maps
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("{url}")
//    Call<ResponseBody> post(@Path("url") String url, @FieldMap Map<String,String> maps);

    /**
     * POST请求(RxJava)
     * @param url
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> post(@Path("url") String url, @FieldMap Map<String,String> maps);



    /**
     * 上传单个文件
     * @param url
     * @param requestBody
     * @return
     */
    @Multipart
    @POST("{url}")
    Call<ResponseBody> uploadFile(@Path("url") String url,
                                  @Part("image\";filename=\"image.jpg") RequestBody requestBody);

    /**
     * 上传多个文件
     * @param url
     * @param description
     * @param maps
     * @return
     */
    @Multipart
    @POST()
    Call<ResponseBody> uploadFiles(@Url String url,
                                   @Part("filename") String description,
                                   @PartMap() Map<String,RequestBody> maps);

    /**
     * 上传多个文件
     * @param url
     * @param description
     * @param file
     * @return
     */
    @Multipart
    @POST()
    Call<ResponseBody> uploadFiles(@Url String url,
                                   @Part("description") RequestBody description,
                                   @Part("filekey") MultipartBody.Part file);
}
