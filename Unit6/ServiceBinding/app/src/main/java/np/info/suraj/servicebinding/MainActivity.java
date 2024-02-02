package np.info.suraj.servicebinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MyService.MyServiceBinder binder = (MyService.MyServiceBinder) service;
        MyService myService = binder.getService();
        myService.showToast();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public void onBtnClick(View v) {
//        String input = "Some text input";
//        new MyAsynkTask().execute(input);

        this.backgroundExecution();
    }

    // This method is called on the main GUI thread.
    private void backgroundExecution() {
        // This moves the time consuming operation to a child thread.
        Thread thread = new Thread(null, doBackgroundThreadProcessing, "Background");
        thread.start();
    }
    // Runnable that executes the background processing method.
    private Runnable doBackgroundThreadProcessing = new Runnable() {
        public void run() {
            backgroundThreadProcessing();
        }
    };

    // Method which does some processing in the background.
    private void backgroundThreadProcessing() {
        Log.i("ManualThread", "Manual: Time consuming operations");
    }
}