package com.fitquest;
import com.fitquest.api.ApiClient;
import com.fitquest.ui.AppFrame;

public class Main {
    public static void main(String[] args) {
        // The API scripts are in the /api/ subfolder.
        // The URL must point directly to the directory containing the PHP API scripts.
        ApiClient client = new ApiClient("http://localhost/fitquest");
        AppFrame.open(client);
    }
}
