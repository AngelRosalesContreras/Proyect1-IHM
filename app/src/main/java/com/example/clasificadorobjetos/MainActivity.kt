package com.example.clasificadorobjetos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.leanback.widget.Row
import com.example.clasificadorobjetos.ui.theme.ClasificadorObjetosTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent()
        }
    ) {
        Scaffold(

            topBar = { TopBar { scope.launch { drawerState.open() } } },


            ) { innerPadding ->
            ScrollContent(innerPadding)
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
            Text(text = "Clasificador", color = Color.Black, fontSize = 22.sp)
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
                .weight(1f) // Esto asegura que ocupe la mitad de la pantalla verticalmente
        )
        PredictionSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Esto asegura que ocupe la otra mitad de la pantalla verticalmente
        )

    }
}

@Composable
fun DrawerContent() {

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

                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Filled.Info ,
                        contentDescription = "Abrir Menu",
                        tint = Color.Black
                    )
                }

                Text(text = "Como Usar", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ){
                Column {
                    Text(text = "Usar tema del",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp))
                    Text(text = "sistema",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp))
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
                Text(text = "Modo oscuro", fontSize = 18.sp, modifier = Modifier.padding(8.dp))

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
            onClick = { /* Acción del botón */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Salir",  color = Color.White)
        }
    }
}

@Composable
fun ImageSection(modifier: Modifier = Modifier) {
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
        Image(
            painter = painterResource(id = R.drawable.se), // Reemplaza con el nombre de tu imagen
            contentDescription = "Imagen de ejemplo",
            contentScale = ContentScale.Crop, // Para que la imagen se ajuste bien
            modifier = Modifier
                .size(250.dp) // Ajusta el tamaño de la imagen si es necesario
                .clip(RoundedCornerShape(20.dp)) // Asegúrate de que la imagen también tenga bordes redondeados
                .shadow(8.dp, RoundedCornerShape(20.dp)) // Añade la sombra

        )
    }
}



@Composable
fun PredictionSection(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(
                Color(0xFFFFD700),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Predicción", fontSize = 24.sp, color = Color.Black)

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
                onClick = { /* Acción para abrir cámara */ }
            )

            // Botón con imagen y texto para abrir la galería
            ImageButtonWithText(
                imageRes = R.drawable.img_1, // Reemplaza con el recurso de imagen correcto
                description = "Galería",
                onClick = { /* Acción para abrir galería */ }
            )

            // Botón con imagen y texto para limpiar la imagen
            ImageButtonWithText(
                imageRes = R.drawable.img_2, // Reemplaza con el recurso de imagen correcto
                description = "Limpiar",
                onClick = { /* Acción para limpiar imagen */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { /* Acción para clasificar imagen */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Limpiar", color = Color.White)
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
            style = TextStyle(fontSize = 14.sp, color = Color.Black)
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