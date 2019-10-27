package com.example

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

import java.util.ArrayList
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MyService : Service(), RecognitionListener {
    private var returnedText: String? = null
    private var toggleButton: ToggleButton? = null
    private var progressBar: ProgressBar? = null
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private val LOG_TAG = "VoiceRecognition"
    private var wait: Boolean = false

    override fun onCreate() {
        super.onCreate()

        speech = SpeechRecognizer.createSpeechRecognizer(this)
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this))
        speech!!.setRecognitionListener(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en-US")
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "en-US")
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)

        speech!!.startListening(recognizerIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech")
    }

    override fun onBufferReceived(buffer: ByteArray) {
        Log.i(LOG_TAG, "onBufferReceived: $buffer")
    }

    override fun onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech")
    }

    override fun onError(errorCode: Int) {
        val errorMessage = getErrorText(errorCode)
        Log.d(LOG_TAG, "FAILED $errorMessage")
        returnedText = errorMessage

        speech!!.destroy()
        speech = null
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this))
        speech!!.setRecognitionListener(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en-US")
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "en-US")
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)

        speech!!.startListening(recognizerIntent)
    }

    override fun onEvent(arg0: Int, arg1: Bundle) {
        Log.i(LOG_TAG, "onEvent")
    }

    override fun onPartialResults(arg0: Bundle) {
        Log.i(LOG_TAG, "onPartialResults")
    }

    override fun onReadyForSpeech(arg0: Bundle) {
        Log.i(LOG_TAG, "onReadyForSpeech")
    }

    override fun onResults(results: Bundle) {
        Log.i(LOG_TAG, "onResults")
        val matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text = ""
        for (result in matches!!)
            text += result + "\n"

        returnedText = text
        Log.i(LOG_TAG, text)
        sendMessageToActivity(text)

//        wait = false
        speech!!.startListening(recognizerIntent)
    }

    private fun sendMessageToActivity(res: String) {
        val intent = Intent("TextUpdates")
        // You can also include some extra data.
        intent.putExtra("Text", res)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onRmsChanged(rmsdB: Float) {
//        if (rmsdB > 0)
            Log.i(LOG_TAG, "onRmsChanged: $rmsdB")
    }

    companion object {

        private val REQUEST_RECORD_PERMISSION = 100

        fun getErrorText(errorCode: Int): String {
            val message: String
            when (errorCode) {
                SpeechRecognizer.ERROR_AUDIO -> message = "Audio recording error"
                SpeechRecognizer.ERROR_CLIENT -> message = "Client side error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "Insufficient permissions"
                SpeechRecognizer.ERROR_NETWORK -> message = "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "Network timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> message = "No match"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RecognitionService busy"
                SpeechRecognizer.ERROR_SERVER -> message = "error from server"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "No speech input"
                else -> message = "Didn't understand, please try again."
            }
            return message
        }
    }
}


