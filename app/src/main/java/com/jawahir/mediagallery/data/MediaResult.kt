package com.jawahir.mediagallery.data


sealed class MediaResult<T> (val data:T? = null,val  error:Throwable?=null){
    class Loading<T>(data:T? = null): MediaResult<T>(data)
    class Success<T>(data:T): MediaResult<T>(data)
    class Error<T>(data:T? = null, error: Throwable): MediaResult<T>(data,error)
}