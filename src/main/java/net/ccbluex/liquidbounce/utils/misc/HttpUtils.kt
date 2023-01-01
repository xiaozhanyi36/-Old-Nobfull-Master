// Destiny made by ChengFeng

package net.ccbluex.liquidbounce.utils.misc

import com.google.common.io.ByteStreams
import net.ccbluex.liquidbounce.utils.ClientUtils
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
object HttpUtils {

    const val DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.7113.93 Safari/537.36 Java/1.8.0_191"

    init {
        HttpURLConnection.setFollowRedirects(true)
    }

    fun make(
        url: String,
        method: String,
        data: String = "",
        agent: String = DEFAULT_AGENT
    ): HttpURLConnection {
        val httpConnection = URL(url).openConnection() as HttpURLConnection

        httpConnection.requestMethod = method
        httpConnection.connectTimeout = 2000
        httpConnection.readTimeout = 10000

        httpConnection.setRequestProperty("User-Agent", agent)

        httpConnection.instanceFollowRedirects = true
        httpConnection.doOutput = true

        if (data.isNotEmpty()) {
            val dataOutputStream = DataOutputStream(httpConnection.outputStream)
            dataOutputStream.writeBytes(data)
            dataOutputStream.flush()
        }

        return httpConnection
    }

    fun request(
        url: String,
        method: String,
        data: String = "",
        agent: String = DEFAULT_AGENT
    ): String {
        val connection = make(url, method, data, agent)

        return connection.inputStream.reader().readText()
    }

    fun requestStream(
        url: String,
        method: String,
        agent: String = DEFAULT_AGENT
    ): InputStream? {
        val connection = make(url, method, agent)

        return connection.inputStream
    }

    @Throws(IOException::class)
    fun getRepo(url: String): String {
        val con = URL(url).openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.setRequestProperty("User-Agent", "Mozilla/5.0")
        val `in` = BufferedReader(InputStreamReader(con.inputStream))
        val response = StringBuilder()
        var inputLine: String?
        while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
            response.append("\n")
        }
        `in`.close()
        return response.toString()
    }

    fun download(url: String, file: File) {
        ClientUtils.logWarn("Downloading $url to ${file.absolutePath}")
        FileOutputStream(file).use { ByteStreams.copy(make(url, "GET").inputStream, it) }
    }

    @Throws(IOException::class)
    @JvmStatic
    fun getHttps(url: String): String {
        HackUtils.processHacker()
        val httpsConnection = URL(url).openConnection() as HttpsURLConnection

        httpsConnection.requestMethod = "GET"
        httpsConnection.connectTimeout = 10000
        httpsConnection.readTimeout = 10000

        httpsConnection.instanceFollowRedirects = true
        httpsConnection.doOutput = true

        val getter = httpsConnection.inputStream.reader().readText()
        HackUtils.revertHacker()
        return getter
    }

    fun get(url: String) = request(url, "GET")

    fun post(url: String, data: String) = request(url, "POST", data = data)
}
