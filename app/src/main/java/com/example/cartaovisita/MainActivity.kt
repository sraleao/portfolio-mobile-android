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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cartaovisita.data.local.AppDatabase
import com.example.cartaovisita.data.remote.RetrofitInstance
import com.example.cartaovisita.repository.ProjectRepository
import com.example.cartaovisita.ui.theme.CartaoVisitaTheme
import com.example.cartaovisita.ui.viewmodel.ProjectListViewModel
import com.example.cartaovisita.ui.viewmodel.ProjectListViewModelFactory
import com.example.cartaovisita.ui.viewmodel.UiState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Banco de dados
    val db = remember {
        androidx.room.Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }
    val dao = db.projectDao()

    // Repository com usuário GitHub: sraleao
    val repository = remember {
        ProjectRepository(dao, RetrofitInstance.api, "sraleao")
    }

    // ViewModel
    val viewModel: ProjectListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ProjectListViewModelFactory(repository)
    )

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
                viewModel = viewModel,
                onProjetoClick = { projetoId ->
                    navController.navigate("detalhes/$projetoId")
                },
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalhes/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            TelaDetalhesProjeto(
                id = id,
                repository = repository,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}

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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6E270D),
                            Color(0xFFBE6A53)
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

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

            Button(
                onClick = onVerProjetosClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x750E0E0E)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "Conheça meus Projetos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaProjetos(
    viewModel: ProjectListViewModel,
    onProjetoClick: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    Brush.verticalGradient(
                        listOf(Color(0xFF6E270D), Color(0xFFBE6A53))
                    )
                )
                .padding(padding)
        ) {

            when (uiState) {

                UiState.Loading -> {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Carregando projetos...", color = Color.White)
                    }
                }

                is UiState.Error -> {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Erro ao carregar projetos",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                is UiState.Success -> {
                    val projetos = (uiState as UiState.Success).projects

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(projetos) { projeto ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clickable { onProjetoClick(projeto.id) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.6f)
                                ),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(projeto.nome, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(projeto.descricao ?: "")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesProjeto(
    id: Int,
    repository: ProjectRepository,
    onVoltar: () -> Unit
) {
    val projeto by repository.getProjectByIdFlow(id).collectAsState(initial = null)

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
                .background(Brush.verticalGradient(listOf(Color(0xFF6E270D), Color(0xFFBE6A53))))
                .padding(padding)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (projeto == null) {
                    Text("Carregando...", color = Color.White)
                } else {
                    Text(
                        "ID do projeto: ${projeto!!.id}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        projeto!!.descricao ?: "Sem descrição",
                        color = Color.White
                    )
                }
            }
        }
    }
}

