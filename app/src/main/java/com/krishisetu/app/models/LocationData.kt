package com.krishisetu.app.models

data class LocationHierarchy(
    val state: String,
    val districts: List<DistrictData>
)

data class DistrictData(
    val name: String,
    val talukas: List<TalukaData>
)

data class TalukaData(
    val name: String,
    val villages: List<String>
)

object MockLocationRepository {
    val locations: List<LocationHierarchy> = listOf(
        LocationHierarchy(
            state = "Maharashtra",
            districts = listOf(
                DistrictData(
                    name = "Satara",
                    talukas = listOf(
                        TalukaData(
                            name = "Satara",
                            villages = listOf("Khed", "Padli", "Karanje", "Varye", "Shahupuri")
                        ),
                        TalukaData(
                            name = "Karad",
                            villages = listOf("Malkapur", "Saidapur", "Oundh", "Helwak", "Kale")
                        ),
                        TalukaData(
                            name = "Wai",
                            villages = listOf("Panchgani", "Dhom", "Bhuinj", "Bawdhan", "Veldur")
                        ),
                        TalukaData(
                            name = "Phaltan",
                            villages = listOf("Surwadi", "Taradgaon", "Rajale", "Nimboni")
                        ),
                        TalukaData(
                            name = "Koregaon",
                            villages = listOf("Rahimatpur", "Kumthe", "Kinhi", "Loni")
                        )
                    )
                ),
                DistrictData(
                    name = "Pune",
                    talukas = listOf(
                        TalukaData(
                            name = "Haveli",
                            villages = listOf("Khadakwasla", "Wagholi", "Loni Kalbhor", "Manjari", "Uruli Kanchan")
                        ),
                        TalukaData(
                            name = "Baramati",
                            villages = listOf("Malegaon", "Bhavaninagar", "Jalochi", "Katewadi", "Rui")
                        ),
                        TalukaData(
                            name = "Shirur",
                            villages = listOf("Shikrapur", "Sanaswadi", "Nabalabad", "Ranjangaon")
                        ),
                        TalukaData(
                            name = "Junnar",
                            villages = listOf("Narayangaon", "Ozar", "Alephata", "Otur")
                        )
                    )
                ),
                DistrictData(
                    name = "Nashik",
                    talukas = listOf(
                        TalukaData(
                            name = "Niphad",
                            villages = listOf("Lasalgaon", "Pimpalgaon Baswant", "Saikheda", "Ugar")
                        ),
                        TalukaData(
                            name = "Dindori",
                            villages = listOf("Vani", "Janori", "Mohadi", "Khedgaon")
                        ),
                        TalukaData(
                            name = "Malegaon",
                            villages = listOf("Camp", "Umrane", "Dyane", "Saujanya")
                        )
                    )
                ),
                DistrictData(
                    name = "Kolhapur",
                    talukas = listOf(
                        TalukaData(
                            name = "Hatkanangale",
                            villages = listOf("Ichalkaranji", "Vadgaon", "Shiroli", "Alte")
                        ),
                        TalukaData(
                            name = "Shirol",
                            villages = listOf("Jaysingpur", "Kurundwad", "Umalwad", "Narsingwadi")
                        ),
                        TalukaData(
                            name = "Karveer",
                            villages = listOf("Bapat Camp", "Uchgaon", "Kalamba", "Koparde")
                        )
                    )
                )
            )
        ),
        LocationHierarchy(
            state = "Karnataka",
            districts = listOf(
                DistrictData(
                    name = "Belagavi",
                    talukas = listOf(
                        TalukaData(
                            name = "Chikodi",
                            villages = listOf("Nipani", "Sankeshwar", "Bedkihal", "Koganolli")
                        ),
                        TalukaData(
                            name = "Gokak",
                            villages = listOf("Konnur", "Ghataprabha", "Arabhavi", "Ankalgi")
                        )
                    )
                ),
                DistrictData(
                    name = "Dharwad",
                    talukas = listOf(
                        TalukaData(
                            name = "Hubballi",
                            villages = listOf("Unkal", "Amargol", "Gopankoppa", "Tarihal")
                        )
                    )
                )
            )
        ),
        LocationHierarchy(
            state = "Gujarat",
            districts = listOf(
                DistrictData(
                    name = "Surat",
                    talukas = listOf(
                        TalukaData(
                            name = "Olpad",
                            villages = listOf("Sayan", "Karasada", "Kim", "Karanj")
                        ),
                        TalukaData(
                            name = "Kamrej",
                            villages = listOf("Valthan", "Kathor", "Digas", "Pasodara")
                        )
                    )
                )
            )
        )
    )
}
