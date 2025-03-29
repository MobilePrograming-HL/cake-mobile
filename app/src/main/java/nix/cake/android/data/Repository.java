package nix.cake.android.data;

import nix.cake.android.data.local.prefs.PreferencesService;
import nix.cake.android.data.local.room.RoomService;
import nix.cake.android.data.remote.ApiService;


public interface Repository {

    /**
     * ################################## Preference section ##################################
     */
    String getToken();
    void setToken(String token);

    PreferencesService getSharedPreferences();


    /**
     *  ################################## Remote api ##################################
     */
    ApiService getApiService();

    RoomService getRoomService();

}
