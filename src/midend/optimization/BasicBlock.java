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
        HashMap<Value, Value> valueMap = new HashMap<>();
        HashMap<Value, Boolean> valueAssigned = new HashMap<>();
        ArrayList<IRCode> newIRCodes = new ArrayList<>();
        for (IRCode irCode : irCodes) {
            if (irCode instanceof UnaryAssign) {
                UnaryAssign unaryAssign = (UnaryAssign) irCode;
                if (valueMap.containsKey(unaryAssign.getRight())) {
                    if (unaryAssign.getRight().getType() == ValueType.Variable && unaryAssign.getRight().getName().matches("t_\\d+_temp")) {
                        valueAssigned.put(unaryAssign.getRight(), true);
                    }
                    unaryAssign.setRight(valueMap.get(unaryAssign.getRight()));
                }
//                for (Value value : valueMap.keySet()) {
//                    if (value.equals(unaryAssign.getRight()))
//                        unaryAssign.setRight(valueMap.get(value));
//                }
                switch (unaryAssign.getOperator()) {
                    case PLUS:
                        if (unaryAssign.getLeft().getType() == ValueType.Array) {
                            newIRCodes.add(irCode);
                        } else {
                            valueMap.put(unaryAssign.getLeft(), unaryAssign.getRight());
                            valueAssigned.put(unaryAssign.getLeft(), false);
                        }
                        break;
                    case MINUS:
                        if (unaryAssign.getRight().getType() == ValueType.Imm && unaryAssign.getLeft().getType() != ValueType.Array) {
                            valueMap.put(unaryAssign.getLeft(), new Value(-unaryAssign.getRight().getValue()));
                            valueAssigned.put(unaryAssign.getLeft(), false);
                        } else {
                            newIRCodes.add(irCode);
                            valueMap.remove(unaryAssign.getLeft());
                            valueAssigned.put(unaryAssign.getLeft(), true);
                        }
                        break;
                    case NOT:
                        if (unaryAssign.getRight().getType() == ValueType.Imm && unaryAssign.getLeft().getType() != ValueType.Array) {
                            valueMap.put(unaryAssign.getLeft(), new Value(unaryAssign.getRight().getValue() == 0 ? 1 : 0));
                            valueAssigned.put(unaryAssign.getLeft(), false);
                        } else {
                            newIRCodes.add(irCode);
                            valueMap.remove(unaryAssign.getLeft());
                            valueAssigned.put(unaryAssign.getLeft(), true);
                        }
                        break;
                }
            } else if (irCode instanceof BinaryAssign) {
                BinaryAssign binaryAssign = (BinaryAssign) irCode;
                if (valueMap.containsKey(binaryAssign.getRight(0))) {
                    if (binaryAssign.getRight(0).getType() == ValueType.Variable && binaryAssign.getRight(0).getName().matches("t_\\d+_temp")) {
                        valueAssigned.put(binaryAssign.getRight(0), true);
                    }
                    binaryAssign.setRight(0, valueMap.get(binaryAssign.getRight(0)));
                }
                if (valueMap.containsKey(binaryAssign.getRight(1))) {
                    if (binaryAssign.getRight(1).getType() == ValueType.Variable && binaryAssign.getRight(1).getName().matches("t_\\d+_temp")) {
                        valueAssigned.put(binaryAssign.getRight(1), true);
                    }
                    binaryAssign.setRight(1, valueMap.get(binaryAssign.getRight(1)));
                }
//                for (Value value : valueMap.keySet()) {
//                    if (value.equals(binaryAssign.getRight(0)))
//                        binaryAssign.setRight(0, valueMap.get(value));
//                }
//                for (Value value : valueMap.keySet()) {
//                    if (value.equals(binaryAssign.getRight(1)))
//                        binaryAssign.setRight(1, valueMap.get(value));
//                }
                if (binaryAssign.getRight(0).getType() == ValueType.Imm && binaryAssign.getRight(1).getType() == ValueType.Imm) {
                    switch (binaryAssign.getOperator()) {
                        case PLUS:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() + binaryAssign.getRight(1).getValue()));
                            break;
                        case MINUS:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() - binaryAssign.getRight(1).getValue()));
                            break;
                        case MULTI:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() * binaryAssign.getRight(1).getValue()));
                            break;
                        case DIV:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() / binaryAssign.getRight(1).getValue()));
                            break;
                        case MOD:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() % binaryAssign.getRight(1).getValue()));
                            break;
                        case EQL:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() == binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case NEQ:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() != binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case GRE:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() > binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case LSS:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() < binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case LEQ:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() <= binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                        case GEQ:
                            valueMap.put(binaryAssign.getLeft(), new Value(binaryAssign.getRight(0).getValue() >= binaryAssign.getRight(1).getValue() ? 1 : 0));
                            break;
                    }
                    if (binaryAssign.getLeft().getType() == ValueType.Variable)
                        valueAssigned.put(binaryAssign.getLeft(), false);
                    else
                        newIRCodes.add(irCode);
                } else {
                    newIRCodes.add(irCode);
                    valueAssigned.put(binaryAssign.getLeft(), true);
                    valueMap.remove(binaryAssign.getLeft());
                }
            } else if (irCode instanceof FuncCall) {
                FuncCall funcCall = (FuncCall) irCode;
                ArrayList<Value> params = new ArrayList<>();
                for (Value param : funcCall.getParams()) {
                    params.add(valueMap.getOrDefault(param, param));
                }
                funcCall.setParams(params);
                newIRCodes.add(irCode);
            } else if (irCode instanceof GetInt) {
                GetInt getInt = (GetInt) irCode;
                if (valueMap.containsKey(getInt.getValue())) {
                    valueMap.remove(getInt.getValue());
                    valueAssigned.put(getInt.getValue(), true);
                }
//                for (Value value : valueMap.keySet()) {
//                    if (value.equals(getInt.getValue())) {
//                        valueMap.remove(value);
//                        valueAssigned.put(value, true);
//                    }
//                }
                newIRCodes.add(irCode);
            } else if (irCode instanceof PrintNumber) {
                PrintNumber printNumber = (PrintNumber) irCode;
                if (valueMap.containsKey(printNumber.getValue())) {
                    printNumber.setValue(valueMap.get(printNumber.getValue()));
                    valueMap.remove(printNumber.getValue());
                }
//                for (Value value : valueMap.keySet()) {
//                    if (value.equals(printNumber.getValue())) {
//                        valueMap.remove(value);
//                        valueAssigned.put(value, true);
//                        printNumber.setValue(valueMap.get(value));
//                    }
//                }
                newIRCodes.add(irCode);
            } else if (irCode instanceof Return) {
                Return returnCode = (Return) irCode;
                if (returnCode.getValue() != null && valueMap.containsKey(returnCode.getValue()))
                    returnCode.setValue(valueMap.get(returnCode.getValue()));
//                if (returnCode.getValue() != null)
//                    for (Value value : valueMap.keySet()) {
//                        if (value.equals(returnCode.getValue())) {
//                            returnCode.setValue(valueMap.get(value));
//                        }
//                    }
                newIRCodes.add(irCode);
            } else if (irCode instanceof Jump) {
                for (Value key : valueMap.keySet()) {
                    if (!valueAssigned.get(key)) {
                        newIRCodes.add(new UnaryAssign(key, valueMap.get(key), Operator.PLUS));
                    }
                }
                newIRCodes.add(irCode);
            } else if (irCode instanceof Branch) {
                Branch branch = (Branch) irCode;
                if (valueMap.containsKey(branch.getCondition())) {
                    branch.setCondition(valueMap.get(branch.getCondition()));
                    valueMap.remove(branch.getCondition());
                }
            } else {
                newIRCodes.add(irCode);
            }
        }
//        System.out.println(valueMap);
//        System.out.println(valueAssigned);
        irCodes = newIRCodes;
    }
}
