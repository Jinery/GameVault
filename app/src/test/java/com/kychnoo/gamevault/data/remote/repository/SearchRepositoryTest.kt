package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.data.local.dao.SearchHistoryDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchRepositoryTest {
    private val dao = mockk<SearchHistoryDao>()
    private val repository = SearchRepository(dao)

    @Test
    fun `addSearchQuery should delete existing and insert new entry`() = runTest {
        val query = "Hades"
        coEvery { dao.deleteByQuery(any()) } returns Unit
        coEvery { dao.insert(any()) } returns Unit
        coEvery { dao.trimHistory() } returns Unit

        repository.addSearchQuery(query)

        coVerify { dao.deleteByQuery(query) }
        coVerify { dao.insert(match { it.query == query }) }
        coVerify { dao.trimHistory() }
    }

    @Test
    fun `addSearchQuery should not do anything if query is blank`() = runTest {
        repository.addSearchQuery("   ")

        coVerify(exactly = 0) { dao.deleteByQuery(any()) }
        coVerify(exactly = 0) { dao.insert(any()) }
    }
}
