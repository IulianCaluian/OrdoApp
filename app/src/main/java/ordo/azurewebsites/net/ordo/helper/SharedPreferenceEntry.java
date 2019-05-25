package ordo.azurewebsites.net.ordo.helper;


public class SharedPreferenceEntry {
    // Name of the user.
    private final String mName;
    // Date of Birth of the user.
    // Email address of the user.
    private final String mEmail;
    public SharedPreferenceEntry(String name, String email) {
        mName = name;
        mEmail = email;
    }
    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }
}

