package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    lateinit var tvCharCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        tvCharCount = findViewById(R.id.tvCharCount)

        //val etValue = findViewById(R.id.etValue) as EditText

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                val charsRemaining = 280 - s.length
                tvCharCount.setText(charsRemaining.toString() + " characters remaining")
                if(charsRemaining <= 0) {
                    tvCharCount.setTextColor(-65536)
                    btnTweet.setEnabled(false)
                }
                else {
                    tvCharCount.setTextColor(-7829368)
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                //tvCharCount.setText(s.toString())
            }
        })

        //handling the user's click on the tweet button
        btnTweet.setOnClickListener {

            // Grab the content of edit text (etCompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet is not empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed", Toast.LENGTH_SHORT).show()
                // Look into displaying SnackBar message
            }
            // 2. Make sure the tweet is under character count
            else if (tweetContent.length > 280) {
                Toast.makeText(
                    this,
                    "Tweet is too long! Limit is 280 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Make an api call to Twitter to publish tweet
                // Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                    Log.i(TAG, "Successfully published tweet!")
                    // Send the tweet back to TimeLineActivity

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet",throwable)
                    }
                })
            }
        }
    }

    companion object {
        val TAG = "ComposeActivity"
    }
}
