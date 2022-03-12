package com.codepath.apps.restclienttemplate.models

import android.text.format.DateUtils
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Tweet {

    var body: String = ""
    var createdAt: String = ""
    var user: User? = null
    var timeStamp: String = ""

    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            tweet.timeStamp = getRelativeTimeAgo(tweet.createdAt)
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length())  {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

        // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
        fun getRelativeTimeAgo(rawJsonDate: String): String {
            val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
            val sf = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
            sf.setLenient(true)
            var relativeDate = ""
            try {
                val dateMillis: Long = sf.parse(rawJsonDate).getTime()
                relativeDate = DateUtils.getRelativeTimeSpanString(
                    dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
                ).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return relativeDate
        }
    }


}