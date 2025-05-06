package com.binarybeats.petconnect

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchPet : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchView: SearchView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var petList: ArrayList<PetData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_pet)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        searchView = findViewById(R.id.search)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Pets")

        petList = ArrayList()

        searchAdapter = SearchAdapter(petList, this@SearchPet)
        recyclerView.adapter = searchAdapter

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                petList.clear()
                for (dataSnapshot in snapshot.children) {
                    val petData: PetData? = dataSnapshot.getValue(PetData::class.java)
                    petData?.let { petList.add(it) }
                }
                searchAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SearchPet", "Failed to read value.", error.toException())
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchAdapter.filterBySpecies(it) }
                return false
            }
        })
    }

}

