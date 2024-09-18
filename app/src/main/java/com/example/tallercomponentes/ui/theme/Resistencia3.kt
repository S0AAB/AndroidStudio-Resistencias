package com.example.tallercomponentes.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tallercomponentes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun Resistencia3() {
    // Estados para bandas y menús expandidos
    var bandas by remember { mutableStateOf(mutableMapOf("banda1" to 0, "banda2" to 0, "banda3" to 0, "bandaM" to 0, "bandaT" to 0)) }
    var isExpanded by remember { mutableStateOf(mutableMapOf("banda1" to false, "banda2" to false, "banda3" to false, "bandaM" to false, "bandaT" to false)) }

    // Estados para los textos de las bandas
    var textoBanda1 by remember { mutableStateOf("Valor banda 1") }
    var textoBanda2 by remember { mutableStateOf("Valor banda 2") }
    var textoBanda3 by remember { mutableStateOf("Valor banda 3") }
    var textoBandaM by remember { mutableStateOf("Valor banda multiplicador") }
    var textoBandaT by remember { mutableStateOf("Valor banda tolerancia") }

    val colores = mapOf(
        "Negro" to Color.Black,
        "Café" to Color(0xFFA52A2A),
        "Rojo" to Color.Red,
        "Naranja" to Color(0xFFFFA500),
        "Amarillo" to Color.Yellow,
        "Verde" to Color(0xFF22BD17),
        "Azul" to Color(0xFF1430C0),
        "Violeta" to Color(0xFF4312CC),
        "Gris" to Color(0xFF767D8A),
        "Blanco" to Color(0xFFFAFAFA),
        "Dorado" to Color(0xFFAA7108),
        "Plateado" to Color(0xFFA5A4A2)
    )

    val multiplicadores = listOf(1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 0.1, 0.01)
    val tolerancias = listOf(1, 2, 5, 10)

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Text(
                text = "RESISTENCIA 3 BANDAS",
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(15.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth().padding(10.dp).height(100.dp),
                painter = painterResource(id = R.drawable.resisstor),
                contentDescription = "Imagen"
            )

            bandas.keys.forEachIndexed { index, banda ->
                val expanded = isExpanded[banda] ?: false
                val valor = bandas[banda] ?: 0
                val colorSeleccionado = colores.values.toList().getOrNull(valor) ?: Color.White // Color seleccionado

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { isExpanded = isExpanded.toMutableMap().apply { put(banda, it) } }
                    ) {
                        // Actualiza el texto correspondiente basado en el índice
                        val textoBanda = when (index) {
                            3 -> textoBandaM
                            4 -> textoBandaT
                            0 -> textoBanda1
                            1 -> textoBanda2
                            2 -> textoBanda3
                            else -> "Valor banda ${index + 1}"
                        }
                        TextField(
                            value = textoBanda,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .background(colorSeleccionado)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { isExpanded = isExpanded.toMutableMap().apply { put(banda, false) } }
                        ) {
                            colores.entries.forEachIndexed { colorIndex, (colorName, colorValue) ->
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(colorValue)
                                            )
                                            Text(text = "  $colorName")
                                        }
                                    },
                                    onClick = {
                                        val salida = when {
                                            (index == 3 || index == 4) -> colorIndex
                                            colorIndex <= 9 -> colorIndex
                                            else -> 0
                                        }

                                        // Actualiza el texto de la banda según el índice
                                        when (index) {
                                            0 -> textoBanda1 = colorName
                                            1 -> textoBanda2 = colorName
                                            2 -> textoBanda3 = colorName
                                            3 -> textoBandaM = colorName
                                            4 -> textoBandaT = colorName
                                        }

                                        bandas = bandas.toMutableMap().apply { put(banda, salida) }
                                        isExpanded = isExpanded.toMutableMap().apply { put(banda, false) }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val valorSignificativo = bandas["banda1"]!! * 100 + bandas["banda2"]!! * 10 + bandas["banda3"]!!
                    val multiplicador = multiplicadores.getOrElse(bandas["bandaM"]!!) {1}
                    val tolerancia = tolerancias.getOrElse(bandas["bandaT"]!! - 10) { 20 } // Tolerancia por defecto 20%

                    val valorResistencia = valorSignificativo * multiplicador.toInt()
                    Toast.makeText(context, "Valor de la resistencia: $valorResistencia Ω ± $tolerancia%", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Text(text = "Calcular Resistencia")
            }
        }
    }
}
