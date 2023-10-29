package fr.isep.mobiledev.androidrestapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroceryService {

    @POST("groceries/add")
    fun addGrocery(@Body grocery : Grocery) : Call<Long>

    @GET("groceries/{id}")
    fun getGrocery(@Path("id") id : Long) : Call<Grocery>

    @POST("groceries/update")
    fun updateGrocery(@Body grocery : Grocery) : Call<Void>

    @POST("groceries/remove")
    fun removeGrocery(@Body grocery : Grocery) : Call<Void>

    @GET("groceries/list")
    fun listGroceries() : Call<List<Grocery>>
}