package com.androiddevs.mvvmnewsapp.ui.util

 sealed class Resource<T> (
     val data : T? = null,
     val message : String? = null
         ) {

     class Succsess<T> (data: T) : Resource<T>(data)
     class Erorr<T>(message: String,data: T? = null) : Resource<T>(data,message)
     class Loading<T> : Resource<T>()
 }