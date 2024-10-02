package com.example.clasificadorobjetos

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.leanback.widget.Row
import com.example.clasificadorobjetos.ui.theme.ClasificadorObjetosTheme
import com.example.clasificadorobjetos.ui.theme.fontFamily1
import com.example.clasificadorobjetos.ui.theme.fontFamily2
import com.example.clasificadorobjetos.ui.theme.fontFamily3
import com.example.clasificadorobjetos.ui.theme.fontFamily4
import com.example.clasificadorobjetos.ui.theme.foutFamily
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClasificadorObjetosTheme {
                PantallaPrincipal()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {

    // Estado para controlar si el drawer está abierto o cerrado
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showTutorial by remember { mutableStateOf(false) }  // Estado para el tutorial


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(onInfoClick = { showTutorial = true })
        }
    ) {
        Scaffold(

            topBar = { TopBar { scope.launch { drawerState.open() } } },


            ) { innerPadding ->

            // Usamos Box para superponer el contenido y el tutorial
            Box(modifier = Modifier.fillMaxSize()) {

                // Contenido principal
                ScrollContent(innerPadding)

                // Muestra el tutorial cuando showTutorial es true
                if (showTutorial) {
                    // Asegurarse de que TutorialScreen llene toda la pantalla
                    TutorialScreen(
                        onBackClick = { showTutorial = false },
                        modifier = Modifier.fillMaxSize()  // Este es el modificador importante
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFD700) // Fondo amarillo
        ),
        title = {
            Text(text = "Clasificador de Objetos", color = Color.Black, fontSize = 22.sp, fontFamily = foutFamily)
        },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(
                    imageVector = Icons.Filled.Menu ,
                    contentDescription = "Abrir Menu",
                    tint = Color.Black
                    )
            }
        }
    )
}

@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    // Estado para la imagen y el texto
    var imageRes by remember { mutableStateOf(R.drawable.img_3) } //Imagen Inicial
    //var text by remember { mutableStateOf(" ") }         //Texto Inicial

    var imageUri by remember { mutableStateOf<Uri?>(null) } // Imagen inicial como URI
    var text by remember { mutableStateOf(" ") } // Texto inicial

    // Guardamos los valores iniciales para poder restablecerlos
    val initialImageRes = R.drawable.img_3
    val initialText = " "

    val context = LocalContext.current
    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            // Convertimos el bitmap a un URI temporal si es necesario o manejamos el bitmap directamente
            imageUri = bitmap?.let { saveBitmapToUri(context, it) }
        }
    )

    Column(
        // Modificadores de estilo de la columna
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding) // Aplica el padding que viene del Scaffold
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        ImageSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Esto asegura que ocupe la mitad de la pantalla verticalmente
            imageUri = imageUri
        )
        PredictionSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Esto asegura que ocupe la otra mitad de la pantalla verticalmente
            text = text, // Pasamos el estado del texto
            onClassifyClick = {
                //imageRes = R.drawable.se // Cambia la imagen al hacer clic
                text = "CPU" // Cambia el texto al hacer clic
            },
            onClearClick = {
                //imageRes = initialImageRes // Restablece la imagen inicial
                imageUri = null // Restablece la imagen a null
                text = initialText // Restablece el texto inicial
            },
            onGalleryClick = {
                launcherGallery.launch("image/*")// Abre la galería

            },
            onCameraClick = {
                launcherCamera.launch() // Abre la cámara
            }

        )

    }
}

// Función auxiliar para guardar el bitmap en un URI temporal
fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return file.toUri()
}

@Composable
fun DrawerContent(onInfoClick: () -> Unit) {

    // Obtener el ancho de la pantalla
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Estado de los switches
    var isSwitch1Checked by remember { mutableStateOf(false) }
    var isSwitch2Checked by remember { mutableStateOf(false) }


    // Menú con 3 opciones en la parte superior y 1 botón en la parte inferior
    Column(
        modifier = Modifier

            .width(screenWidth * 3 / 4) // Aquí hacemos que el drawer ocupe 3/4 del ancho
            .fillMaxHeight() // Ocupa toda la altura
            .background(Color(0x80FFFF00))
            .clip(RoundedCornerShape(10.dp))
            .padding(16.dp),

        verticalArrangement = Arrangement.SpaceBetween // Para separar las opciones superiores del botón inferior
    ) {
        // Opciones en la parte superior
        Column {
            Row{
                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = {
                    onInfoClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Info ,
                        contentDescription = "Abrir Menu",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Como Usar",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp),
                    fontFamily = fontFamily1
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ){
                Column {
                    Text(
                        text = "Usar tema del",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp),
                        fontFamily = fontFamily1
                    )
                    Text(
                        text = "sistema",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp),
                        fontFamily = fontFamily1
                    )
                }

                Switch(
                    checked = isSwitch1Checked,
                    onCheckedChange = { isSwitch1Checked = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black, // Cambia el color del thumb cuando está activado
                        uncheckedThumbColor = Color.White, // Cambia el color del thumb cuando está desactivado
                        checkedTrackColor = Color.White, // Cambia el color de la pista cuando está activado
                        uncheckedTrackColor = Color.Black // Cambia el color de la pista cuando está desactivado
                    )
                )


            }

            Row{
                Text(
                    text = "Modo oscuro",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(8.dp),
                    fontFamily = fontFamily1
                )

                Switch(
                    checked = isSwitch2Checked,
                    onCheckedChange = { if (!isSwitch1Checked) isSwitch2Checked = it }, // Solo se activa si el switch 1 está desactivado
                    enabled = !isSwitch1Checked, // Deshabilita el switch si el switch 1 está activo
                    colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black, // Cambia el color del thumb cuando está activado
                    uncheckedThumbColor = Color.White, // Cambia el color del thumb cuando está desactivado
                    checkedTrackColor = Color.White, // Cambia el color de la pista cuando está activado
                    uncheckedTrackColor = Color.Black // Cambia el color de la pista cuando está desactivado
                )
                )
            }


        }

        // Botón en la parte inferior
        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(
                "Salir",
                color = Color.White,
                fontFamily = fontFamily2,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun TutorialScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Gray)  // Solo para visualizar
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            // Contenido de la imagen
            val currentIndex = remember { mutableStateOf(0) }  // Estado para la imagen actual

            Spacer(modifier = Modifier.height(36.dp))

            // Imagen grande en la parte superior
            Image(
                painter = painterResource(id = getTutorialImageRes(currentIndex.value)),
                contentDescription = "Instructivo Imagen ${currentIndex.value}",
                modifier = Modifier
                    .weight(1f)  // Toma el espacio disponible
                    .fillMaxWidth()  // Asegúrate de que ocupe todo el ancho
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    .align(Alignment.CenterHorizontally)
            )

            // Riel de imágenes
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(7) { index ->
                    Image(
                        painter = painterResource(id = getTutorialImageRes(index)),
                        contentDescription = "Instructivo Imagen $index",
                        modifier = Modifier
                            .size(80.dp)  // Tamaño más pequeño para las imágenes del riel
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                            .clickable {
                                currentIndex.value = index  // Cambia la imagen actual al hacer clic
                            }
                    )
                }
            }

            // Botón para regresar
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    "Regresar",
                    color = Color.White,
                    fontFamily = fontFamily2,
                    fontSize = 24.sp
                )
            }
        }
    }
}


//@Composable
//fun TutorialScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
//    Column(
//        // Modificadores de estilo de la columna
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Riel de imágenes
//        LazyRow(
//            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(7) { index ->
//                Image(
//                    painter = painterResource(id = getTutorialImageRes(index)),
//                    contentDescription = "Instructivo Imagen $index",
//                    modifier = Modifier
//                        .size(400.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                        .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
//                )
//            }
//        }
//
//        // Botón de volver
//        Row{
//            Spacer(modifier = Modifier.height(16.dp))
//
//            IconButton(onClick = {
//                onBackClick()
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBack ,
//                    contentDescription = "Abrir Menu",
//                    tint = Color.Black
//                )
//            }
//
//            Text(
//                text = "Volver",
//                fontSize = 18.sp,
//                modifier = Modifier.padding(8.dp),
//                fontFamily = fontFamily1,
//                color = Color.Black
//            )
//        }
//    }
//}

// Función que devuelve el recurso de la imagen del tutorial correspondiente
fun getTutorialImageRes(index: Int): Int {
    return when (index) {
        0 -> R.drawable.image1
        1 -> R.drawable.image2
        2 -> R.drawable.image3
        3 -> R.drawable.image4
        4 -> R.drawable.image5
        5 -> R.drawable.image6
        6 -> R.drawable.image7
        else -> R.drawable.se // Imagen de reserva
    }
}


@Composable
fun ImageSection(
    modifier: Modifier = Modifier,
    imageUri: Uri? // Cambia aquí a Uri en lugar de Int
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(300.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(20.dp)) // Aplica bordes redondeados
            .shadow(8.dp, RoundedCornerShape(10.dp)) // Añade la sombra
            .background(Color.White) // Fondo blanco
        ,
        contentAlignment = Alignment.Center
    ) {
        // Verifica si hay un URI de imagen disponible
        if (imageUri != null) {
            // Muestra la imagen utilizando la URI
            val bitmap = BitmapFactory.decodeStream(LocalContext.current.contentResolver.openInputStream(imageUri))
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop, // Para que la imagen se ajuste bien
                modifier = Modifier
                    .size(250.dp) // Ajusta el tamaño de la imagen si es necesario
                    .clip(RoundedCornerShape(20.dp)) // Asegúrate de que la imagen también tenga bordes redondeados
                    .shadow(8.dp, RoundedCornerShape(20.dp)) // Añade la sombra
            )
        } else {
            // Muestra una imagen por defecto o un placeholder si no hay URI
            Image(
                painter = painterResource(
                    id = R.drawable.img_3
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop, // Para que la imagen se ajuste bien
                modifier = Modifier
                    .size(250.dp) // Ajusta el tamaño de la imagen si es necesario
                    .clip(RoundedCornerShape(20.dp)) // Asegúrate de que la imagen también tenga bordes redondeados
                    .shadow(8.dp, RoundedCornerShape(20.dp)) // Añade la sombra
            )
        }
        /*
        Image(
            painter = painterResource(
                id = imageUri
            ), // Reemplaza con el nombre de tu imagen
            contentDescription = "Imagen de ejemplo",
            contentScale = ContentScale.Crop, // Para que la imagen se ajuste bien
            modifier = Modifier
                .size(250.dp) // Ajusta el tamaño de la imagen si es necesario
                .clip(RoundedCornerShape(20.dp)) // Asegúrate de que la imagen también tenga bordes redondeados
                .shadow(8.dp, RoundedCornerShape(20.dp)) // Añade la sombra

        )*/
    }
}



@Composable
fun PredictionSection(
    modifier: Modifier = Modifier,
    text: String, // Recibe el texto como parámetro
    onClassifyClick: () -> Unit, // Callback para manejar el clic en el botón
    onClearClick: () -> Unit, // Callback para manejar el clic en el botón "Limpiar"
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(
                Color(0xFFFFD700),
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Clasificación:",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start),
            fontFamily = fontFamily4
        )

        Text(
            text = text,
            fontSize = 24.sp,
            color = Color.Black,
            fontFamily = fontFamily3
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Botón con imagen y texto para abrir la cámara
            ImageButtonWithText(
                imageRes = R.drawable.img, // Reemplaza con el recurso de imagen correcto
                description = "Cámara",
                onClick = {
                    onCameraClick()
                }
            )

            // Botón con imagen y texto para abrir la galería
            ImageButtonWithText(
                imageRes = R.drawable.img_1, // Reemplaza con el recurso de imagen correcto
                description = "Galería",
                onClick = {
                    onGalleryClick()
                }
            )

            // Botón con imagen y texto para limpiar la imagen
            ImageButtonWithText(
                imageRes = R.drawable.img_2, // Reemplaza con el recurso de imagen correcto
                description = "Limpiar",
                onClick = {
                    onClearClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { onClassifyClick()
                },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = "Clasificar Imagen",
                color = Color.White,
                fontFamily = fontFamily2,
                fontSize = 26.sp
            )
        }

    }
}

@Composable
fun ImageButtonWithText(imageRes: Int, description: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centra la imagen y el texto
    ) {
        // Imagen
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = description,
            contentScale = ContentScale.Crop, // Ajuste de la imagen
            modifier = Modifier
                .size(94.dp) // Tamaño de la imagen
                .clip(RoundedCornerShape(15.dp))
        )

        // Texto debajo de la imagen
        Text(
            text = description,
            style = TextStyle(fontSize = 20.sp, color = Color.Black, fontFamily = fontFamily1)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClasificadorObjetosTheme {
        PantallaPrincipal()
    }
}