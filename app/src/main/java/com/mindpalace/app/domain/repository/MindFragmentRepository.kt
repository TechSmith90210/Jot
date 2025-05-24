package com.mindpalace.app.domain.repository

import com.mindpalace.app.domain.model.MindFragment
import com.mindpalace.app.domain.model.MindFragmentSummary

interface MindFragmentRepository {
    //Create
    suspend fun createFragment(fragment: MindFragment): Result<Unit>

    //Read
    // for recently opened fragments
    suspend fun getFragmentsByLastOpened(userId: String, limit: Int = 6): Result<List<MindFragmentSummary>>

    //for recently created fragments
    suspend fun getFragmentsByCreatedAt(userId: String, limit: Int = 7): Result<List<MindFragmentSummary>>

    suspend fun getAllFragments(userId: String): Result<List<MindFragmentSummary>>

    //To get specific Fragment
    suspend fun getFragment(userId: String, fragmentId: String): Result<MindFragment>

    //Update
    suspend fun updateFragment(fragment: MindFragment): Result<Unit>

    //Delete
    suspend fun deleteFragment(userId: String, fragmentId: String): Result<Unit>
}