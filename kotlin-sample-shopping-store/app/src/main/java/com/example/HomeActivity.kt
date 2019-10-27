package com.example

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.MKStoreItem
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.collections.LinkedHashMap


class HomeActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    val storeItems= mutableListOf<MKStoreItem>()
    var adapter: MKStoreAdapter?=null
    var txtKartValue: TextView?=null
    val kartItems=LinkedHashMap<Int, MKStoreItem>();
    private var tts: TextToSpeech? = null


    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


    fun post(body: String): String {
//        var reqParam = URLEncoder.encode(body, "UTF-8")
        var reqParam = body
        val mURL = URL("http://192.168.43.214:8080/api/commands/check")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"
            setRequestProperty("Content-Type", "text/plain")

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(reqParam);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")
                return response.toString()
            }
        }
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("Text")
//            if (message.contains("FAILED") { // }

            Log.i("LLOLOLOLOL", message)
            Log.i("LLOLOLOLOL", adapter?.storeItems.toString())
            Log.i("LLOLOLOLOL", message.split("\n")[0])


            Thread {
                var result = post(message.split("\n")[0]).replace("+", " ").replace("=", "").toLowerCase()
//                var result = "add item a"
                runOnUiThread {
                    if (result.contains("filter")) {
                        filter?.performClick()
                    }
                    if (result.contains("add")) {
                        for (item in adapter!!.storeItems) {
                            Log.i("LLOLOLOLOL", item.name)
                            if (result.contains(item.name)) {
                                Log.i("LLOLOLOLOL", "HERE")
                                speakOut(item.description)
                                item.button?.performClick()
                            }
                        }
                    }
//                    if (result.containts)
                }
            }.start()



//            for (lol in adapter!!.storeItems) {
//                Log.i("LLOLOLOLOL", "LOLOLO")
//                if (lol.name == "item 1") {
//                    lol.button?.performClick();
//                    Log.i("LLOLOLOLOL", "HERE")
//
//                }
//
//            }
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.title="MKStore"
        tts = TextToSpeech(this, this)
        setupStoreData()
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, IntentFilter("TextUpdates"))
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                Log.e("TTS","The GOOD!")
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun setupStoreData(){
        var index = 0
        for (i in 'a'..'z'){
            storeItems.add(MKStoreItem("item $i", "description $i", index, 30.50f+index,0, false))
            index+=1
        }
        adapter= MKStoreAdapter(this, storeItems);
        recyclerView.layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView.adapter=adapter
        adapter?.itemAddRemoveListner=object :OnItemAddRemoveListener{
            override fun onAddRemove(item: MKStoreItem) {
                when(item.isSelected){
                    true ->kartItems.put(item.id, item)
                    false -> kartItems.remove(item.id)
                }
                if(kartItems.size > 0){
                    txtKartValue?.visibility=View.VISIBLE
                    txtKartValue?.text=kartItems.size.toString();
                }else{
                    txtKartValue?.visibility=View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItem: MenuItem=menu!!.findItem(R.id.action_kart)
        var kartView: View=menuItem.actionView;
        var flKart=kartView.findViewById<FrameLayout>(R.id.flKart)
        txtKartValue= kartView.findViewById(R.id.txtKartValue) as TextView?
        flKart.setOnClickListener{
            calculateKartItems()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_kart -> calculateKartItems()
        }
        return true
    }

    private fun calculateKartItems(){
        if(kartItems.size > 0){
            var intent= Intent(this, KartDetailActivity::class.java)
            intent.putExtra("Data", kartItems)
            startActivity(intent)
        }
    }

}
