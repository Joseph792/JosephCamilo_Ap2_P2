package edu.ucne.josephcamilo_ap2_p2.data.remote

import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.RepositoryDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GitHubApi {
    @GET("/api/Viajes")
    suspend fun listRepos(@Path("username") username: String): List<RepositoryDto>
    @POST("/api/Viajes")
    suspend fun createRepo(@Path("username") username: String, @Body repo: RepositoryDto): RepositoryDto
    @PUT("/api/Viajes/{id}")
    suspend fun updateRepo(@Path("username") username: String, @Path("repoName") repoName: String, @Body repo: RepositoryDto): RepositoryDto
}