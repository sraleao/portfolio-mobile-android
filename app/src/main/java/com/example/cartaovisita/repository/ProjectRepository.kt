package com.example.cartaovisita.repository

import com.example.cartaovisita.data.local.ProjectDao
import com.example.cartaovisita.data.local.ProjectEntity
import com.example.cartaovisita.data.remote.GithubApiService
import com.example.cartaovisita.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProjectRepository(
    private val dao: ProjectDao,
    private val api: GithubApiService,
    private val githubUser: String
) {

    fun getProjectsFlow(): Flow<List<Project>> =
        dao.getAllProjects().map { list ->
            list.map {
                Project(it.id, it.name, it.description)
            }
        }

    fun getProjectByIdFlow(id: Int): Flow<Project?> =
        dao.getProjectById(id).map { entity ->
            entity?.let { Project(it.id, it.name, it.description) }
        }

    // Sincroniza com a API do GitHub
    suspend fun syncFromNetwork(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val repos = api.listRepos(githubUser)
            val entities = repos.map {
                ProjectEntity(it.id, it.name, it.description)
            }
            dao.insertAll(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
