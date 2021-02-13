package com.example.examen3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SING_IN = 100
    private val utilidades = Utilidades()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setup()
        session()
        // setupDeveloper()
    }

    override fun onStart() {
        super.onStart()
        authLayout.visibility = View.VISIBLE
    }

    private fun session() {
        // Si existen las preferencias --> siguiente Activity

        val utilidades = Utilidades()

        var email = utilidades.getEmailPreferences(this)
        val provider = utilidades.getProviderPreferences(this)

        if (email != null && provider != null) {
            authLayout.visibility = View.INVISIBLE
            showFolowActivity(email, ProviderType.valueOf(provider))
        }

        /* // llamada a preferences pasada a utilidades
        val prefs: SharedPreferences =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        */
    }

    private fun setupDeveloper() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setup() {
        title = "Autentificacion"

        //////  BOTON  REGISTRARSE      //////////////////////////////////////////

        registerButton.setOnClickListener {
            var myEmail = emailEditText.text.toString()
            var myPassword = passWordEditText.text.toString()
            var utilidades = Utilidades()
            if (utilidades.isValidEmail(myEmail) && utilidades.isValidPassword(myPassword)) {
                // registrar en Firebase
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(myEmail, myPassword)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Registro satisfactorio -> continuar app
                            Toast.makeText(
                                this,
                                "Enhorabuena, registro satisfactorio",
                                Toast.LENGTH_SHORT
                            ).show();

                            this.showFolowActivity(
                                it.result?.user?.email
                                    ?: "", ProviderType.BASIC
                            );
                        } else {
                            // no se ha registrado
                            utilidades.mostrarAviso(
                                this,
                                "Error de Autentificacion",
                                "No has podido registrarte en la aplicacion, puede que ya estes registrado"
                            )
                        }
                    }
            } else {
                // Correo o Contraseña no valido mostrar ventana
                utilidades.mostrarAviso(this, "Aviso", "El correo o la contraseña son incorrentos")
            }
        }

        //////  BOTON  LOGEARSE     //////////////////////////////////////////

        loginButton.setOnClickListener {
            var myEmail = emailEditText.text.toString()
            var myPassword = passWordEditText.text.toString()

            if (utilidades.isValidEmail(myEmail) && utilidades.isValidPassword(myPassword)) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(myEmail, myPassword)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // login satisfactorio continuar app
                            this.showFolowActivity(
                                it.result?.user?.email
                                    ?: "", ProviderType.BASIC
                            );
                        } else {
                            // no se ha registrado
                            utilidades.mostrarAviso(
                                this,
                                "Error de Login",
                                "No has podido ingresar en la aplicacion, Comprueba tu email y contraseña"
                            )
                        }
                    }
            } else {
                // Correo o Contraseña no valido mostrar ventana
                utilidades.mostrarAviso(this, "Aviso", "El correo o la contraseña son incorrentos")
            }
        }

        //////  BOTON  GOOGLE     //////////////////////////////////////////
        googleButton.setOnClickListener {
            // configuracion de google

            val googleconf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            // cliente de identificador de google
            val googleClient = GoogleSignIn.getClient(this, googleconf)
            googleClient.signOut()
            // llamar a la atentificacion de google
            startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)
        }

    }

    //// IR A LA SIGUIENTE ACTIVIDAD

    private fun showFolowActivity(email: String?, provider: ProviderType) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("provider", provider.name)

        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SING_IN) {
            // la respuesta se corresconde con la autentificacion de google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // login satisfactorio continuar app
                                this.showFolowActivity(account.email ?: "", ProviderType.GOOGLE);
                            } else {
                                // no se ha registrado
                                utilidades.mostrarAviso(
                                    this,
                                    "Error de Login",
                                    "No has podido ingresar en la aplicacion, Comprueba tu email y contraseña"
                                )
                            }
                        }

                }
            } catch (ex: ApiException) {
                utilidades.mostrarAviso(
                    this,
                    "Error en Autentificacion",
                    "No has podido logearte con google"
                )

            }
        }
    }
}


