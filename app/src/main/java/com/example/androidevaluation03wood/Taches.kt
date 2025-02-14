package com.example.androidevaluation03wood

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.collection.emptyLongSet
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



data class Tache(
    val idTache: Int,
    val nomTache: String,
    val descriptionTache: String,
    var estTerminee: Boolean
)

class Utilisateur(
    val nomEtPrenom: String = "Penny Counter",
    val nomUtilisateur: String = "user@example.com",
    val motDePasse: String = "password123",
    var estVerifie: Boolean = false,
)



//    val utilisateur = Utilisateur(
//        nomEtPrenom = "Penny Counter",
//        nomUtilisateur = "user@example.com",
//        motDePasse = "password123",
//        estVerifie = false
//    )


class ViewModelTaches (application: Application): AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AppPrefs", MODE_PRIVATE)
    private val gson = Gson()

    private val _utilisateur = Utilisateur(
        nomEtPrenom =  sharedPreferences.getString("NOM_ET_PRENOM","Penny Counter") ?: "Penny Counter",
        nomUtilisateur = sharedPreferences.getString("NOM_UTILISATEUR","user@example.com") ?: "user@example.com",
        motDePasse = sharedPreferences.getString("MOT_DE_PASSE", "password123") ?: "password123",
        estVerifie = sharedPreferences.getBoolean ("UTILISATEUR_VERIFIE", false),
//        tachesDuUtilisateur = sharedPreferences.getString("PREF_KEY_TACHES",
//            mutableStateListOf<Tache>().toString()
//        ) ?: mutableStateListOf<Tache>()
    )
    val utilisateur: Utilisateur get() = _utilisateur

 fun verifierUtilisateur(nomUtilisateur: String, motDePasse: String): Boolean {
        val estVerifie = (utilisateur.nomUtilisateur == nomUtilisateur) && (utilisateur.motDePasse == motDePasse)
//        utilisateur.estVerifie = estVerifie

        sharedPreferences.edit().putBoolean("UTILISATEUR_VERIFIE", estVerifie).apply()
        return estVerifie
    }
    fun deconecterUtilisateur(nomUtilisateur: String) {
        val nomFichier = apporteNomFichierUtilisateur(nomUtilisateur)
        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
        sharedPreferencesUtilisateur.edit().clear().apply()
        utilisateur.estVerifie = false

        sharedPreferences.edit().putBoolean("UTILISATEUR_VERIFIE", false).apply()
    }
    fun estUtilisateurVerifie(): Boolean {
        return utilisateur.estVerifie
    }
    private fun apporteNomFichierUtilisateur(nomUtilisateur: String): String {
        var nomFichier = "Taches_${nomUtilisateur}"
        return nomFichier
    }

    private val _taches = mutableStateListOf<Tache>() // ViewModel
    val taches: List<Tache> get() = _taches // View

    // init avec l'aide de ChatGPT pour apporte les Taches déjà crée si existant.
    init {
        apporteTaches(nomUtilisateur = utilisateur.nomUtilisateur)
    }


    fun sauvegarderTache(nomUtilisateur: String) {
        val nomFichier = apporteNomFichierUtilisateur(nomUtilisateur)
        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
        val jsonString = gson.toJson(_taches)
        val editor =
        sharedPreferencesUtilisateur.edit()
        editor.putString("PREF_KEY_TACHES", jsonString)
        editor.apply()
    }
    fun apporteTaches(nomUtilisateur: String) {
        val nomFichier = apporteNomFichierUtilisateur(nomUtilisateur)
        val estVerifie = sharedPreferences.getBoolean("UTILISATEUR_VERIFIE", true)

        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
//        val jsonStringUtilisateur = sharedPreferences.getString("NOM_ET_PRENOM",null) ?: return
        val jsonString = sharedPreferencesUtilisateur.getString("PREF_KEY_TACHES", null) ?: return
        val listType = object : TypeToken<List<Tache>>() {}.type
        val tacheSauves: List<Tache> = gson.fromJson(jsonString, listType)
        _taches.clear()
        estUtilisateurVerifie()
        if (estVerifie) {
            _taches.addAll(tacheSauves)
        } else {
            _taches.clear()
//            makeText(LocalContext,"L'utilisateur n'est pas verifié", Toast.LENGTH_SHORT).show()
        }
    }

 fun ajouteTache(nomTache: String, descriptionTache: String) {
        val nouvelleIDTache = (taches.maxOfOrNull { it.idTache } ?: 0) + 1


        val nouvelleTache = Tache(
            idTache = nouvelleIDTache,
            nomTache = nomTache,
            descriptionTache = descriptionTache,
            estTerminee = false
        )
        _taches.add(nouvelleTache)
        sauvegarderTache(nomUtilisateur = utilisateur.nomUtilisateur)
    }

    // Pas trés contente avec fun modifieTache. On veut le simplifier.
    fun modifieTache (idTache: Int, nomTache: String, descriptionTache: String) {

        _taches.removeAll { it.idTache == idTache }
        sauvegarderTache(nomUtilisateur = utilisateur.nomUtilisateur)

        val nouvelleIDTache = (taches.maxOfOrNull { it.idTache } ?: 0) + 1

        val nouvelleTache = Tache(
            idTache = nouvelleIDTache,
            nomTache = nomTache,
            descriptionTache = descriptionTache,
            estTerminee = false
        )
        _taches.add(nouvelleTache)
        sauvegarderTache(nomUtilisateur = utilisateur.nomUtilisateur)
    }

    fun supprimeTache(idTache: Int) {
        _taches.removeAll { it.idTache == idTache }
        sauvegarderTache(nomUtilisateur = utilisateur.nomUtilisateur)
    }

    fun toggleTache(idTache: Int) {
        _taches.replaceAll { tache ->
            if (tache.idTache == idTache) tache.copy(estTerminee = !tache.estTerminee)
            else tache
        }
        sauvegarderTache(nomUtilisateur = utilisateur.nomUtilisateur)
    }
}