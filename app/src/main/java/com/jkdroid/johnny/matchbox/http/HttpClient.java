package com.jkdroid.johnny.matchbox.http;

import com.jkdroid.johnny.matchbox.base.BaseSubscriber;
import com.jkdroid.johnny.matchbox.retrofit.APIService;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by johnny on 16/03/2017.
 */

public class HttpClient {

    private static final String BASE_URL = "http://rapapi.org/mockjsdata/15041/";
    private static final String TAG = HttpClient.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 10;
    private static APIService apiService;

    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile HttpClient instance;

    //定义一个共有的静态方法，返回该类型实例
    public static HttpClient getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (HttpClient.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    // 定义一个私有构造方法
    private HttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(
//                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
////                .cookieJar(new CookieManger(context))
//                .addInterceptor(new BaseInterceptor(mContext))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())//增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//增加返回值为RxJava的Oservable<T>的支持
                .build();
        apiService = retrofit.create(APIService.class);
    }

    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable)observable)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

//    public <T> Observable.Transformer<BaseResponse<T>, T> transformer() {
//
//        return new Observable.Transformer() {
//
//            @Override
//            public Object call(Object observable) {
//                return ((Observable) observable).map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
//            }
//        };
//    }
//
//    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
//        @Override public Observable<T> call(Throwable t) {
//            return Observable.error(ExceptionHandle.handleException(t));
//        }
//    }
//
//    private class HandleFuc<T> implements Func1<BaseResponse<T>, T> {
//        @Override
//        public T call(BaseResponse<T> response) {
//            if (!response.isOk()) throw new RuntimeException(response.getCode() + "" + response.getMsg() != null ? response.getMsg(): "");
//            return response.getData();
//        }
//    }


    public Call<RequestBody> get(String url, Map parameters) {
        return apiService.get(url, parameters);
    }

    public Subscription login(String url, Map parameters, BaseSubscriber<ResponseBody> subscriber) {
        return apiService.post(url, parameters)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }
}
