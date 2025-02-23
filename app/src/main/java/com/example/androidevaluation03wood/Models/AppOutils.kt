package com.example.androidevaluation03wood.Models

// Ce object est apellé dans les classes ViewModelUtilisateur et ViewModelTransactions
// et il est sépraré pour éviter le dupliqué de code.

object AppOutils {

    fun apporteNomFichierUtilisateur(nomUtilisateur: String): String {
        var nomFichier = "Transactions_${nomUtilisateur}"
        return nomFichier
    }
}