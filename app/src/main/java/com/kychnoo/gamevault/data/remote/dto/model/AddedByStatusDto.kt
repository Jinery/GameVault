package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.addedBy.AddedByStatusData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class AddedByStatusDto(
    val yet: Int? = null,
    val owned: Int? = null,
    val beaten: Int? = null,
    val toplay: Int? = null,
    val dropped: Int? = null,
    val playing: Int? = null
) : DtoMapper<AddedByStatusData> {
    override fun toData(): AddedByStatusData = AddedByStatusData(
        yet = this.yet,
        owned = this.owned,
        beaten = this.beaten,
        toplay = this.toplay,
        dropped = this.dropped,
        playing = this.playing
    )

}
