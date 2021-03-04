package com.connecttix.speedlimit.utils;

import android.speech.tts.TextToSpeech;

public class VozNotification {
    public static void notificationAudible(TextToSpeech textToVoice, String message){
        textToVoice.speak(message,
                TextToSpeech.QUEUE_FLUSH, null,null);
    }
}
