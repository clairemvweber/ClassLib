package com.example.classlib
import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.content.Intent
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class NetworkingJSONResponseActivity : FragmentActivity(),
    RetainedFragment.OnFragmentInteractionListener {

    private lateinit var mRetainedFragment: RetainedFragment
    private lateinit var mListView: ListView

    companion object {

        const val ISBN_TAG = "canonical_isbn"
        const val TITLE_TAG = "title"
        const val AUTHORS_TAG = "authors"
        const val CATEGORIES_TAG = "categories"
        const val MEASUREMENTS_TAG = "measurements"
        const val LEXILE_TAG = "lexile"
        const val MIN_AGE_TAG = "min_age"
        const val MAX_AGE_TAG = "max_age"

    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_json_response)

//        mListView = findViewById(R.id.list)

        if (null != savedInstanceState) {
            mRetainedFragment = supportFragmentManager
                .findFragmentByTag(RetainedFragment.TAG) as RetainedFragment
            onDownloadfinished()
        } else {

            mRetainedFragment = RetainedFragment()
            supportFragmentManager.beginTransaction()
                .add(mRetainedFragment, RetainedFragment.TAG)
                .commit()
            mRetainedFragment.onButtonPressed(intent.getStringExtra("isbn")!!)
        }
    }

    override fun onDownloadfinished() {
        mRetainedFragment.data?.let {
            val book = JSONTokener(mRetainedFragment.data!![0]).nextValue() as JSONObject
            Log.i("Download fin", mRetainedFragment.data.toString())
            val intent =  Intent()
            intent.putExtra(TITLE_TAG, book.getString(TITLE_TAG))
            intent.putExtra(ISBN_TAG, book.getString(ISBN_TAG))
            intent.putExtra(AUTHORS_TAG, book.getJSONArray(AUTHORS_TAG).get(0).toString())
            intent.putExtra(CATEGORIES_TAG, book.getJSONArray(CATEGORIES_TAG).get(0).toString())
            intent.putExtra(LEXILE_TAG,  book.getJSONObject(MEASUREMENTS_TAG).getJSONObject("english").getString(LEXILE_TAG))
            intent.putExtra(MIN_AGE_TAG, book.getString(MIN_AGE_TAG))
            intent.putExtra(MAX_AGE_TAG, book.getString(MAX_AGE_TAG))
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}