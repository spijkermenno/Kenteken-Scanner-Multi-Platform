package data.model

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import ui.theme.Spacing

@Serializable
data class PlatedVehicle(
    val id: Int,
    val kenteken: String,
    val voertuigsoort: String?,
    val merk: String?,
    val handelsbenaming: String?,
    val vervaldatum_apk: Int?,
    val datum_tenaamstelling: Int?,
    val bruto_bpm: Double?,
    val inrichting: String?,
    val aantal_zitplaatsen: Int?,
    val eerste_kleur: String?,
    val tweede_kleur: String?,
    val aantal_cilinders: Int?,
    val cilinderinhoud: Double?,
    val massa_ledig_voertuig: Double?,
    val toegestane_maximum_massa_voertuig: Double?,
    val massa_rijklaar: Double?,
    val maximum_massa_trekken_ongeremd: Double?,
    val maximum_trekken_massa_geremd: Double?,
    val datum_eerste_toelating: Int?,
    val datum_eerste_tenaamstelling_in_nederland: Int?,
    val wacht_op_keuren: String?,
    val catalogusprijs: Double?,
    val wam_verzekerd: String?,
    val maximum_constructiesnelheid: Double?,
    val laadvermogen: Double?,
    val oplegger_geremd: Double?,
    val aanhangwagen_autonoom_geremd: Double?,
    val aanhangwagen_middenas_geremd: Int?,
    val aantal_staanplaatsen: Int?,
    val aantal_deuren: Int?,
    val aantal_wielen: Int?,
    val afwijkende_maximum_snelheid: Double?,
    val lengte: Double?,
    val breedte: Double?,
    val plaats_chassisnummer: String?,
    val technische_max_massa_voertuig: Double?,
    val type: String?,
    val variant: String?,
    val uitvoering: String?,
    val vermogen_massarijklaar: Double?,
    val wielbasis: Double?,
    val export_indicator: String?,
    val openstaande_terugroepactie_indicator: String?,
    val vervaldatum_tachograaf: Int?,
    val taxi_indicator: String?,
    val maximum_massa_samenstelling: Double?,
    val aantal_rolstoelplaatsen: Int?,
    val maximum_ondersteunende_snelheid: Double?,
    val jaar_laatste_registratie_tellerstand: Int?,
    val tellerstandoordeel: String?,
    val code_toelichting_tellerstandoordeel: String?,
    val tenaamstellen_mogelijk: String?,
    val wielbasis_voertuig_minimum: Double?,
    val wielbasis_voertuig_maximum: Double?,
    val lengte_voertuig_minimum: Double?,
    val lengte_voertuig_maximum: Double?,
    val breedte_voertuig_minimum: Double?,
    val breedte_voertuig_maximum: Double?,
    val hoogte_voertuig: Double?,
    val hoogte_voertuig_minimum: Double?,
    val hoogte_voertuig_maximum: Double?,
    val massa_bedrijfsklaar_minimaal: Double?,
    val massa_bedrijfsklaar_maximaal: Double?,
    val technisch_toelaatbaar_massa_koppelpunt: Double?,
    val maximum_massa_technisch_maximaal: Double?,
    val maximum_massa_technisch_minimaal: Double?,
    val subcategorie_nederland: String?,
    val verticale_belasting_koppelpunt_getrokken_voertuig: Double?,
    val zuinigheidsclassificatie: String?,
    val registratie_datum_goedkeuring_afschrijvingsmoment_bpm: Int?,
    val gemiddelde_lading_waarde: Double?,
    val aerodynamische_voorziening_of_uitrusting: String?,
    val additionele_massa_alternatieve_aandrijving: Double?,
    val verlengde_cabine_indicator: String?,
    val carrosserie_gegevens: List<CarBodyData>?,
    val emissie_gegevens: List<EmissionData>?,
    val images: List<Image>?
) {
    companion object {
        fun getFormattedLicensePlate(unformattedLicensePlate: String): String {
            val formattedLicensePlate = unformattedLicensePlate.uppercase()

            val licensePlateSideCode = this.getSideCode(formattedLicensePlate)

            return when (licensePlateSideCode) {
                1, 2, 3, 4, 5 -> "${formattedLicensePlate.substring(0, 2)}-${
                    formattedLicensePlate.substring(
                        2,
                        4
                    )
                }-${formattedLicensePlate.substring(4)}"

                6 -> "${formattedLicensePlate.substring(0, 2)}-${
                    formattedLicensePlate.substring(
                        2,
                        5
                    )
                }-${formattedLicensePlate.substring(5)}"

                7, 9 -> "${formattedLicensePlate.substring(0, 1)}-${
                    formattedLicensePlate.substring(
                        1,
                        4
                    )
                }-${formattedLicensePlate.substring(4)}"

                8 -> "${formattedLicensePlate.substring(0, 2)}-${
                    formattedLicensePlate.substring(
                        2,
                        5
                    )
                }-${formattedLicensePlate.substring(5)}"

                11, 12 -> "${
                    formattedLicensePlate.substring(
                        0,
                        1
                    )
                }-${formattedLicensePlate.substring(1, 3)}-${
                    formattedLicensePlate.substring(
                        3
                    )
                }"

                10, 13 -> "${
                    formattedLicensePlate.substring(
                        0,
                        3
                    )
                }-${formattedLicensePlate.substring(3, 5)}-${
                    formattedLicensePlate.substring(
                        5
                    )
                }"

                else -> formattedLicensePlate
            }.uppercase()
        }

        fun getSideCode(licensePlate: String): Int {
            val patterns = mapOf(
                0 to "^[a-zA-Z]{2}[0-9]{2}[0-9]{2}$",       // 1 XX-99-99
                1 to "^[0-9]{2}[0-9]{2}[a-zA-Z]{2}$",       // 2 99-99-XX
                2 to "^[0-9]{2}[a-zA-Z]{2}[0-9]{2}$",       // 3 99-XX-99
                3 to "^[a-zA-Z]{2}[0-9]{2}[a-zA-Z]{2}$",    // 4 XX-99-XX
                4 to "^[a-zA-Z]{2}[a-zA-Z]{2}[0-9]{2}$",    // 5 XX-XX-99
                5 to "^[0-9]{2}[a-zA-Z]{2}[a-zA-Z]{2}$",    // 6 99-XX-XX
                6 to "^[0-9]{2}[a-zA-Z]{3}[0-9]{1}$",       // 7 99-XXX-9
                7 to "^[0-9]{1}[a-zA-Z]{3}[0-9]{2}$",       // 8 9-XXX-99
                8 to "^[a-zA-Z]{2}[0-9]{3}[a-zA-Z]{1}$",    // 9 XX-999-X
                9 to "^[a-zA-Z]{1}[0-9]{3}[a-zA-Z]{2}$",    // 10 X-999-XX
                10 to "^[a-zA-Z]{3}[0-9]{2}[a-zA-Z]{1}$",   // 11 XXX-99-X
                11 to "^[a-zA-Z]{1}[0-9]{2}[a-zA-Z]{3}$",   // 12 X-99-XXX
                12 to "^[0-9]{1}[a-zA-Z]{2}[0-9]{3}$",      // 13 9-XX-999
                13 to "^[0-9]{3}[a-zA-Z]{2}[0-9]{1}$"       // 14 999-XX-9
            )

            return patterns.entries.firstOrNull {
                Regex(it.value).matches(
                    licensePlate.replace(
                        "-",
                        ""
                    )
                )
            }?.key
                ?: -2
        }
    }
}

@Composable
fun AddCarPicture(
    onAddPictureClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(all = Spacing.Medium.dp)
            .aspectRatio(2f)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(Color.LightGray)
            .clickable(onClick = onAddPictureClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            contentDescription = "Add Car Picture",
            tint = Color.White,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun AddCarPictureBox(
    onAddPictureClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(all = Spacing.Small.dp)
            .fillMaxHeight()
            .clip(MaterialTheme.shapes.large)
            .background(Color.LightGray)
            .padding(all = Spacing.Medium.dp)
            .clickable(onClick = onAddPictureClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            contentDescription = "Add Car Picture",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}