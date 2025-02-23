package com.example.androidevaluation03wood.Models

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.ui.input.key.type
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID


data class Transaction(
    val idTransaction: String,
    val selectedOption: String,
    val montant: Double = 0.00,
    val categorieTransaction: String
)

class ViewModelTransactionsFactory(
    private val application: Application,
    private val viewModelUtilisateur: ViewModelUtilisateur
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelTransactions::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelTransactions(application, viewModelUtilisateur) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ViewModelTransactions(
    application: Application,
    private val viewModelUtilisateur: ViewModelUtilisateur
) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AppPrefs", MODE_PRIVATE)
    private val gson = Gson()

// val _utilisateur = Utilisateur(
//        nomEtPrenom = sharedPreferences.getString("NOM_ET_PRENOM", "Penny Counter")
//            ?: "Penny Counter",
//        nomUtilisateur = sharedPreferences.getString("NOM_UTILISATEUR", "user@example.com")
//            ?: "user@example.com",
//        motDePasse = sharedPreferences.getString("MOT_DE_PASSE", "password123") ?: "password123",
//        estVerifie = sharedPreferences.getBoolean("UTILISATEUR_VERIFIE", false)
//    )
//    val utilisateur: Utilisateur get() = _utilisateur

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList()) // ViewModel
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow() // View

    
    init {
        apporteTransactions(nomUtilisateur = viewModelUtilisateur.utilisateur.nomUtilisateur)
    }

    fun sauvegarderTransaction(nomUtilisateur: String) {
        val nomFichier = AppOutils.apporteNomFichierUtilisateur(nomUtilisateur)
        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
        val jsonString = gson.toJson(_transactions)
        val editor =
        sharedPreferencesUtilisateur.edit()
        editor.putString("PREF_KEY_TRANSACTIONS", jsonString)
        editor.apply()
    }
    fun apporteTransactions(nomUtilisateur: String) {
        val nomFichier = AppOutils.apporteNomFichierUtilisateur(nomUtilisateur)
        val estVerifie = viewModelUtilisateur.utilisateur.estVerifie // Use the correct estVerifie

        val sharedPreferencesUtilisateur = getApplication<Application>().getSharedPreferences(nomFichier, Context.MODE_PRIVATE)
        val jsonString = sharedPreferencesUtilisateur.getString("PREF_KEY_TRANSACTIONS", null) ?: return
        val listType = object : TypeToken<List<Transaction>>() {}.type
        val transactionsSauves: List<Transaction> = gson.fromJson(jsonString, listType)

        _transactions.value = if (estVerifie) {
            transactionsSauves
        } else {
            emptyList()
        }
    }

// fun ajouteTransaction(montant: Double, categorieTransaction: String) {
//     private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
//     val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    fun ajouteTransaction(selectedOption: String, montant: Double, categorieTransaction: String) {

        val nouvelleTransaction = Transaction(
            UUID.randomUUID().toString(),
            selectedOption,
            montant,
            categorieTransaction,
        )
        _transactions.value = _transactions.value + nouvelleTransaction
        sauvegarderTransaction(nomUtilisateur = viewModelUtilisateur.utilisateur.nomUtilisateur)
    }

    fun modifieTransaction(idTransaction: String, selectedOption: String,montant: Double, categorieTransaction: String) {
        _transactions.value = _transactions.value.map {
            if (it.idTransaction == idTransaction) it.copy(selectedOption= selectedOption, montant = montant, categorieTransaction = categorieTransaction)
            else it
        }
        sauvegarderTransaction(nomUtilisateur = viewModelUtilisateur.utilisateur.nomUtilisateur)
    }

    fun supprimeTransaction(idTransaction: String) {
        _transactions.value = _transactions.value.filterNot { it.idTransaction == idTransaction }
        sauvegarderTransaction(nomUtilisateur = viewModelUtilisateur.utilisateur.nomUtilisateur)
    }
}