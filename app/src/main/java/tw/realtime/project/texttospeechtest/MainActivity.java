package tw.realtime.project.texttospeechtest;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static int MAX_LEVEL = 20;
    private static int DEFAULT_LEVEL = 10;


    private EditText mTextInput;
    private SeekBar mPitchAdjustment;
    private SeekBar mSpeedAdjustment;

    private TextToSpeech mTextToSpeech;
    private boolean mHasTtsInitialized;


    private String getLogTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextToSpeech = new TextToSpeech(getApplicationContext(), new MyTextToSpeechInitCallback());

        mTextInput = (EditText) findViewById(R.id.textInput);

        mPitchAdjustment = (SeekBar) findViewById(R.id.pitchAdjustment);
        mPitchAdjustment.setProgress(DEFAULT_LEVEL);
        mPitchAdjustment.setMax(MAX_LEVEL);

        mSpeedAdjustment = (SeekBar) findViewById(R.id.speedAdjustment);
        mSpeedAdjustment.setProgress(DEFAULT_LEVEL);
        mSpeedAdjustment.setMax(MAX_LEVEL);

        View view = findViewById(R.id.proceedAction);
        if (null != view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);

                    proceedSpeakOut();

                    view.setEnabled(true);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTextToSpeech) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            mTextToSpeech = null;
        }
    }

    private class MyTextToSpeechInitCallback implements TextToSpeech.OnInitListener {
        @Override
        public void onInit(int status) {
            Log.i(getLogTag(), "OnInit - Status: " + status);

            if (isSuccessfulOperation(status)) {
                mPitchAdjustment.setOnSeekBarChangeListener(new PitchAdjustmentCallback());
                mSpeedAdjustment.setOnSeekBarChangeListener(new SpeedAdjustmentCallback());

                resultOfSetLanguageToTextToSpeech (mTextToSpeech.setLanguage(Locale.getDefault()));
            }
            else {
                exceptionOnTextToSpeech(status);
            }
        }
    }

    private boolean isSuccessfulOperation (int status) {
        return (status == TextToSpeech.SUCCESS);
    }

    private void exceptionOnTextToSpeech (int status) {
        switch (status) {
            //Denotes a generic operation failure.
            case TextToSpeech.ERROR:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR: generic operation failure");
                break;

            //Denotes a failure caused by an invalid request.
            case TextToSpeech.ERROR_INVALID_REQUEST:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_INVALID_REQUEST: failure due to invalid request");
                break;

            //Denotes a failure caused by a network connectivity problems.
            case TextToSpeech.ERROR_NETWORK:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_NETWORK: failure due to a network connectivity problems");
                break;

            //Denotes a failure caused by network timeout.
            case TextToSpeech.ERROR_NETWORK_TIMEOUT:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_NETWORK_TIMEOUT: failure due to network timeout");
                break;

            //Denotes a failure caused by an unfinished download of the voice data.
            case TextToSpeech.ERROR_NOT_INSTALLED_YET:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_NOT_INSTALLED_YET: failure due to an unfinished download of the voice data");
                break;

            //Denotes a failure related to the output (audio device or a file)
            case TextToSpeech.ERROR_OUTPUT:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_OUTPUT: failure due to output");
                break;

            //Denotes a failure of a TTS service
            case TextToSpeech.ERROR_SERVICE:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_SERVICE: failure due to a TTS service");
                break;

            //Denotes a failure of a TTS engine to synthesize the given input.
            case TextToSpeech.ERROR_SYNTHESIS:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - ERROR_SYNTHESIS: failure due to a TTS engine to synthesize the given input");
                break;

            //Queue mode where the new entry is added at the end of the playback queue.
            case TextToSpeech.QUEUE_ADD:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - QUEUE_ADD: the new entry is added at the end of the playback queue");
                break;

            //Queue mode where all entries in the playback queue (media to be played and text to be synthesized) are dropped and replaced by the new entry.
            case TextToSpeech.QUEUE_FLUSH:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - QUEUE_FLUSH: all entries in the playback queue are dropped and replaced by the new entry");
                break;

            //Denotes a stop requested by a client.
            case TextToSpeech.STOPPED:
                Log.w(getLogTag(), "exceptionOnTextToSpeech - STOPPED: a stop requested by a client");
                break;
        }
    }

    private void resultOfSetLanguageToTextToSpeech (int status) {
        Log.i(getLogTag(), "resultOfSetLanguageToTextToSpeech - Status: " + status);
        switch (status) {
            //Denotes the language is available for the language by the locale, but not the country and variant.
            case TextToSpeech.LANG_AVAILABLE:
                mHasTtsInitialized = true;
                Log.w(getLogTag(), "resultOfSetLanguageToTextToSpeech - LANG_AVAILABLE: failure due to a TTS engine to synthesize the given input");
                break;

            //Denotes the language is available for the language and country specified by the locale, but not the variant.
            case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                mHasTtsInitialized = true;
                Log.w(getLogTag(), "resultOfSetLanguageToTextToSpeech - LANG_COUNTRY_AVAILABLE: the language is available for the language and country specified by the locale, but not the variant");
                break;

            //Denotes the language is available exactly as specified by the locale.
            case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                mHasTtsInitialized = true;
                Log.w(getLogTag(), "resultOfSetLanguageToTextToSpeech - LANG_COUNTRY_VAR_AVAILABLE: the language is available exactly as specified by the locale");
                break;

            //Denotes the language data is missing.
            case TextToSpeech.LANG_MISSING_DATA:
                mHasTtsInitialized = false;
                Log.w(getLogTag(), "resultOfSetLanguageToTextToSpeech - LANG_MISSING_DATA: the language data is missing");
                break;

            //Denotes the language is not supported.
            case TextToSpeech.LANG_NOT_SUPPORTED:
                mHasTtsInitialized = false;
                Log.w(getLogTag(), "resultOfSetLanguageToTextToSpeech - LANG_NOT_SUPPORTED: the language is not supported");
                break;
        }
    }

    private void proceedSpeakOut () {
        if (!mHasTtsInitialized) {
            return;
        }
        String text = mTextInput.getText().toString().trim();
        if (text.isEmpty()) {
            return;
        }

        if (Build.VERSION.SDK_INT < 21) {
            if (!isSuccessfulOperation(mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null) ) ) {
                Log.w(getLogTag(), "proceedSpeakOut - speak: fail");
            }
        }
        else {
            if (!isSuccessfulOperation(mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) ) ) {
                Log.w(getLogTag(), "proceedSpeakOut - speak: fail");
            }
        }
    }

    private abstract class BaseAdjustmentCallback implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.i(getLogTag(), "onProgressChanged - progress: " + progress + ", isFromUser: " + fromUser);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d(getLogTag(), "onStartTrackingTouch");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(getLogTag(), "onStopTrackingTouch");
        }
    }

    private class PitchAdjustmentCallback extends BaseAdjustmentCallback {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            super.onProgressChanged(seekBar, progress, fromUser);

            // float: Speech pitch. 1.0 is the normal pitch,
            // lower values lower the tone of the synthesized voice, greater values increase it.
            float setting = ((float)progress) / ((float)DEFAULT_LEVEL);
            if (!isSuccessfulOperation(mTextToSpeech.setPitch(setting) ) ) {
                Log.w(getLogTag(), "PitchAdjustmentCallback - setPitch: fail");
            }
        }
    }

    private class SpeedAdjustmentCallback extends BaseAdjustmentCallback {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            super.onProgressChanged(seekBar, progress, fromUser);

            // float: Speech rate. 1.0 is the normal speech rate,
            // lower values slow down the speech (0.5 is half the normal speech rate),
            // greater values accelerate it (2.0 is twice the normal speech rate).
            float setting = ((float)progress) / ((float)DEFAULT_LEVEL);
            if (!isSuccessfulOperation(mTextToSpeech.setSpeechRate(setting) ) ) {
                Log.w(getLogTag(), "SpeedAdjustmentCallback - setSpeechRate: fail");
            }
        }
    }
}
