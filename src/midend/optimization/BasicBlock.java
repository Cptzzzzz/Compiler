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
        return irCodes;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void constOptimize() {

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
                        map.remove(value);
                    }
                }
                if (map.containsKey(binaryAssign.getRight(1))) {
                    Value value = binaryAssign.getRight(1);
                    binaryAssign.setRight(1, map.get(binaryAssign.getRight(1)));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        map.remove(value);
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
                    map.remove(binaryAssign.getLeft());
                    newIRCodes.add(irCode);
                }
            } else if (irCode instanceof Branch) {
                Branch branch = (Branch) irCode;
                if (map.containsKey(branch.getCondition())) {
                    Value value = branch.getCondition();
                    branch.setCondition(map.get(branch.getCondition()));
                    if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                        map.remove(value);
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
                            map.remove(value);
                        }
                    }
                }
                ArrayList<Value> shouldRemove = new ArrayList<>();
                for (Value value : map.keySet()) {
                    if (value.getName().matches(".*_0")) {
                        newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
                        shouldRemove.add(value);
                    }
                }
                for (Value value : shouldRemove) {
                    map.remove(value);
                }
                newIRCodes.add(irCode);
            } else if (irCode instanceof FuncEnd) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof FuncEntry) {
                newIRCodes.add(irCode);
            } else if (irCode instanceof GetInt) {
                GetInt getInt = (GetInt) irCode;
                map.remove(getInt.getValue());
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
                        map.remove(value);
                    }
                }
                if (printNumber.getValue().getType() == ValueType.Array) {
                    if (map.containsKey(printNumber.getValue().getOffset())) {
                        Value value = printNumber.getValue().getOffset();
                        printNumber.getValue().setOffset(map.get(printNumber.getValue().getOffset()));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            map.remove(value);
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
                        map.remove(value);
                    }
                }
                if (unaryAssign.getLeft().getType() == ValueType.Array && unaryAssign.getLeft().getOffset().getType() == ValueType.Variable) {
                    if (map.containsKey(unaryAssign.getLeft().getOffset())) {
                        Value value = unaryAssign.getLeft().getOffset();
                        unaryAssign.getLeft().setOffset(map.get(unaryAssign.getLeft().getOffset()));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            map.remove(value);
                        }
                    }
                }
                if (unaryAssign.getRight().getType() == ValueType.Array && unaryAssign.getRight().getOffset().getType() == ValueType.Variable) {
                    if (map.containsKey(unaryAssign.getRight().getOffset())) {
                        Value value = unaryAssign.getRight().getOffset();
                        unaryAssign.getRight().setOffset(map.get(unaryAssign.getRight().getOffset()));
                        if (value.getType() == ValueType.Variable && value.getName().matches("t_\\d+_temp")) {
                            map.remove(value);
                        }
                    }
                }
                if (unaryAssign.getRight().getType() == ValueType.Array || unaryAssign.getLeft().getType() == ValueType.Array) {
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
                } else {
                    map.remove(unaryAssign.getLeft());
                    newIRCodes.add(irCode);
                }
            }
        }
        for (Value value : map.keySet()) {
            newIRCodes.add(new UnaryAssign(value, map.get(value), Operator.PLUS));
        }
        irCodes = newIRCodes;
    }
}
