package com.example.cartaovisita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cartaovisita.ui.theme.CartaoVisitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartaoVisitaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CartaoDeVisitas()
                }
            }
        }
    }
}

@Composable
fun CartaoDeVisitas() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Fundo curvado (metade inferior da tela)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color(0xFF04497E)) // azul escuro
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // centraliza os elementos na vertical
            modifier = Modifier.fillMaxSize()
        ) {
            // Foto redonda sobreposta
            Image(
                painter = painterResource(id = R.drawable.foto),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
                    .offset(y = 0.dp) // empurra para sobrepor fundo curvado
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Nome
            Text(
                text = "Ellen Leao",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Profissão
            Text(
                text = "Tecnologista em Sistemas para Internet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Ícones de contato (dispostos em grade 2x2)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    IconeContato(R.drawable.ic_phone)
                    IconeContato(R.drawable.ic_email)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    IconeContato(R.drawable.ic_github)
                    IconeContato(R.drawable.ic_location)
                }
            }
        }
    }
}

@Composable
fun IconeContato(icone: Int) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icone),
            contentDescription = null,
            modifier = Modifier.size(55.dp)
        )
    }
}
