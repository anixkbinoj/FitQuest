package com.fitquest;
import com.fitquest.api.ApiClient;
import com.fitquest.ui.AppFrame;

public class Main {
    public static void main(String[] args) {
        // The API scripts are in the /api/ subfolder.
        // The URL must point there directly.
        ApiClient client = new ApiClient("http://localhost/fitquest/api");
        AppFrame.open(client);
    }
}
