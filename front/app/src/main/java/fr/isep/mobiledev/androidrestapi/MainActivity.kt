package fr.isep.mobiledev.androidrestapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isep.mobiledev.androidrestapi.ui.theme.AndroidRestAPITheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidRestAPITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        val retrofit : Retrofit = Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl("http://192.168.56.1:8080/").build()
                        val groceryService : GroceryService = retrofit.create(GroceryService::class.java)

                        val groceries = remember { mutableStateListOf<Grocery>() }
                        getGroceries(groceryService, groceries)

                        AddItemSection(groceryService, groceries)
                        ListItems(groceryService, groceries)
                    }
                }
            }
        }
    }
}

fun getGroceries(groceryService: GroceryService, items: MutableList<Grocery>){
    groceryService.listGroceries().enqueue(object : Callback<List<Grocery>> {
        override fun onResponse(call: Call<List<Grocery>>, response: Response<List<Grocery>>) {
            if(response.isSuccessful){
                val newGroceries : List<Grocery>? = response.body()
                if(newGroceries != null){
                    items.clear();
                    items.addAll(newGroceries)
                    Log.println(Log.INFO, "MainActivity", "Groceries: $items")
                }
            }
        }
        override fun onFailure(call: Call<List<Grocery>>, t: Throwable) {
            Log.e("MainActivity", "Error while getting groceries", t)
        }
    })
}

@Composable
fun AddItemSection(groceryService: GroceryService, items: MutableList<Grocery>) {
    var item by remember { mutableStateOf(TextFieldValue("")) }
    TextField(modifier = Modifier.fillMaxWidth(),
        value = item,
        onValueChange = { item = it },
        label = { Text("Item to add") }
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        onClick = {
            if(item.text.isNotEmpty()){
                groceryService.addGrocery(Grocery(item.text, 1)).enqueue(object : Callback<Long> {
                    override fun onResponse(call: Call<Long>, response: Response<Long>) {
                        if(response.isSuccessful){
                            item = TextFieldValue("")
                            getGroceries(groceryService, items)
                        }
                    }
                    override fun onFailure(call: Call<Long>, t: Throwable) {
                        Log.e("MainActivity", "Error while adding item", t)
                    }
                })
            }
        }){
        Text("Add")
    }
}

@Composable
fun ListItems(groceryService: GroceryService, items: MutableList<Grocery>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items.size) { item ->
            GroceryItem(groceryService, items, item)
            Log.println(Log.INFO, "MainActivity", "Grocery: ${items[item].name}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryItem(groceryService: GroceryService, items: MutableList<Grocery>, item : Int) {
    val grocery : Grocery = items[item]
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Text(text = grocery.name, modifier = Modifier.fillMaxWidth(0.7f),)

        var expanded by remember { mutableStateOf(false) }
        var quantity by remember { mutableStateOf(grocery.quantity.toString()) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
            TextField(
                value = quantity,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Remove") }, onClick = {
                    Log.println(Log.INFO, "MainActivity", "Removing item: ${grocery.name} ${grocery.quantity} ${grocery.id}")
                    groceryService.removeGrocery(grocery).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                getGroceries(groceryService, items)
                            }
                        }
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("MainActivity", "Error while removing item", t)
                        }
                    })
                })
                for (i in 1..100) {
                    DropdownMenuItem(text = { Text(i.toString()) }, onClick = {
                        groceryService.updateGrocery(Grocery(grocery.id, grocery.name, i)).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    expanded = false
                                    quantity = i.toString()
                                }
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("MainActivity", "Error while updating item", t)
                            }
                        })
                    })
                }
            }
        }
    }
}