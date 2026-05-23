package dam

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class AIAssistantNvidiaClasses(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "NVIDIA"
    override val apiKeyName = "NVIDIA_API_KEY"
    var newTemperature: Double? = null


    override var model = "meta/llama-3.3-80b-instruct"

    data class NvidiaRequest(
        val model: String = "meta/llama-4-maverick-17b-128e-instruct",
        val messages: List<Message>,
        val maxTokens: Int? = 512,
        val temperature: Double? = 1.0,
        val top_p: Double? = 1.0,
        val frequency_penalty: Double? = 0.0,
        val presence_penalty: Double? = 0.0,
        val stream: Boolean = false,
        val extraBody: Map<String, Any>? = null
    )

    data class Message(
        val role: String = "user",
        val content: String
    )
    // Gson instance for JSON serialization
    private val gson = Gson()

    override fun buildRequest(prompt: String): Request {
        // Create request structure using data classes
        val temp = newTemperature?:temperature
        val nvidiaRequest = NvidiaRequest(
            messages = listOf(Message(
                content = prompt
            )),
            top_p = 1.0,
            maxTokens = maxTokens,
            temperature = temp,
            frequency_penalty = 0.0,
            presence_penalty = 0.0,
            stream = false
        )

        // Convert to JSON string using Gson
        val requestBody = gson.toJson(nvidiaRequest)

        println("DEBUG temperature: $temp")

        // Configure the HTTP request with proper headers and authentication
        val request = Request.Builder()
            .url("https://integrate.api.nvidia.com/v1/chat/completions") // NVIDIA endpoint
            .addHeader("Authorization", "Bearer $apiKey") // Standard OAuth Bearer token
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return request
    }
}