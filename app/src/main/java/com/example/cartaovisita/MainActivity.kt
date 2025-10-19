package com.example.cartaovisita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.rememberNavController
import com.example.cartaovisita.ui.theme.CartaoVisitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartaoVisitaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

// --- Navegação ---
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "perfil") {
        composable("perfil") {
            CartaoDeVisitas(
                onVerProjetosClick = {
                    navController.navigate("projetos")
                }
            )
        }

        composable("projetos") {
            TelaListaProjetos(
                onProjetoClick = { projetoId ->
                    navController.navigate("detalhes/$projetoId")
                },
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalhes/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            TelaDetalhesProjeto(id, onVoltar = { navController.popBackStack() })
        }
    }
}

// --- Tela de Perfil ---
@Composable
fun CartaoDeVisitas(onVerProjetosClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Fundo curvado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6E270D),
                        Color(0xFFBE6A53)
        ))))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Foto
            Image(
                painter = painterResource(id = R.drawable.foto),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Nome e profissão
            Text(
                text = "Ellen Leao",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Tecnologista em Sistemas para Internet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Ícones de contato
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    IconeContato(R.drawable.ic_phone)
                    IconeContato(R.drawable.ic_email)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    IconeContato(R.drawable.ic_github)
                    IconeContato(R.drawable.ic_location)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botão "Ver Meus Projetos" centralizado
            Button(
                onClick = onVerProjetosClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x750E0E0E)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Conheça meus Projetos", style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                    color = Color.White)
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

// --- Dados Mock ---
data class Projeto(val id: Int, val nome: String, val descricao: String)

val mockProjetos = listOf(
    Projeto(1, "App de Biblioteca", "Aplicativo que faz gerenciamento de livros e empréstimos."),
    Projeto(2, "Catálogo de Livros", "Aplicativo que exibe catálogo de livros."),
    Projeto(3, "App de comida", "Aplicativo que faz pedidos de comida"),
    Projeto(4, "Site Pessoal", "Página web com portfólio e contatos.")
)

// --- Tela de Lista de Projetos ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaProjetos(onProjetoClick: (Int) -> Unit, onVoltar: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Projetos") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6E270D),
                            Color(0xFFBE6A53)
                        )
                    )
                )
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockProjetos) { projeto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clickable { onProjetoClick(projeto.id) },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(projeto.nome, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(projeto.descricao)
                        }
                    }
                }
            }
        }
}}

// --- Tela de Detalhes do Projeto ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesProjeto(id: Int, onVoltar: () -> Unit) {
    val projeto = mockProjetos.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(projeto?.nome ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6E270D),
                            Color(0xFFBE6A53)
                        )
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ID do projeto: ${projeto?.id}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = projeto?.descricao ?: "Descrição não encontrada", color = Color.White)
        }    }
    }
}
