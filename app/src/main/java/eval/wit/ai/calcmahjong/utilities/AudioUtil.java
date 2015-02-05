package eval.wit.ai.calcmahjong.utilities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;


/**
 * Created by koba on 2015/02/05.
 */
public class AudioUtil {

    public static void play(final MediaPlayer mp, final Context context, final String uri,
                            final MediaPlayer.OnPreparedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mp.reset();
                    mp.setDataSource(context, Uri.parse(uri));
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.prepareAsync();
                    mp.setOnPreparedListener(listener != null ? listener : new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (mp.isPlaying()) {
                                mp.stop();
                            }

                            mp.start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
