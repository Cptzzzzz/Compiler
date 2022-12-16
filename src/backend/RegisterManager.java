package backend;

import midend.util.Value;

import java.util.HashMap;

public class RegisterManager {
    private static RegisterManager registerManager;

    private RegisterManager() {
    }

    public static RegisterManager getInstance() {
        if (registerManager == null)
            registerManager = new RegisterManager();
        return registerManager;
    }

    private HashMap<String, Value> manager = new HashMap<>();
}
