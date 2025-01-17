package com.oudombun.movieminiapp.data

import com.oudombun.movieminiapp.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
){
    val remote = remoteDataSource
}