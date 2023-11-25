import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.synder.R
import com.example.synder.R.raw.sendtext

class SoundEffectPlayer(private val context: Context) {
    private val soundPool: SoundPool
    private var send: Int = 0
    private var openkeyboard: Int = 1
    private var closekeyboard: Int = 2
    private var type: Int = 3

    private var lastTypingTime = 0L
    private var lastPlayTime = System.currentTimeMillis()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(7)
            .setAudioAttributes(audioAttributes)
            .build()

        // Bytt ut 'your_sound_file' med navnet på lydfilen i 'res/raw'
        send = soundPool.load(context, R.raw.sendtext, 1)
        openkeyboard = soundPool.load(context, R.raw.openkeyboard, 1)
        closekeyboard = soundPool.load(context, R.raw.closekeyboard, 1)
        type = soundPool.load(context, R.raw.typing, 1)
    }

    fun playSend() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPlayTime > 300) {
            soundPool.play(send, 1f, 1f, 0, 0, 1f)
            lastPlayTime = currentTime
        }
    }
    fun playOpenKeyboard() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPlayTime > 300) {
            soundPool.play(openkeyboard, 1f, 1f, 0, 0, 1f)
            lastPlayTime = currentTime
        }
    }
    fun playCloseKeyboard() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPlayTime > 300) {
            soundPool.play(closekeyboard, 1f, 1f, 0, 0, 1f)
            lastPlayTime = currentTime
        }
    }
    fun playTyping() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTypingTime > 500) {
            soundPool.play(type, 1f, 1f, 0, 0, 1f)
            lastTypingTime = currentTime
        }
    }

    fun release() {
        soundPool.release()
    }
}
