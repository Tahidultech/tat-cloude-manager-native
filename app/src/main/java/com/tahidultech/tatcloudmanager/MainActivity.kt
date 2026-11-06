package com.tahidultech.tatcloudmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.tahidultech.tatcloudmanager.data.CloudFile
import com.tahidultech.tatcloudmanager.data.PrefsManager
import com.tahidultech.tatcloudmanager.data.VaultAccount

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PrefsManager(this)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TatManagerApp(prefs)
                }
            }
        }
    }
}

@Composable
fun TatManagerApp(prefs: PrefsManager) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController, prefs)
        }

        composable("files") {
            FilesScreen(navController, prefs)
        }

        composable("add_file") {
            AddFileScreen(navController, prefs)
        }

        composable("vault") {
            VaultScreen(navController, prefs)
        }

        composable("add_account") {
            AddAccountScreen(navController, prefs)
        }

        composable("settings") {
            SettingsScreen(navController, prefs)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (title != "TAT Manager") {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                }
            )
        },
        content = content
    )
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    prefs: PrefsManager
) {
    val files = remember { mutableStateOf(prefs.getFiles()) }
    val accounts = remember { mutableStateOf(prefs.getAccounts()) }

    AppScaffold(
        title = "TAT Manager",
        navController = navController
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Cloud Manager",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    "Manage your cloud files, links and account information.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                DashboardCard(
                    title = "Cloud Files",
                    value = files.value.size.toString(),
                    icon = Icons.Default.Cloud,
                    onClick = { navController.navigate("files") }
                )
            }

            item {
                DashboardCard(
                    title = "Vault Accounts",
                    value = accounts.value.size.toString(),
                    icon = Icons.Default.Lock,
                    onClick = { navController.navigate("vault") }
                )
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("add_file") }
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Cloud File")
                }
            }

            item {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("settings") }
                ) {
                    Icon(Icons.Default.Settings, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Settings")
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Open",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    navController: NavHostController,
    prefs: PrefsManager
) {
    var files by remember { mutableStateOf(prefs.getFiles()) }

    AppScaffold(
        title = "Cloud Files",
        navController = navController
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (files.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CloudOff,
                        null,
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "No cloud files yet",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("add_file") }
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add File")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(files) { file ->
                        FileCard(
                            file = file,
                            onDelete = {
                                files = files.filter { it.id != file.id }.toMutableList()
                                prefs.setFiles(files)
                            }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("add_file") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    }
}

@Composable
fun FileCard(
    file: CloudFile,
    onDelete: () -> Unit
) {
    var showDelete by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.InsertDriveFile, null)

                Spacer(Modifier.width(12.dp))

                Text(
                    file.title.ifBlank { "Untitled File" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { showDelete = true }) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }

            Spacer(Modifier.height(8.dp))

            Text("Category: ${file.category}")
            Text("Cloud: ${file.cloud}")

            if (file.cloudAcc.isNotBlank()) {
                Text("Account: ${file.cloudAcc}")
            }

            if (file.folderPath.isNotBlank()) {
                Text("Folder: ${file.folderPath}")
            }

            if (file.directLink.isNotBlank()) {
                Text("Link: ${file.directLink}")
            }

            if (file.tags.isNotBlank()) {
                Text("Tags: ${file.tags}")
            }

            if (file.note.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text("Note: ${file.note}")
            }
        }
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Delete file?") },
            text = { Text("This file entry will be removed from the device.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDelete = false
                        onDelete()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDelete = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFileScreen(
    navController: NavHostController,
    prefs: PrefsManager
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Software Project") }
    var cloud by remember { mutableStateOf("Google Drive") }
    var cloudAcc by remember { mutableStateOf("") }
    var folderPath by remember { mutableStateOf("") }
    var directLink by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AppScaffold(
        title = "Add Cloud File",
        navController = navController
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = cloud,
                    onValueChange = { cloud = it },
                    label = { Text("Cloud Provider") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = cloudAcc,
                    onValueChange = { cloudAcc = it },
                    label = { Text("Cloud Account") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = folderPath,
                    onValueChange = { folderPath = it },
                    label = { Text("Folder Path") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = directLink,
                    onValueChange = { directLink = it },
                    label = { Text("Direct Link") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val file = CloudFile(
                            title = title,
                            category = category,
                            cloud = cloud,
                            cloudAcc = cloudAcc,
                            folderPath = folderPath,
                            directLink = directLink,
                            tags = tags,
                            note = note
                        )

                        val files = prefs.getFiles()
                        files.add(file)
                        prefs.setFiles(files)

                        navController.popBackStack()
                    }
                ) {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save File")
                }
            }
        }
    }
}

@Composable
fun VaultScreen(
    navController: NavHostController,
    prefs: PrefsManager
) {
    var accounts by remember { mutableStateOf(prefs.getAccounts()) }

    AppScaffold(
        title = "Vault",
        navController = navController
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (accounts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Lock,
                        null,
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "No accounts saved",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("add_account") }
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Account")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(accounts) { account ->
                        AccountCard(
                            account = account,
                            onDelete = {
                                accounts = accounts
                                    .filter { it.id != account.id }
                                    .toMutableList()
                                prefs.setAccounts(accounts)
                            }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("add_account") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    }
}

@Composable
fun AccountCard(
    account: VaultAccount,
    onDelete: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, null)

                Spacer(Modifier.width(12.dp))

                Text(
                    account.name.ifBlank { account.provider },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }

            Text("Provider: ${account.provider}")

            if (account.mail.isNotBlank()) {
                Text("Email: ${account.mail}")
            }

            if (account.password.isNotBlank()) {
                Text(
                    "Password: ${
                        if (showPassword) account.password else "••••••••"
                    }"
                )

                TextButton(
                    onClick = { showPassword = !showPassword }
                ) {
                    Text(if (showPassword) "Hide Password" else "Show Password")
                }
            }

            if (account.note.isNotBlank()) {
                Text("Note: ${account.note}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    navController: NavHostController,
    prefs: PrefsManager
) {
    var provider by remember { mutableStateOf("Google") }
    var name by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AppScaffold(
        title = "Add Vault Account",
        navController = navController
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = provider,
                    onValueChange = { provider = it },
                    label = { Text("Provider") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Account Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = mail,
                    onValueChange = { mail = it },
                    label = { Text("Email / Username") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val account = VaultAccount(
                            provider = provider,
                            name = name,
                            mail = mail,
                            password = password,
                            note = note
                        )

                        val accounts = prefs.getAccounts()
                        accounts.add(account)
                        prefs.setAccounts(accounts)

                        navController.popBackStack()
                    }
                ) {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Account")
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    navController: NavHostController,
    prefs: PrefsManager
) {
    AppScaffold(
        title = "Settings",
        navController = navController
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "TAT Manager",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("Local cloud file and account manager")
                    Text("Version 1.0")
                }
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.popBackStack() }
            ) {
                Text("Back")
            }
        }
    }
}
