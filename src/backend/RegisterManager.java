package backend;

import midend.util.Value;
import midend.util.ValueType;

import java.util.ArrayList;
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

    private HashMap<String, Value> manager;
    private HashMap<Value, Boolean> writeBack;
    private ArrayList<String> free;
    private ArrayList<String> protect;

    public void protect(String register) {
        protect.add(register);
    }

    public void unprotect(String register) {
        protect.remove(register);
    }

    private boolean legal(String register) {
        return !register.equals("$a0") &&
                !register.equals("$v0") &&
                !register.equals("$t0") &&
                !register.equals("$t1") &&
                !register.equals("$t4") &&
                !register.equals("$t3");
    }

    public void init() {
        protect = new ArrayList<>();
        manager = new HashMap<>();
        writeBack = new HashMap<>();
        free = new ArrayList<>();
        free.add("$t2");
        free.add("$t5");
        free.add("$t6");
        free.add("$t7");
        free.add("$t8");
        free.add("$t9");
        free.add("$s0");
        free.add("$s1");
        free.add("$s2");
        free.add("$s3");
        free.add("$s4");
        free.add("$s5");
        free.add("$s6");
        free.add("$s7");
        free.add("$v1");
        free.add("$a1");
        free.add("$a2");
        free.add("$a3");
    }

    public String load(Value value) {
        for (String register : manager.keySet()) {
            if (manager.get(register).equals(value)) {
                if (manager.get(register).getType() == ValueType.Variable &&
                        manager.get(register).getName().matches("t\\_\\d+\\_temp"))
                    writeBack.put(value, false);
                return register;
            }
        }
        if (free.size() == 0) {
            eliminate();
            return load(value);
        } else {
            String register = free.remove(0);
            manager.put(register, value);
            writeBack.put(value, false);
            SymbolManager.getInstance().load(value, register);
            return register;
        }
    }

    public void load(Value value, String register) {
        String temp = load(value);
        Mips.writeln(String.format("move %s %s", register, temp));
    }

    public String allocate(Value value) {
        for (String register : manager.keySet()) {
            if (manager.get(register).equals(value)) {
                writeBack.put(value, true);
                return register;
            }
        }
        if (free.size() == 0) {
            eliminate();
            return allocate(value);
        } else {
            String register = free.remove(0);
            manager.put(register, value);
            writeBack.put(value, true);
            return register;
        }
    }

    public void allocate(Value value, String register) {
        String temp = allocate(value);
        Mips.writeln(String.format("move %s %s", temp, register));
    }

    private void eliminate() {
        String eliminate = null;
        int weight = 998244353;
        for (String register : manager.keySet()) {
            if (protect.contains(register))
                continue;
            if (SymbolManager.getInstance().getWeight(manager.get(register)) < weight) {
                weight = SymbolManager.getInstance().getWeight(manager.get(register));
                if (writeBack.get(manager.get(register)))
                    weight *= 2;
                eliminate = register;
            }
        }
        assert eliminate != null;
        if (writeBack.get(manager.get(eliminate))) {
            SymbolManager.getInstance().store(manager.get(eliminate), eliminate);
        }
        writeBack.remove(manager.get(eliminate));
        manager.remove(eliminate);
        free.add(eliminate);
    }

    public void clear() {
        ArrayList<String> temp = new ArrayList<>();
        for (String register : manager.keySet()) {
            Value value = manager.get(register);
            if (writeBack.get(value)) {
//                SymbolManager.getInstance().store(manager.get(register), register);
                temp.add(register);
            }
        }
        for (String register : temp) {
            protect(register);
            SymbolManager.getInstance().store(manager.get(register), register);
            unprotect(register);
        }
        init();
    }

    public void symbolLoad(Value value) {
        for (String register : manager.keySet()) {
            if (manager.get(register).equals(value)) {
                Mips.writeln("move $t0 " + register);
                return;
            }
        }
        SymbolManager.getInstance().load(value, "$t0");
    }

    public void free(String register) {
        if (manager.containsKey(register)) {
            writeBack.remove(manager.get(register));
            manager.remove(register);
            free.add(register);
        }
    }
}
