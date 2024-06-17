package data.model

import kotlinx.serialization.Serializable

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
)