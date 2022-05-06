package com.example.classlib
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class RetainedFragment : Fragment() {

    companion object {
        internal const val TAG = "RetainedFragment"
    }

    private var mListener: OnFragmentInteractionListener? = null
    internal var data: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    internal fun onButtonPressed(isbn1 : String) {
        HttpGetTask(this, isbn1).execute()
    }

    private fun onDownloadFinished(result: List<String>) {
        data = result
        mListener?.onDownloadfinished()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onDownloadfinished()
    }

    internal class HttpGetTask(retainedFragment: RetainedFragment, isbn: String) :
        AsyncTask<Void, Void, List<String>>() {
        private var isbn1 = isbn

        companion object {

            private const val TAG = "HttpGetTask"
            private const val URL =
                ("https://atlas-fab.lexile.com/free/books/")
            const val ISBN_TAG = "canonical_isbn"
            const val AUTHORS_TAG = "authors"
            const val CATEGORIES_TAG = "categories"
            const val MEASUREMENTS_TAG = "measurements"
            const val LEXILE_TAG = "lexile"
            const val MIN_AGE_TAG = "min_age"
            const val MAX_AGE_TAG = "max_age"

        }

        private val mListener: WeakReference<RetainedFragment> = WeakReference(retainedFragment)

        override fun doInBackground(vararg params: Void): List<String> {

            var httpUrlConnection: HttpURLConnection? = null
            var data: String? = null

            try {
                // 1. Get connection. 2. Prepare request (URI)
                httpUrlConnection = URL(URL+isbn1)
                    .openConnection() as HttpURLConnection

                // 3. This app does not use a request body
                httpUrlConnection.setRequestProperty("Accept", "application/json; version=1.0")
                // 4. Read the response
                Log.i(TAG,httpUrlConnection.responseCode.toString())
                Log.i(TAG,httpUrlConnection.responseMessage.toString())
                Log.i(TAG,"before stream")
                if(httpUrlConnection.responseCode == HttpURLConnection.HTTP_OK){
                    val inputstream = BufferedInputStream(
                        httpUrlConnection.inputStream
                    )
                    Log.i(TAG,"before read")
                    data = readStream(inputstream)
                }

            } catch (exception: MalformedURLException) {
                Log.e(TAG, "MalformedURLException")
            } catch (exception: IOException) {
                Log.e(TAG, "IOException")
            } finally {
                httpUrlConnection?.disconnect()
            }
            // Parse the JSON-formatted response
            return parseJsonString(data)
        }


        override fun onPostExecute(result: List<String>) {
            Log.i("background", "here2")
            if (null != mListener.get()) {
                mListener.get()?.onDownloadFinished(result)
            }
        }

        private fun readStream(inputStream: InputStream): String {
            Log.i("background", "here3")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sep = System.getProperty("line.separator")
            val data = StringBuilder()
            try {
                reader.forEachLine {
                    Log.i(TAG, "Reading from socket")
                    data.append(it + sep)
                }
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            } finally {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Log.e(TAG, e.toString())
                }
            }
            return data.toString()
        }

        private fun parseJsonString(data: String?): List<String> {
            Log.i("background", "here4")
            val result =  ArrayList<String>()

            try {
                // Get top-level JSON Object - a Map
                val responseObject = JSONTokener(
                    data
                ).nextValue() as JSONObject
                val work = responseObject
                    .getJSONObject("data").getJSONObject("work")
                result.add(work.toString())

            } catch (e: Throwable) {
                return result
            }
            return result
        }
    }
}
