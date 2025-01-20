@file:OptIn(InternalAPI::class)

package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import data.model.AddCarPicture
import data.model.AddCarPictureBox
import data.model.Image
import data.model.PlatedVehicle
import domain.ktor.client
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.util.InternalAPI
import kotlinx.coroutines.launch
import ui.components.BottomSheetContent
import ui.components.LicensePlate
import ui.extensions.WidthFraction
import ui.extensions.formatDateFromString
import ui.extensions.hasMultiple
import ui.extensions.orElse
import ui.theme.SpacerS
import ui.theme.SpacerXS
import ui.theme.Spacing
import kotlin.random.Random

class LicensePlateDetailsScreen(private val platedVehicle: PlatedVehicle) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalBottomSheetNavigator.current
        val snackBarHostState = remember { SnackbarHostState() }

        BottomSheetContent(
            bottomSheetNavigator = navigator,
            showDragHandle = true,
            hasContentPadding = false,
            snackBarHostState = snackBarHostState,
            content = {
                VehicleDataList(
                    modifier = it,
                    platedVehicle = platedVehicle,
                    snackBarHostState = snackBarHostState
                )
            }
        )
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun VehicleDataList(
        modifier: Modifier = Modifier,
        platedVehicle: PlatedVehicle,
        snackBarHostState: SnackbarHostState
    ) {
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        suspend fun uploadImage(id: Int, bitmap: ByteArray) {
            val fileName = "${Random.nextInt()}.jpg"

            val formData = MultiPartFormDataContent(
                formData {
                    append(FormPart("bitmapName", "image.jpg"))
                    append(
                        key = "image",
                        value = bitmap,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Image.JPEG)
                            append(
                                HttpHeaders.ContentDisposition,
                                "form-data; name=\"image\"; filename=\"$fileName\""
                            )
                        }
                    )
                }
            )

            val response: HttpResponse = client.post("gekentekende-voertuigen/$id/upload-image") {
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                    append(HttpHeaders.ContentType, ContentType.MultiPart.FormData)
                }
                setBody(formData)
            }

            when (response.status.value) {
                200, 201, 202, 203, 204, 205, 206, 207 -> {
                    snackBarHostState.showSnackbar("The image is successfully uploaded, it will be available after validation.")
                }

                else -> {
                    snackBarHostState.showSnackbar("Something went wrong...")
                }
            }
        }

        val singleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = scope,
            onResult = { byteArrays ->
                byteArrays.firstOrNull()?.let { byteArray ->

                    scope.launch {
                        try {
                            uploadImage(platedVehicle.id, byteArray)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            snackBarHostState.showSnackbar("Something went wrong...")
                        }
                    }
                }
            }
        )

        Column(
            modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp)
        ) {
            if (platedVehicle.images?.isNotEmpty() == true) {
                VehicleImageCarousel(
                    images = platedVehicle.images,
                    contentDescription = platedVehicle.handelsbenaming,
                    onAddCarPictureClick = {
                        singleImagePicker.launch()
                    }
                )
            } else {
                AddCarPicture {
                    singleImagePicker.launch()
                }
            }

            Column(
                Modifier.padding(horizontal = Spacing.Medium.dp, vertical = Spacing.Small.dp),
                verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LicensePlate(
                        modifier = Modifier,
                        widthFraction = WidthFraction.LARGE,
                        text = PlatedVehicle.getFormattedLicensePlate(platedVehicle.kenteken)
                    )
                }

                Divider()

                VehicleDataRow(title = "Merk", value = platedVehicle.merk.orEmpty())

                VehicleDataRow(
                    title = "Handelsbenaming",
                    value = platedVehicle.handelsbenaming.orEmpty()
                )

                VehicleDataRow(
                    title = "Voertuig soort",
                    value = platedVehicle.voertuigsoort.orEmpty()
                )

                // GRID
                platedVehicle.emissie_gegevens?.forEach { emissionData ->
                    Text(
                        text = "${emissionData.brandstof_omschrijving} aandrijving",
                        color = MaterialTheme.colors.secondaryVariant,
                        fontWeight = FontWeight.W700
                    )

                    SpacerS()

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Small.dp),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp),
                        maxItemsInEachRow = 2
                    ) {
                        emissionData.getVermogen()?.let {
                            VehicleDataRectangle(
                                Modifier.weight(1f),
                                title = "Vermogen",
                                value = it.first
                            )

                            VehicleDataRectangle(
                                Modifier.weight(1f),
                                title = "Vermogen",
                                value = it.second
                            )
                        }

                        VehicleDataRectangle(
                            Modifier.weight(1f),
                            title = "Uitlaat emissie niveau",
                            value = emissionData.uitlaatemissieniveau
                        )

                        emissionData.geluidsniveau_stationair?.let {
                            VehicleDataRectangle(
                                Modifier.weight(1f),
                                title = "Geluidsniveau stationair",
                                value = "$it dB"
                            )
                        }

                        emissionData.brandstofverbruik_stad?.let {
                            VehicleDataRectangle(
                                Modifier.weight(1f),
                                title = "Brandstof verbruik in de stad",
                                value = "$it L/100km"
                            )
                        }

                        emissionData.brandstofverbruik_buiten_de_stad?.let {
                            VehicleDataRectangle(
                                Modifier.weight(1f),
                                title = "Brandstof verbruik buiten de stad",
                                value = "$it L/100km"
                            )
                        }

                        emissionData.brandstofverbruik_gecombineerd?.let {
                            VehicleDataRectangle(
                                Modifier.weight(1f),
                                title = "Brandstof verbruik gecombineerd",
                                value = "$it L/100km"
                            )
                        }
                    }
                }

                VehicleDataRow(
                    title = "Datum tenaamstelling",
                    value = platedVehicle.datum_tenaamstelling?.toString()?.formatDateFromString()
                )

                VehicleDataRow(
                    title = "Catalogus prijs",
                    value = "€ ${platedVehicle.catalogusprijs?.toInt()}"
                )

                SpacerS()

                Text(
                    text = "Gewichten",
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.W700
                )

                SpacerS()

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small.dp),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp),
                    maxItemsInEachRow = 2
                ) {
                    val laadvermogen: Double? = platedVehicle.laadvermogen
                        ?: run {
                            val massaLedigVoertuig = platedVehicle.massa_ledig_voertuig
                            val toegestaneMaximumMassaVoertuig =
                                platedVehicle.toegestane_maximum_massa_voertuig

                            if (massaLedigVoertuig != null && toegestaneMaximumMassaVoertuig != null) {
                                toegestaneMaximumMassaVoertuig - massaLedigVoertuig
                            } else {
                                null
                            }
                        }

                    VehicleDataRectangle(
                        modifier = Modifier.weight(1f),
                        title = "Maximale laadvermogen (Kg)",
                        value = laadvermogen?.let { "${it.toInt()} Kg" }
                    )

                    VehicleDataRectangle(
                        modifier = Modifier.weight(1f),
                        title = "Vermogen Massarijklaar",
                        value = "${platedVehicle.vermogen_massarijklaar}"
                    )

                    VehicleDataRectangle(
                        modifier = Modifier.weight(1f),
                        title = "Toegestane trek massa ongeremd",
                        value = "${platedVehicle.maximum_massa_trekken_ongeremd?.toInt()} Kg"
                    )

                    VehicleDataRectangle(
                        modifier = Modifier.weight(1f),
                        title = "Toegestane trek massa geremd",
                        value = "${platedVehicle.maximum_trekken_massa_geremd?.toInt()} Kg"
                    )
                }

                platedVehicle.lengte?.let {
                    VehicleDataRow(
                        title = "Lengte",
                        value = "${it.toInt()} cm"
                    )
                }

                platedVehicle.breedte?.let {
                    VehicleDataRow(
                        title = "Breedte",
                        value = "${it.toInt()} cm"
                    )
                }

                platedVehicle.hoogte_voertuig?.let {
                    VehicleDataRow(
                        title = "Hoogte",
                        value = "${it.toInt()} cm"
                    )
                }

                platedVehicle.bruto_bpm?.let {
                    VehicleDataRow(
                        title = "Bruto BPM",
                        value = "€ ${it.toInt()}"
                    )
                }

                VehicleDataRow(
                    title = "APK Datum",
                    value = platedVehicle.vervaldatum_apk?.toString()?.formatDateFromString()
                )

                VehicleDataRow(
                    title = "Cilinder inhoud (cc)",
                    value = "${platedVehicle.cilinderinhoud?.toInt().toString()} cc"
                )

                VehicleDataRow(
                    title = "Kleur",
                    value = platedVehicle.eerste_kleur
                )

                VehicleDataRow(
                    title = "Tellerstand Oordeel",
                    value = platedVehicle.tellerstandoordeel
                )

                VehicleDataRow(
                    title = "WOK Status",
                    value = platedVehicle.wacht_op_keuren
                )

                VehicleDataRow(
                    title = "Zuinigheid Classificatie",
                    value = platedVehicle.zuinigheidsclassificatie
                )
            }
        }
    }

    @Composable
    fun VehicleDataRectangle(modifier: Modifier = Modifier, title: String, value: String?) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.medium)
                .border(1.dp, Color.LightGray.copy(alpha = 0.2f), MaterialTheme.shapes.medium)
                .background(Color.LightGray.copy(alpha = 0.1f))
                .padding(Spacing.Medium.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Center
            )

            SpacerXS()

            Text(text = value.orElse("N/A"), textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun VehicleImage(
        modifier: Modifier = Modifier,
        image: Image,
        contentDescription: String? = null,
        onClick: () -> Unit
    ) {
        KamelImage(
            modifier = modifier
                .aspectRatio(1.6f)
                .fillMaxWidth()
                .padding(Spacing.Small.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = onClick),
            resource = asyncPainterResource(data = image.getFilePath()),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth
        )
    }

    @Composable
    fun VehicleDataRow(modifier: Modifier = Modifier, title: String, value: String?) {
        Column(modifier = modifier) {
            Spacer(Modifier.height(Spacing.Small.dp))
            Text(title, color = MaterialTheme.colors.secondaryVariant, fontWeight = FontWeight.W600)
            Text(value.orElse("N/A"))
            Spacer(Modifier.height(Spacing.Small.dp))
            Divider()
        }
    }

    @Composable
    fun VehicleImageCarousel(
        images: List<Image>,
        contentDescription: String? = null,
        onAddCarPictureClick: () -> Unit
    ) {
        suspend fun scrollToNextImage(image: Image, index: Int, lazyListState: LazyListState) {
            if (images.last() == image) {
                lazyListState.animateScrollToItem(0)
            } else {
                lazyListState.animateScrollToItem(index.inc())
            }
        }

        val state = rememberLazyListState()
        val scope = rememberCoroutineScope()

        LazyRow(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f),
            state = state,
        ) {
            itemsIndexed(images) { index, image ->
                VehicleImage(
                    modifier = Modifier,
                    image = image,
                    contentDescription = contentDescription
                ) {
                    images.hasMultiple {
                        scope.launch {
                            scrollToNextImage(image, index, state)
                        }
                    }
                }
            }

            item {
                AddCarPictureBox(onAddCarPictureClick)
            }
        }
    }
}