package com.example.chatgpt



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
   private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this is used for  reset the text from the edit text

        button_Another.setOnClickListener {
            etQuestion.text?.clear()
        }

        // this is the share button that will be share the text from the scrollView

        shareButton.setOnClickListener {
            val textToShare = txtResponse.text.toString()
            if (textToShare.isNotEmpty()) {
                shareText(textToShare)
            } else {
                Toast.makeText(this, "No text to share", Toast.LENGTH_SHORT).show()
            }
        }
        button_submit.setOnClickListener {
            val question = etQuestion.text.toString()
            Toast.makeText(this, question, Toast.LENGTH_SHORT).show()
            getResponse(question) { response ->
                runOnUiThread {
                    txtResponse.text = response


                }

            }
        }

        }

        fun getResponse(question: String, callback: (String) -> Unit) {

            val apiKey="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"
           // val url="https://api.openai.com/v1/engines/text-davinci-003/completions"
            val url= "https://api.openai.com/v1/completions"

            val requestBody="""
            {
             "model": "text-davinci-003",
            "prompt": "$question",
            "max_tokens": 500,
            "temperature": 0
            }
        """.trimIndent()

            val request = Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $apiKey")
                .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                .build()





            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                   Log.e("error","API failed")
                }

                override fun onResponse(call: Call, response: Response) {
                    val body=response.body?.string()
                    if(body!=null){
                      Log.v("data",body)
                    }
                    else{
                      Log.v("data","empty")
                    }
                    val jsonObject=JSONObject(body)
                    val jsonArray:JSONArray=jsonObject.getJSONArray("choices")
                    val textResult=jsonArray.getJSONObject(0).getString("text")
                    callback(textResult)

                }

            })


        }

        fun shareText(text: String) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Share via"))
        }
}
