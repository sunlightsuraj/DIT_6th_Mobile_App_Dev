package np.info.suraj.servicebinding;

import android.os.AsyncTask;
import android.util.Log;

public class MyAsynkTask extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... parameter) {
        // Moved to a background thread.
        String result = "";
        int myProgress = 0;
        int inputLength = parameter[0].length();

        // Perform background processing task, update myProgress]
        for (int i = 1; i <= inputLength; i++) {
            myProgress = i;
            result = result + parameter[0].charAt(inputLength-i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
        }

        publishProgress(myProgress);

        // Return the value to be passed to onPostExecute
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Synchronized to UI thread.
        // Update progress bar, Notification, or other UI elements
        Log.i("asyncTask", String.valueOf(progress[0]));
        //asyncProgress.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
    // Synchronized to UI thread.
    // Report results via UI update, Dialog, or notifications
//        asyncTextView.setText(result);
        Log.i("asyncTask", result);
    }
}
