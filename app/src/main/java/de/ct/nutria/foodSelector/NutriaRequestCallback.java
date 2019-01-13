package de.ct.nutria.foodSelector;

import android.net.NetworkInfo;

public interface NutriaRequestCallback<T> {
    interface Progress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int PROCESS_INPUT_STREAM_SUCCESS = 3;
    }

    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    void updateFromRequest(T result);

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /**
     * Indicate to callback handler any progress update.
     * @param progressCode must be one of the constants defined in NutriaRequestCallback.Progress.
     */
    void onProgressUpdate(int progressCode);

    /**
     * Indicates that the request has finished. This method is called even if the
     * request hasn't completed successfully.
     */
    void finishRequest();

}
