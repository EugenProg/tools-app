package kz.just_code.toolsapp

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.UUID

object NameRepository {

    private val totalPages = 10

    suspend fun getNames(page: Int): NameResponse {
        Timber.d("Current page: $page")
        delay(2000)
        return NameResponse(
            name = get20Names(page),
            meta = Meta(page, totalPages)
        )
    }

    private fun get20Names(page: Int): List<String> {
        val list: MutableList<String> = mutableListOf()
        for (name in 0..4) {
            list.add("$page ${UUID.randomUUID()}")
        }
        return list
    }

    var names = Pager(
        config = PagingConfig(5, enablePlaceholders = true, initialLoadSize = 1),
        pagingSourceFactory = { NameSource() }
    ).flow
}

class NameSource() : PagingSource<Int, String>() {
    override fun getRefreshKey(state: PagingState<Int, String>): Int? = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val response = NameRepository.getNames(params.key ?: 0)
            val nextPage = if ((response.meta?.currentPage ?: 0) < (response.meta?.lastPage ?: 0)) {
                (response.meta?.currentPage ?: 0) + 1
            } else null
            LoadResult.Page(
                data = response.name ?: listOf(),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


data class NameResponse(
    val name: List<String>? = null,
    val meta: Meta? = null
)

data class Meta(
    val currentPage: Int,
    val lastPage: Int
)