@file:OptIn(InternalAPI::class)

package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
import ui.theme.Spacing
import kotlin.random.Random

class LicensePlateDetailsScreen(private val platedVehicle: PlatedVehicle) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalBottomSheetNavigator.current

        BottomSheetContent(
            bottomSheetNavigator = navigator,
            showDragHandle = true,
            hasContentPadding = false,
            content = {
                VehicleDataList(modifier = it, platedVehicle = platedVehicle)
            }
        )
    }
}

@Composable
fun VehicleDataList(modifier: Modifier = Modifier, platedVehicle: PlatedVehicle) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val showImageError = remember { mutableStateOf(false) }
    val showImageSuccess = remember { mutableStateOf(false) }

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
                // Add any additional form fields here if necessary
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
                // Handle successful response
                showImageSuccess.value = true
                showImageError.value = false
            }

            else -> {
                // Handle error response
                showImageError.value = true
                showImageSuccess.value = false
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
                        showImageError.value = true
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

        if (showImageError.value) {
            Box(
                modifier = Modifier
                    .padding(Spacing.Medium.dp)
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .background(Color.Red.copy(alpha = 0.2f))
                    .padding(Spacing.Large.dp)
                    .clickable { showImageError.value = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Something went wrong...",
                    color = Color.White,
                )
            }
        } else if (showImageSuccess.value) {
            Box(
                modifier = Modifier
                    .padding(Spacing.Medium.dp)
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(Spacing.Large.dp)
                    .clickable { showImageSuccess.value = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "The image is succesfully uploaded, it will be available after validation.",
                    color = Color.Black,
                )
            }
        } else {
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
                    // TODO add functionality to upload image
                    singleImagePicker.launch()
                }
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

            VehicleData(title = "Merk", value = platedVehicle.merk.orEmpty())

            VehicleData(
                title = "Handelsbenaming",
                value = platedVehicle.handelsbenaming.orEmpty()
            )

            VehicleData(
                title = "APK Datum",
                value = platedVehicle.vervaldatum_apk?.toString()?.formatDateFromString()
            )

            VehicleData(
                title = "Cilinder inhoud (cc)",
                value = platedVehicle.cilinderinhoud?.toInt().toString()
            )

            VehicleData(
                title = "Kleur",
                value = platedVehicle.eerste_kleur
            )

            VehicleData(
                title = "Tellerstand Oordeel",
                value = platedVehicle.tellerstandoordeel
            )

            VehicleData(
                title = "WOK Status",
                value = platedVehicle.wacht_op_keuren
            )

            VehicleData(
                title = "Zuinigheid Classificatie",
                value = platedVehicle.zuinigheidsclassificatie
            )
        }
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
fun VehicleData(modifier: Modifier = Modifier, title: String, value: String?) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(Spacing.Small.dp))
        Text(title, color = MaterialTheme.colors.secondary, fontWeight = FontWeight.W600)
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