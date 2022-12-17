package midend.optimization;

import midend.ir.*;
import midend.util.Operator;
import midend.util.Value;
import midend.util.ValueType;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicBlock {
    private ArrayList<IRCode> irCodes = new ArrayList<>();
    private int blockNumber;


    public void addIRCode(IRCode irCode) {
        irCodes.add(irCode);
    }

    public ArrayList<IRCode> getIrCodes() {
//        ArrayList<IRCode> res = new ArrayList<>();
//        for (IRCode irCode : irCodes) {
//            res.add(irCode);
//            if (irCode instanceof Return)
//                break;
//        }
//        irCodes = res;
        return irCodes;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    private boolean hasReturn = false;

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public boolean isHasReturn() {
        return hasReturn;
    }

    private ArrayList<String> def;
    private ArrayList<String> use;
    private ArrayList<String> in;
    private ArrayList<String> out;

    public ArrayList<String> getIn() {
        if (in == null)
            in = new ArrayList<>();
        return in;
    }

    public void setIn(ArrayList<String> in) {
        this.in = in;
    }

    public ArrayList<String> getOut() {
        if (out == null)
            out = new ArrayList<>();
        return out;
    }

    public void setOut(ArrayList<String> out) {
        this.out = out;
    }

    public ArrayList<String> getUse() {
        return use;
    }

    public ArrayList<String> getDef() {
        return def;
    }

    private void addDef(Value value) {
        if (value.getType() == ValueType.Imm)
            return;
        if (def.contains(value.getName()) || use.contains(value.getName()))
            return;
        if (value.getType() == ValueType.Array)
            addUse(value.getOffset());
        def.add(value.getName());
    }

    private void addDef(String name) {
        if (def.contains(name) || use.contains(name))
            return;
        def.add(name);
    }

    private void addUse(Value value) {
        if (value.getType() == ValueType.Imm)
            return;
        if (def.contains(value.getName()) || use.contains(value.getName()))
            return;
        if (value.getType() == ValueType.Array)
            addUse(value.getOffset());
        use.add(value.getName());
    }

    public void analyse() {
        def = new ArrayList<>();
        use = new ArrayList<>();
        boolean flag = false;
        for (IRCode irCode : irCodes) {
            if (irCode instanceof BinaryAssign) {
                addUse(((BinaryAssign) irCode).getRight(0));
                addUse(((BinaryAssign) irCode).getRight(1));
                addDef(((BinaryAssign) irCode).getLeft());
            } else if (irCode instanceof Branch) {
                addUse(((Branch) irCode).getCondition());
            } else if (irCode instanceof Declaration) {
                addDef(((Declaration) irCode).getName());
            } else if (irCode instanceof FuncCall) {
                for (Value value : ((FuncCall) irCode).getParams())
                    addUse(value);
            } else if (irCode instanceof GetInt) {
                addDef(((GetInt) irCode).getValue());
            } else if (irCode instanceof PrintNumber) {
                addUse(((PrintNumber) irCode).getValue());
            } else if (irCode instanceof Return) {
                if (((Return) irCode).getValue() != null)
                    addUse(((Return) irCode).getValue());
                flag = true;
            } else if (irCode instanceof UnaryAssign) {
                addUse(((UnaryAssign) irCode).getRight());
                addDef(((UnaryAssign) irCode).getLeft());
            }
            if (flag)
                break;
        }
//        System.out.println(getBlockNumber());
//        System.out.println("def: " + def);
//        System.out.println("use: " + use);
    }

    public void dag() {
        Dag dag = new Dag();
        ArrayList<IRCode> res = new ArrayList<>();
        for (IRCode irCode : irCodes) {
            if (irCode instanceof Declaration)
                res.add(irCode);
            else if (irCode instanceof Label)
                res.add(irCode);
            else
                dag.read(irCode);
        }
        System.out.println(dag);
        res.addAll(dag.export(out));
        irCodes = res;
    }

    private void mapRemove(HashMap<Value, Value> map, Value key, ArrayList<IRCode> irCodes) {
        ArrayList<Value> shouldRemove = new ArrayList<>();
        map.remove(key);
        for (Value value : map.keySet()) {
            if (map.get(value).equals(key)) {
                shouldRemove.add(value);
                irCodes.add(new UnaryAssign(value, key, Operator.PLUS));
            }
        }
        for (Value value : shouldRemove)
            map.remove(value);
    }

    public void optimize() {
        HashMap<Value, Value> map = new HashMap<>();//常量/复写传播 key: variable  value: constant or variable
        ArrayList<IRCode> newIRCodes = new ArrayList<>();
        for (IRCode irCode : irCodes) {
            if (irCode instanceof BinaryAssign) {
                BinaryAssign binaryAssign = (BinaryAssign) irCode;
                if (map.containsKey(binaryAssign.getRight(0))) {
                    Value value = binaryAssign.getRight(0);
                    binaryAssign.setRight(0, map.get(binaryAssign.getRight(0)));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        mapRemove(map, value, newIRCodes);
                    }
                }
                if (map.containsKey(binaryAssign.getRight(1))) {
                    Value value = binaryAssign.getRight(1);
                    binaryAssign.setRight(1, map.get(binaryAssign.getRight(1)));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        mapRemove(map, value, newIRCodes);
                    }
                }
                if (binaryAssign.getRight(0).getType() == ValueType.Imm && binaryAssign.getRight(1).getType() == ValueType.Imm) {
                    switch (binaryAssign.getOperator()) {
                        case PLUS:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() + binaryAssign.getRight(1).getValue()));
                            break;
                        case MINUS:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() - binaryAssign.getRight(1).getValue()));
                            break;
                        case MULTI:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() * binaryAssign.getRight(1).getValue()));
                            break;
                        case DIV:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() / binaryAssign.getRight(1).getValue()));
                            break;
                        case MOD:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() % binaryAssign.getRight(1).getValue()));
                            break;
                        case EQL:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() == binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case NEQ:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() != binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case GRE:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() > binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case LSS:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() < binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case LEQ:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() <= binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case GEQ:
                            map.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() >= binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                    }
                } else {
                    mapRemove(map, binaryAssign.getLeft(), newIRCodes);
                    newIRCodes.add(irCode);
                }
            } else if (irCode instanceof Branch) {
                Branch branch = (Branch) irCode;
                if (map.containsKey(branch.getCondition())) {
                    Value value = branch.getCondition();
                    branch.setCondition(map.get(branch.getCondition()));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        mapRemove(map, value, newIRCodes);
                    }
                }
                for (Value value : map.keySet()) {
                    newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
                }
                map.clear();
                if (branch.getCondition().getType() == ValueType.Imm) {
                    if (branch.getCondition().getValue() == 0) {
                        newIRCodes.add(new Jump(branch.getLabel()));
                    }
                } else {
                    newIRCodes.add(irCode);
                }
            } else if (irCode instanceof Declaration) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof FuncCall) {
                FuncCall funcCall = (FuncCall) irCode;
                int length = funcCall.getParams().size();
                for (int i = 0; i < length; i++) {
                    if (map.containsKey(funcCall.getParams().get(i))) {
                        Value value = funcCall.getParams().get(i);
                        funcCall.getParams().set(i, map.get(funcCall.getParams().get(i)));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            mapRemove(map, value, newIRCodes);
                        }
                    }
                }
                ArrayList<Value> shouldRemove = new ArrayList<>();
                for (Value value : map.keySet()) {
                    if (value.getName().matches(".*_0")) {
                        newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
                        shouldRemove.add(value);
                    } else if (map.get(value).getType() == ValueType.Variable && map.get(value).getName().matches(".*_0")) {
                        newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
                        shouldRemove.add(value);
                    }
                }
                for (Value value : shouldRemove) {
                    mapRemove(map, value, newIRCodes);
                }
                newIRCodes.add(irCode);
            } else if (irCode instanceof FuncEnd) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof FuncEntry) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof GetInt) {
                GetInt getInt = (GetInt) irCode;
                mapRemove(map, getInt.getValue(), newIRCodes);
                newIRCodes.add(irCode);
            } else if (irCode instanceof Jump) {
                for (Value value : map.keySet()) {
                    newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
                }
                map.clear();
                newIRCodes.add(irCode);
            } else if (irCode instanceof Label) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof PrintNumber) {
                PrintNumber printNumber = (PrintNumber) irCode;
                if (map.containsKey(printNumber.getValue())) {
                    Value value = printNumber.getValue();
                    printNumber.setValue(map.get(printNumber.getValue()));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        mapRemove(map, value, newIRCodes);
                    }
                }
                if (printNumber.getValue().getType() == ValueType.Array) {
                    if (map.containsKey(printNumber.getValue().getOffset())) {
                        Value value = printNumber.getValue().getOffset();
                        printNumber.getValue().setOffset(map.get(printNumber.getValue().getOffset()));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            mapRemove(map, value, newIRCodes);
                        }
                    }
                }
                newIRCodes.add(irCode);
            } else if (irCode instanceof PrintString) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof Return) {
                Return returnCode = (Return) irCode;
                if (map.containsKey(returnCode.getValue())) {
                    returnCode.setValue(map.get(returnCode.getValue()));
                }
                for (Value value : map.keySet()) {
                    if (value.getName().matches(".*_0"))
                        newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
                }
                map.clear();
                newIRCodes.add(irCode);
            } else if (irCode instanceof UnaryAssign) {
                UnaryAssign unaryAssign = (UnaryAssign) irCode;
                if (map.containsKey(unaryAssign.getRight())) {
                    Value value = unaryAssign.getRight();
                    unaryAssign.setRight(map.get(unaryAssign.getRight()));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        mapRemove(map, value, newIRCodes);
                    }
                }
                if (unaryAssign.getLeft().getType() == ValueType.Array && unaryAssign.getLeft().getOffset().getType() == ValueType.Variable) {
                    if (map.containsKey(unaryAssign.getLeft().getOffset())) {
                        Value value = unaryAssign.getLeft().getOffset();
                        unaryAssign.getLeft().setOffset(map.get(unaryAssign.getLeft().getOffset()));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            mapRemove(map, value, newIRCodes);
                        }
                    }
                }
                if (unaryAssign.getRight().getType() == ValueType.Array && unaryAssign.getRight().getOffset().getType() == ValueType.Variable) {
                    if (map.containsKey(unaryAssign.getRight().getOffset())) {
                        Value value = unaryAssign.getRight().getOffset();
                        unaryAssign.getRight().setOffset(map.get(unaryAssign.getRight().getOffset()));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            mapRemove(map, value, newIRCodes);
                        }
                    }
                }
                if (unaryAssign.getRight().getType() == ValueType.Array || unaryAssign.getLeft().getType() == ValueType.Array) {
                    mapRemove(map, unaryAssign.getLeft(), newIRCodes);
                    newIRCodes.add(irCode);
                    continue;
                }
                if (unaryAssign.getRight().getType() == ValueType.Imm) {
                    switch (unaryAssign.getOperator()) {
                        case PLUS:
                            map.put(unaryAssign.getLeft(), new Value(unaryAssign.getRight().getValue()));
                            break;
                        case MINUS:
                            map.put(unaryAssign.getLeft(), new Value(-unaryAssign.getRight().getValue()));
                            break;
                        case NOT:
                            map.put(unaryAssign.getLeft(), new Value(unaryAssign.getRight().getValue() == 0 ? 1 : 0));
                            break;
                    }
                } else if (unaryAssign.getOperator() == Operator.PLUS) {
                    mapRemove(map, unaryAssign.getLeft(), newIRCodes);
                    map.put(unaryAssign.getLeft(), unaryAssign.getRight());
                } else {
                    mapRemove(map, unaryAssign.getLeft(), newIRCodes);
                    newIRCodes.add(irCode);
                }
            }
        }
        for (Value value : map.keySet()) {
            newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
        }
        irCodes = newIRCodes;
    }

    public void print() {
        System.out.println(getBlockNumber());
        System.out.println("in" + in);
        System.out.println("out" + out);
        System.out.println("def" + def);
        System.out.println("use" + use);
    }
}
