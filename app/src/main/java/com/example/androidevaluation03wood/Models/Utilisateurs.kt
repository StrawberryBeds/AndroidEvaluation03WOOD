package com.example.androidevaluation03wood.Models

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

data class Utilisateur(
    val nomEtPrenom: String = "Penny Counter",
    val nomUtilisateur: String = "user@example.com",
    val motDePasse: String = "password123",
    var estVerifie: Boolean = false,
)

class ViewModelUtilisateur(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AppPrefs", MODE_PRIVATE)
    private val TAG = "ViewModelUtilisateur"

    var utilisateur by mutableStateOf(
        Utilisateur(
            estVerifie = false
        )
    )

    init {
        Log.d(TAG, "ViewModelUtilisateur created")
        // Load data from SharedPreferences, overriding default values
        utilisateur = loadUtilisateur()
        Log.d(TAG, "Initial utilisateur.estVerifie: ${utilisateur.estVerifie}")
    }

    private fun loadUtilisateur(): Utilisateur {
        Log.d(TAG, "loadUtilisateur() called")
        val nomEtPrenom =
            sharedPreferences.getString("NOM_ET_PRENOM", "Penny Counter") ?: "Penny Counter"
        val nomUtilisateur =
            sharedPreferences.getString("NOM_UTILISATEUR", "user@example.com") ?: "user@example.com"
        val motDePasse = sharedPreferences.getString("MOT_DE_PASSE", "password123") ?: "password123"
        val estVerifie = sharedPreferences.getBoolean("UTILISATEUR_VERIFIE", false)

        Log.d(TAG, "Loaded from SharedPreferences:")
        Log.d(TAG, "  nomEtPrenom: $nomEtPrenom")
        Log.d(TAG, "  nomUtilisateur: $nomUtilisateur")
        Log.d(TAG, "  motDePasse: $motDePasse")
        Log.d(TAG, "  estVerifie: $estVerifie")

        return Utilisateur(nomEtPrenom, nomUtilisateur, motDePasse, estVerifie)
    }

    fun verifierUtilisateur(nomUtilisateur: String, motDePasse: String): Boolean {
        Log.d(TAG, "verifierUtilisateur() called with:")
        Log.d(TAG, "  nomUtilisateur: $nomUtilisateur")
        Log.d(TAG, "  motDePasse: $motDePasse")

        val estVerifie =
            (utilisateur.nomUtilisateur == nomUtilisateur) && (utilisateur.motDePasse == motDePasse)
        utilisateur = utilisateur.copy(estVerifie = estVerifie)
        sharedPreferences.edit().putBoolean("UTILISATEUR_VERIFIE", estVerifie).apply()

        Log.d(TAG, "  estVerifie result: $estVerifie")
        return estVerifie
    }

    fun deconecterUtilisateur() {
            Log.d(TAG, "deconneterUtilisateur() called")
            utilisateur = utilisateur.copy(estVerifie = false)
            Log.d(TAG, "utilisateur state updated: estVerifie = false")
            // Save an empty JSON array to SharedPreferences
            val nomFichier = AppOutils.apporteNomFichierUtilisateur(utilisateur.nomUtilisateur)
            val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
            val editor = sharedPreferencesUtilisateur.edit()
            editor.putString("PREF_KEY_TRANSACTIONS", "[]") // Save an empty array
            editor.apply()
        }

    fun estUtilisateurVerifie(): Boolean {
        Log.d(TAG, "estUtilisateurVerifie() called")
        Log.d(TAG, "  returning: ${utilisateur.estVerifie}")
        return utilisateur.estVerifie
    }
}
