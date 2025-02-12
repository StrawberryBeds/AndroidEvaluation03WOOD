package com.example.androidevaluation03wood

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Tache(
    val idTache: Int,
    val nomTache: String,
    val descriptionTache: String,
    var estTerminee: Boolean
)

class Utilisateur (
    val nomUtilisateur: String = "user@example.com",
    val motDePasse: String = "password123",
    var estVerifie: Boolean = false)

class ViewModelTaches (application: Application): AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AppPrefs", MODE_PRIVATE)
    private val gson = Gson()

    private var utilisateur = Utilisateur(
        nomUtilisateur = sharedPreferences.getString("NOM_UTILISATEUR","user@example.com") ?: "user@example.com",
        motDePasse = sharedPreferences.getString("MOT_DE_PASSE", "password123") ?: "password123",
        estVerifie = sharedPreferences.getBoolean ("UTILISATEUR_VERIFIE", false)
    )

    fun verifierUtilisateur(nomUtilisateur: String, motDePasse: String): Boolean {
        val estVerifie = (utilisateur.nomUtilisateur == nomUtilisateur) && (utilisateur.motDePasse == motDePasse)
        utilisateur.estVerifie = estVerifie

        sharedPreferences.edit().putBoolean("UTILISATEUR_VERIFIE", estVerifie).apply()
        return estVerifie
    }
    fun deconecterUtilisateur() {
        utilisateur.estVerifie = false
        sharedPreferences.edit().putBoolean("UTILISATEUR_VERIFIE", false).apply()
    }
    fun estUtilisateurVerifie(): Boolean {
        return utilisateur.estVerifie
    }

    private val _taches = mutableStateListOf<Tache>() // ViewModel
    val taches: List<Tache> get() = _taches // View

    // init avec l'aide de ChatGPT pour apporte les Taches déjà crée si existant.
    init {
        apporteDuPreferences()
    }


    private fun sauvegarderAuPreferences() {
        val jsonString = gson.toJson(_taches)
        sharedPreferences.edit().putString("PREF_KEY_TACHES", jsonString).apply()
    }
    private fun apporteDuPreferences() {
        val jsonString = sharedPreferences.getString("PREF_KEY_TACHES", null) ?: return
        val listType = object : TypeToken<List<Tache>>() {}.type
        val tacheSauves: List<Tache> = gson.fromJson(jsonString, listType)
        _taches.clear()
        _taches.addAll(tacheSauves)
    }

    // Avec l'aide du ChatGPT pour le bonne gestion de idTache
    fun ajouteTache(nomTache: String, descriptionTache: String) {
        val nouvelleIDTache = (taches.maxOfOrNull { it.idTache } ?: 0) + 1


        val nouvelleTache = Tache(
            idTache = nouvelleIDTache,
            nomTache = nomTache,
            descriptionTache = descriptionTache,
            estTerminee = false
        )
        _taches.add(nouvelleTache)
        sauvegarderAuPreferences()
    }

    // Pas trés contente avec fun modifieTache. On veut le simplifier.
    fun modifieTache (idTache: Int, nomTache: String, descriptionTache: String) {

        _taches.removeAll { it.idTache == idTache }
        sauvegarderAuPreferences()

        val nouvelleIDTache = (taches.maxOfOrNull { it.idTache } ?: 0) + 1

        val nouvelleTache = Tache(
            idTache = nouvelleIDTache,
            nomTache = nomTache,
            descriptionTache = descriptionTache,
            estTerminee = false
        )
        _taches.add(nouvelleTache)
        sauvegarderAuPreferences()
    }

    fun supprimeTache(idTache: Int) {
        _taches.removeAll { it.idTache == idTache }
        sauvegarderAuPreferences()
    }

    fun toggleTache(idTache: Int) {
        _taches.replaceAll { tache ->
            if (tache.idTache == idTache) tache.copy(estTerminee = !tache.estTerminee)
            else tache
        }
        sauvegarderAuPreferences()
    }
}