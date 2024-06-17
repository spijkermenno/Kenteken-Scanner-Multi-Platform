package data.model

import kotlinx.serialization.Serializable

@Serializable
data class CarBodyData(
    val id: Int,
    val gekentekende_voertuig_id: Int,
    val carrosserie_volgnummer: String,
    val carrosserietype: String,
    val type_carrosserie_europese_omschrijving: String
)