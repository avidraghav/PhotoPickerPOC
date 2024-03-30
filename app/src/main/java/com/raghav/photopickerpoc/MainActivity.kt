package com.raghav.photopickerpoc

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.raghav.photopickerpoc.ui.theme.PhotoPickerPOCTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val MAX_PHOTOS_SELECTABLE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhotoPickerPOCTheme {
                var singleImageUri by remember {
                    mutableStateOf<Uri?>(null)
                }
                var multipleImageUris by remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }

                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri -> singleImageUri = uri },
                )
                val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_PHOTOS_SELECTABLE),
                    onResult = { uris -> multipleImageUris = uris },
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.SpaceAround,
                        ) {
                            Button(onClick = {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                )
                            }) {
                                Text(text = "Select Single Image")
                            }
                            Button(onClick = {
                                multiplePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                )
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
//                                        Build.VERSION_CODES.R,
//                                    ) >= 2
//                                ) {
//                                    if (MAX_PHOTOS_SELECTABLE <= MediaStore.getPickImagesMaxLimit()) {
//                                        multiplePhotoPickerLauncher.launch(
//                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
//                                        )
//                                    }
//                                }
//                                else {
//                                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                                        type = "image"
//                                        putExtra(
//                                            Intent.EXTRA_MIME_TYPES,
//                                            arrayOf("image/*"),
//                                        )
//                                    }
//                                }
                            }) {
                                Text(text = "Select $MAX_PHOTOS_SELECTABLE Images")
                            }
                        }
                    }
                    item {
                        val iconSize = 24.dp
                        val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }
                        singleImageUri?.let {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = singleImageUri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                IconButton(
                                    onClick = {
                                        singleImageUri = null
                                    },
                                    modifier = Modifier
                                        .offset {
                                            IntOffset(x = +offsetInPx, y = -offsetInPx)
                                        }
                                        .clip(CircleShape)
                                        .background(White)
                                        .size(iconSize)
                                        .align(Alignment.TopEnd),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "",
                                    )
                                }
                            }
                        }
                    }
                    items(multipleImageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
