package fi.infinitygrow.lainayhdistajalaskuri

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}