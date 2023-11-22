import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.synder.R
import com.example.synder.R.raw.sendtext

class SoundEffectPlayer(private val context: Context) {
    private val soundPool: SoundPool
    private var soundId: Int = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // Bytt ut 'your_sound_file' med navnet på lydfilen i 'res/raw'
        soundId = soundPool.load(context, R.raw.sendtext, 1)
    }

    fun playSound() {
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
