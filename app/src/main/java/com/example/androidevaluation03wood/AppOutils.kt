package com.example.androidevaluation03wood

object AppOutils {

    fun apporteNomFichierUtilisateur(nomUtilisateur: String): String {
        var nomFichier = "Transactions_${nomUtilisateur}"
        return nomFichier
    }
}