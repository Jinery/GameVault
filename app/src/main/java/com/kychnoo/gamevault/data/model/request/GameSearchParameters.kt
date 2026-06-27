package com.kychnoo.gamevault.data.model.request

data class GameSearchParameters(
    val search: String,
    val page: Int = 1,
    val pageSize: Int = 20,
    val searchPrecise: Boolean? = null,
    val searchExact: Boolean? = null,
    val parentPlatforms: String? = null,
    val platforms: String? = null,
    val stores: String? = null,
    val developers: String? = null,
    val publishers: String? = null,
    val genres: String? = null,
    val tags: String? = null,
    val dates: String? = null,
    val ordering: String? = null,
    val excludeAdditions: Boolean? = null
) {
    fun toQueryMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        map["search"] = search
        map["page"] = page.toString()
        map["page_size"] = pageSize.toString()

        if (searchPrecise != null) map["search_precise"] = searchPrecise.toString()
        if (searchExact != null) map["search_exact"] = searchExact.toString()
        if (parentPlatforms != null) map["parent_platforms"] = parentPlatforms
        if (platforms != null) map["platforms"] = platforms
        if (stores != null) map["stores"] = stores
        if (developers != null) map["developers"] = developers
        if (publishers != null) map["publishers"] = publishers
        if (genres != null) map["genres"] = genres
        if (tags != null) map["tags"] = tags
        if (dates != null) map["dates"] = dates
        if (ordering != null) map["ordering"] = ordering
        if (excludeAdditions != null) map["exclude_additions"] = excludeAdditions.toString()

        return map
    }
}
