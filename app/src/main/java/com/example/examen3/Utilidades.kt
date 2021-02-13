package com.example.examen3

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.app.AlertDialog


class Utilidades {

    // VALIDACIONES

    public fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    public fun isValidPassword(password: String): Boolean {
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"
        val pattern = Regex(PASSWORD_PATTERN)
        return pattern.containsMatchIn(password)
    }

    // EXPRESIONES REGULARES
    //^ # inicio-de-cadena
    //(? =. * [0-9]) # un dígito debe aparecer al menos una vez
    //(? =. * [a-z]) # una letra minúscula debe aparecer al menos una vez
    //(? =. * [A-Z]) # una letra mayúscula debe aparecer al menos una vez
    //(? =. * [@ # $% ^ & + =]) # un carácter especial debe aparecer al menos una vez que pueda reemplazarlo con sus caracteres especiales
    // (? = \\ S + $) # no se permiten espacios en blanco en toda la cadena
    //. {4,} # . cualquier caracter,{n} n lugares,{n,m} n=min, m=max
    //$ # final de cadena

    // POP UP

    public fun mostrarAviso(contex: Context, titulo: String, mensaje: String) {
        AlertDialog.Builder(contex)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->
                    //botón OK pulsado
                })
            .show()
    }

    // GUARDAR EN SHARE PREFERENCES
    public fun savePreferences(contex: Context, email: String?, provider: String?) {
        val prefs: SharedPreferences.Editor =
            contex.getSharedPreferences(contex.getString(R.string.prefs_file), Context.MODE_PRIVATE)
                .edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
    }

    // BORRAR SHARE PREFERENCES
    public fun deletePreferences(contex: Context) {
        val prefs: SharedPreferences.Editor =
            contex.getSharedPreferences(contex.getString(R.string.prefs_file), Context.MODE_PRIVATE)
                .edit()
        prefs.clear()
        prefs.apply()

    }

    // DEVOLVER LAS PREFERENCES
    public fun getEmailPreferences(contex: Context): String? {
        val prefs: SharedPreferences =
            contex.getSharedPreferences(contex.getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
    //    val provider = prefs.getString("provider", null)
        return email
    }
    public fun getProviderPreferences(contex: Context): String? {
        val prefs: SharedPreferences =
            contex.getSharedPreferences(contex.getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val provider = prefs.getString("provider", null)
        return provider
    }
}


