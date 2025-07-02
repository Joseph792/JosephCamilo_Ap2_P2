package edu.ucne.josephcamilo_ap2_p2.data.remote

import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.RepositoryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{username}/repos")
    suspend fun listRepos(@Path("username") username: String): List<RepositoryDto>
}