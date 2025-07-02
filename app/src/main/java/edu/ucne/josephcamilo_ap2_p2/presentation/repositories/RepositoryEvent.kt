package edu.ucne.josephcamilo_ap2_p2.presentation.repositories

sealed interface RepositoryEvent{
    data object PostRepository: RepositoryEvent
    data object GetRepositories: RepositoryEvent
    data object PutRepositories: RepositoryEvent
    data object DeleteRepositories: RepositoryEvent
}