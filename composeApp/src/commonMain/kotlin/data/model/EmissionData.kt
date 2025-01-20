package data.model

import kotlinx.serialization.Serializable
import ui.extensions.orElse

@Serializable
data class EmissionData(
    val id: Int,
    val gekentekende_voertuig_id: Int,
    val brandstof_omschrijving: String,
    val brandstofverbruik_buiten_de_stad: String?,
    val brandstofverbruik_gecombineerd: String?,
    val brandstofverbruik_stad: String?,
    val co2_uitstoot_gecombineerd: String?,
    val co2_uitstoot_gewogen: String?,
    val geluidsniveau_rijdend: String?,
    val geluidsniveau_stationair: String?,
    val emissieklasse: String?,
    val milieuklasse_eg_goedkeuring_licht: String?,
    val milieuklasse_eg_goedkeuring_zwaar: String?,
    val uitstoot_deeltjes_licht: String?,
    val uitstoot_deeltjes_zwaar: String?,
    val nettomaximumvermogen: String?,
    val nominaal_continu_maximumvermogen: String?,
    val roetuitstoot: String?,
    val toerental_geluidsniveau: String?,
    val emissie_deeltjes_type1_wltp: Double?,
    val emissie_co2_gecombineerd_wltp: Double?,
    val emissie_co2_gewogen_gecombineerd_wltp: Double?,
    val brandstof_verbruik_gecombineerd_wltp: Double?,
    val brandstof_verbruik_gewogen_gecombineerd_wltp: Double?,
    val elektrisch_verbruik_enkel_elektrisch_wltp: Double?,
    val actie_radius_enkel_elektrisch_wltp: Double?,
    val actie_radius_enkel_elektrisch_stad_wltp: Double?,
    val elektrisch_verbruik_extern_opladen_wltp: Double?,
    val actie_radius_extern_opladen_wltp: Double?,
    val actie_radius_extern_opladen_stad_wltp: Double?,
    val max_vermogen_15_minuten: Double?,
    val max_vermogen_60_minuten: Double?,
    val netto_max_vermogen_elektrisch: Double?,
    val klasse_hybride_elektrisch_voertuig: String?,
    val opgegeven_maximum_snelheid: Double?,
    val uitlaatemissieniveau: String?
) {
    fun getVermogen(): Pair<String, String>? {
        var vermogenFloat: Float = 0f

        this.nettomaximumvermogen?.let { nettomaximumvermogen ->
            try {
                vermogenFloat = nettomaximumvermogen.toFloat()
            } catch (e: Exception) {
                Unit
            }
        }.orElse {
            vermogenFloat = this.netto_max_vermogen_elektrisch?.toFloat() ?: 0f
        }

        val vermogenKW = vermogenFloat
        val vermogenPK = vermogenKW * 1.35962

        var vermogenKWString: String
        var vermogenPKString: String

        if (vermogenFloat > 0f) {
            vermogenKWString = vermogenKW.toInt().toString()
            vermogenKWString += " kW"

            vermogenPKString = vermogenPK.toInt().toString()
            vermogenPKString += " pk"

            return Pair(vermogenKWString, vermogenPKString)
        }

        return null
    }
}