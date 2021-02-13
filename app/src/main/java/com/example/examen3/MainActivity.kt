package com.example.examen3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

enum class ProviderType {
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val utilidades = Utilidades()

        // Recogida parametros  (Aunque puedan ser nulos, si han llegado aqui es que son no nulos)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        // GUARDAR EN SHARE PREFERENCES
        utilidades.savePreferences(this, email, provider)


        setup(utilidades, email ?: "no email in intent", provider ?: "no provider")


    }

    private fun setup(utilidades: Utilidades, email: String, provider: String) {
        title = "Home"

        textViewEmail.text = email
        textViewProvider.text = provider

//////// BOTON DESLOGUEARSE ///////////////////////////////////////////////
        logOutButton.setOnClickListener {
            // borrar preferences, deslogearse de firebase, volver a activity anterior

            // BORRAR SHARE PREFERENCES
            utilidades.deletePreferences(this)

            // DESLOGUEARSE DE FIREBASE
            FirebaseAuth.getInstance().signOut()

            // VOLVER ACTIVITY ANTERIOR
            val intent = Intent(this, AuthActivity::class.java);
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK;
            startActivity(intent);

            /* //otra forma de borrar
              val prefs: SharedPreferences.Editor =
              getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
              prefs.clear()
              prefs.apply()
          */
            //  Mas seguro sustituido por startActivity vacia /
            //    onBackPressed()
        }


        /*     // Ir a Posicion Fija
             buttonMapFijo.setOnClickListener {
                 val intent = Intent(this, LocalizacionFijaActivity::class.java);
                 startActivity(intent)
             }
             // Ir a Ubicacion real
             buttonMapReal.setOnClickListener {
                 val intent = Intent(this, MapsActivity2::class.java);
                 startActivity(intent)
             }
     */
    }
}