package np.info.suraj.servicebinding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    public class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startBackgroundTask(intent, startId);
        return flags;
    }

    public void showToast() {
        Toast.makeText(this, "Some text!", Toast.LENGTH_LONG).show();
    }
}