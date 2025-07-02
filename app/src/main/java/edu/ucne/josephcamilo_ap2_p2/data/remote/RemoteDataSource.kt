package edu.ucne.josephcamilo_ap2_p2.data.remote

import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.RepositoryDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val gitHubApi: GitHubApi
) {
    suspend fun getRepository(username: String) = gitHubApi.listRepos(username)
}