package com.example.examen3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_database.*


class DatabaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        val db = FirebaseFirestore.getInstance()
        val TAG = "TAG++++++++++++++++++"
        // recoger datos intent
        val email = intent.extras?.getString("email")

        textViewEmail.text = email

        saveButton.setOnClickListener {
            Log.d("myTag", "entra save: " + email)

            if (email != null) {
                db.collection("users").document(email).set(
                    hashMapOf(
                        "address" to addressEditText.text.toString(),
                        "phone" to phoneEditText.text.toString()
                    )
                )
            }
        }
        readButton.setOnClickListener {
            Log.d("myTag", "entra leer: " + email)
            if (email != null) {
                db.collection("users").document(email).get()
                    .addOnSuccessListener {
                        addressEditText.setText(it["address"].toString())
                        phoneEditText.setText(it.getString("phone"))
                        Log.d("myTag", "addres: " + it["address"])
                        Log.d("myTag", "phone: " + it["phone"])
                    }
            }
        }
        deleteButton.setOnClickListener {
            if (email != null) {
                db.collection("users").document(email).delete()
            }
        }

        saveButton2.setOnClickListener {
            Log.d("myTag", "entra guardar map: " + email)
            // Create a new user
            val user2 = hashMapOf(
                "address" to addressEditText.text.toString(),
                "phone" to phoneEditText.text.toString(),
                "country" to "USA"
            )

            // otra forma de Create a new user
            val user: MutableMap<String, Any> = HashMap()
            user["address"] = addressEditText.text.toString()
            user["phone"] = phoneEditText.text.toString()
            user["born"] = 1815
            user["email"] = email ?: ""

            // A la coleccion users,al documento de clave este email, le damos el valor del usuario [set(user)]
            if (email != null) {
                db.collection("users").document(email).set(user2)
                    .addOnSuccessListener {
                        Log.d(
                            "TAG ++",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w("TAG ++", "Error writing document", e) }
            }

            if (email != null) {
                db.collection("users").document(email).set(user)
                    .addOnSuccessListener { Log.d("TAG ++", "DocumentSnapshot added with ID: ") }
                    .addOnFailureListener { e -> Log.w("TAG --", "Error adding document", e) }
            }

            // Add a new document with a generated ID
            // Directamente se añade (add) usuario a la coleccion users
            db.collection("users").add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "TAG ++",
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        "TAG ++",
                        "Error adding document",
                        e
                    )
                }

        }
        readButton2.setOnClickListener {
            var lista :MutableList<String>
            lista= mutableListOf()
            Log.d("myTag", "entra leer coleccion: ")
            db.collection("users").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                             lista.add (document.id)
                            Log.d(TAG, "======> " + lista.toString())
                        }
                        Log.d(TAG, "+++++++> " + lista.toString())
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }
    }

}