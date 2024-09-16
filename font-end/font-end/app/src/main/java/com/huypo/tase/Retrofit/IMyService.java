package com.huypo.tase.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IMyService {



    @POST ("/user/login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password
    );

    @POST ("/user/register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("name") String name);

    @POST ("/user/name")
    @Headers({
            "Accept:application/json"
    })
    Observable<String> getNameUser(@Header("token") String token);

    @POST ("/project")
    @FormUrlEncoded
    Observable<String> createProject(@Header("token") String token,@Field("title") String title,
                                 @Field("description") String description,@Field("deadline") String deadline);

    @POST("/project/delete")
    @FormUrlEncoded
    Observable<String> deleteProject(@Header("token") String token,@Field("id") String id);

    @POST("/project/done")
    @FormUrlEncoded
    Observable<String> changeStatusDoneProject(@Header("token") String token,@Field("id") String id);

//    @POST("/project/task/list")
//    @FormUrlEncoded
//    Observable<String> showListTask(@Header("token") String token,@Field("id") String id);

    @POST("/project/tasks")
    @FormUrlEncoded
    Observable<String> showListTask(@Field("id") String id);

    @POST("/email")
    @FormUrlEncoded
    Observable<String> sendEmailProjectPartner(@Field("email") String email,@Field("title") String title,@Field("username") String username);

    @POST("/user/key")
    @FormUrlEncoded
    Observable<String> sendEmailChangePassword(@Field("email") String email);

    @POST("/user/changePassword")
    @FormUrlEncoded
    Observable<String> changePassword(@Field("email") String email,@Field("key") String key,@Field("pass") String pass);

    @POST("/project/task")
    @FormUrlEncoded
    Observable<String> addTask(@Header("token") String token, @Field("id") String id, @Field("title") String title,@Field("deadline") String deadline);

//    @POST("/project/task/item")
//    @FormUrlEncoded
//    Observable<String> addItemTask(@Header("token") String token, @Field("title") String title,@Field("idProject") String idProject, @Field("idTask") int idTask);

    @POST("/project/task/additem")
    @FormUrlEncoded
    Observable<String> addItemTask( @Field("title") String title,@Field("idProject") String idProject, @Field("idTask") int idTask);


    @POST("/project/partner")
    @FormUrlEncoded
    Observable<String> addEmailPartner(@Header("token") String token, @Field("id") String id,@Field("email") String email);

    @POST("/project/getProjectPartner")
    @Headers({
            "Accept:application/json"
    })
    Observable<String> getProjectPartner(@Header("token") String token);

    @POST("/project/partner/delete")
    @FormUrlEncoded
    Observable<String> deleteProjectPartner(@Header("token") String token,@Field("id") String id);


    @GET("/project")
    @Headers({
            "Accept:application/json"
    })
    Observable<String> getProject(@Header("token") String token);





    @GET ("/user/demo")
    Observable<String> demo ();
}
