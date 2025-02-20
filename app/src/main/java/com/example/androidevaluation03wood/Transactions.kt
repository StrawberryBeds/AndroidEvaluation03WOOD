package com.example.androidevaluation03wood

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class Transaction(
    val idTransaction: Int,
    val nomTransaction: String,
    val descriptionTransaction: String,
    var estTerminee: Boolean
)

class ViewModelTransactions (application: Application): AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AppPrefs", MODE_PRIVATE)
    private val gson = Gson()

    private val _utilisateur = Utilisateur(
        nomEtPrenom = sharedPreferences.getString("NOM_ET_PRENOM", "Penny Counter")
            ?: "Penny Counter",
        nomUtilisateur = sharedPreferences.getString("NOM_UTILISATEUR", "user@example.com")
            ?: "user@example.com",
        motDePasse = sharedPreferences.getString("MOT_DE_PASSE", "password123") ?: "password123",
        estVerifie = sharedPreferences.getBoolean("UTILISATEUR_VERIFIE", false)
    )
    val utilisateur: Utilisateur get() = _utilisateur

    private val _transactions = mutableStateListOf<Transaction>() // ViewModel
    val transactions: List<Transaction> get() = _transactions // View
    
    init {
        apporteTransactions(nomUtilisateur = utilisateur.nomUtilisateur)
    }

//    fun apporteNomFichierUtilisateur(nomUtilisateur: String): String {
//        var nomFichier = "Transactions_${nomUtilisateur}"
//        return nomFichier
//    }

    fun sauvegarderTransaction(nomUtilisateur: String) {
        val nomFichier = AppOutils.apporteNomFichierUtilisateur(nomUtilisateur)
        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
        val jsonString = gson.toJson(_transactions)
        val editor =
        sharedPreferencesUtilisateur.edit()
        editor.putString("PREF_KEY_TACHES", jsonString)
        editor.apply()
    }
    fun apporteTransactions(nomUtilisateur: String) {
        val nomFichier = AppOutils.apporteNomFichierUtilisateur(nomUtilisateur)
        val estVerifie = sharedPreferences.getBoolean("UTILISATEUR_VERIFIE", true)

        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
//        val jsonStringUtilisateur = sharedPreferences.getString("NOM_ET_PRENOM",null) ?: return
        val jsonString = sharedPreferencesUtilisateur.getString("PREF_KEY_TACHES", null) ?: return
        val listType = object : TypeToken<List<Transaction>>() {}.type
        val tacheSauves: List<Transaction> = gson.fromJson(jsonString, listType)
        _transactions.clear()
        estVerifie
        if (estVerifie) {
            _transactions.addAll(tacheSauves)
        } else {
            _transactions.clear()
//            makeText(LocalContext,"L'utilisateur n'est pas verifié", Toast.LENGTH_SHORT).show()
        }
    }

 fun ajouteTransaction(nomTransaction: String, descriptionTransaction: String) {
        val nouvelleIDTransaction = (transactions.maxOfOrNull { it.idTransaction } ?: 0) + 1


        val nouvelleTransaction = Transaction(
            idTransaction = nouvelleIDTransaction,
            nomTransaction = nomTransaction,
            descriptionTransaction = descriptionTransaction,
            estTerminee = false
        )
        _transactions.add(nouvelleTransaction)
        sauvegarderTransaction(nomUtilisateur = utilisateur.nomUtilisateur)
    }

    // Pas trés contente avec fun modifieTransaction. On veut le simplifier.
    fun modifieTransaction (idTransaction: Int, nomTransaction: String, descriptionTransaction: String) {

        _transactions.removeAll { it.idTransaction == idTransaction }
        sauvegarderTransaction(nomUtilisateur = utilisateur.nomUtilisateur)

        val nouvelleIDTransaction = (transactions.maxOfOrNull { it.idTransaction } ?: 0) + 1

        val nouvelleTransaction = Transaction(
            idTransaction = nouvelleIDTransaction,
            nomTransaction = nomTransaction,
            descriptionTransaction = descriptionTransaction,
            estTerminee = false
        )
        _transactions.add(nouvelleTransaction)
        sauvegarderTransaction(nomUtilisateur = utilisateur.nomUtilisateur)
    }

    fun supprimeTransaction(idTransaction: Int) {
        _transactions.removeAll { it.idTransaction == idTransaction }
        sauvegarderTransaction(nomUtilisateur = utilisateur.nomUtilisateur)
    }

    fun toggleTransaction(idTransaction: Int) {
        _transactions.replaceAll { tache ->
            if (tache.idTransaction == idTransaction) tache.copy(estTerminee = !tache.estTerminee)
            else tache
        }
        sauvegarderTransaction(nomUtilisateur = utilisateur.nomUtilisateur)
    }
}