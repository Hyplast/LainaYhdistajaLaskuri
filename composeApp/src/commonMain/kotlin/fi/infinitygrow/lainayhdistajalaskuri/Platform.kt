package fi.infinitygrow.lainayhdistajalaskuri

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform