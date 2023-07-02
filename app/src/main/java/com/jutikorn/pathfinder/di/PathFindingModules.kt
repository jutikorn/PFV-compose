package com.jutikorn.pathfinder.di

import com.jutikorn.pathfinder.MainViewModel
import com.jutikorn.pathfinder.methods.AStarEuclidean
import com.jutikorn.pathfinder.methods.AStarManhattan
import com.jutikorn.pathfinder.methods.BFS
import com.jutikorn.pathfinder.methods.DFS
import com.jutikorn.pathfinder.methods.Dijkstra
import com.jutikorn.pathfinder.usecases.GenerateMatrixUseCase
import com.jutikorn.pathfinder.usecases.RenderUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.KoinApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun KoinApplication.applyModules() {
    allowOverride(true)
    modules(MainModules.allModules)
}

object MainModules {
    private val presentationModule = module {
        viewModelOf(::MainViewModel)
    }

    private val domainModule = module {

        singleOf(::BFS)
        singleOf(::Dijkstra)
        singleOf(::DFS)
        singleOf(::AStarEuclidean)
        singleOf(::AStarManhattan)

        singleOf(::GenerateMatrixUseCase)
        singleOf(::RenderUseCase)
    }

    val allModules = listOf(
        presentationModule,
        domainModule,
    )
}
