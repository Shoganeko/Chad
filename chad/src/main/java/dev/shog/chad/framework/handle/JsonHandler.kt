package dev.shog.chad.framework.handle

import com.google.common.base.Charsets
import com.google.common.io.Files.asCharSink
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.apache.commons.lang3.SystemUtils
import dev.shog.chad.core.getLogger
import dev.shog.chad.framework.util.Util
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Handles all web and local JSON content
 *
 * @author sho
 */
object JsonHandler {
    /**
     * Creates files
     */
    private fun execFiles(linux: Boolean) {
        val directory = if (linux)
            File("/home/" + System.getProperty("user.name") + "/chad")
        else File(System.getenv("appdata") + "\\chad")

        if (!directory.exists() && !directory.mkdirs()) {
            getLogger().error("There was an error making the Chad directory.")
            exitProcess(1)
        }

        botLocation = if (linux) File(directory.path + "/bot.json") else File(directory.path + "\\bot.json")

        if (!botLocation.exists()) {
            if (!botLocation.createNewFile()) {
                getLogger().error("There was an error creating the bot.json file.")
                exitProcess(1)
            }

            val obj = org.json.JSONObject()
            obj.put("token", "")
            obj.put("steam_api_key", "")
            obj.put("uri_link", "")
            obj.put("youtube_api_key", "")
            obj.put("jdbc", "")
            obj.put("secret", "")
            obj.put("id", "")

            try {
                FileWriter(botLocation).use { fileWriter ->
                    fileWriter.write(obj.toString())
                    fileWriter.flush()
                }
            } catch (e: IOException) {
                getLogger().error("There was an issue creating files at startup!", e)
            }
        }

        getLogger().debug("Using path for bot.json ${botLocation.path}")
    }

    /**
     * Makes sure all of the files exist
     *
     * @return The handle
     */
    @Synchronized
    fun forceCheck(): JsonHandler {
        when (SystemUtils.OS_NAME.toUpperCase()) {
            "WINDOWS 10" -> {
                getLogger().debug("Checking files as if OS is Windows 10")
                execFiles(false)
            }

            "LINUX" -> {
                getLogger().debug("Checking files as if OS is Linux")
                execFiles(true)
            }

            else -> {
                getLogger().error("Chad cannot run on this Operating System!")
                exitProcess(1)
            }
        }
        return this
    }

    /**
     * Get an entry from the bot.json file
     *
     * @param entry The object to get
     * @return The retrieved object
     */
    @Synchronized
    operator fun get(entry: String): String {
        try {
            val jsonObject = org.json.JSONObject(String(botLocation.inputStream().readBytes()))
            return jsonObject.getString(entry)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * Sets an object in the bot.json file
     *
     * @param object The object to set
     * @param input The input to set the object to
     * @throws IOException With the Files.toString()
     */
    @Synchronized
    @Throws(IOException::class)
    operator fun set(`object`: String, input: String) {
        val jsonString = String(botLocation.inputStream().readBytes())
        val jElement = JsonParser().parse(jsonString)
        val jObject = jElement.asJsonObject
        jObject.addProperty(`object`, input)
        val gson = Gson()

        val resultingJson = gson.toJson(jElement)
        asCharSink(botLocation, Charsets.UTF_8).write(resultingJson)
    }

    /**
     * Reads a JSONObject from a URL
     *
     * @param url The URL to read from
     * @return The JSONObject
     */
    fun read(url: String?): org.json.JSONObject? {
        if (url.isNullOrEmpty()) return null
        val httpGet = Util.httpGet(url)
        return if (httpGet.isEmpty()) null else org.json.JSONObject(httpGet)
    }

    /**
     * Reads a JSONArray from a URL
     *
     * @param url The URL to read from
     * @return The JSONArray
     */
    fun readArray(url: String?): org.json.JSONArray? {
        if (url.isNullOrEmpty()) return null
        val httpGet = Util.httpGet(url)
        return if (httpGet.isEmpty()) null else org.json.JSONArray(httpGet)
    }

    /**
     * Reads a local file into a JSONObject
     *
     * @param file The file to read from
     * @return The JSONObject
     */
    @Synchronized
    fun readFile(file: String): org.json.JSONObject? = org.json.JSONObject(String(file.byteInputStream().readBytes()))

    /**
     * The location of the Chad directory.
     */
    private var botLocation: File = File("")
}