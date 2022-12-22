package midend.optimization;

import midend.ir.*;
import midend.util.Operator;
import midend.util.Value;
import midend.util.ValueType;

import java.util.ArrayList;
import java.util.HashMap;

public class Dag {
    private HashMap<Value, Integer> valueTable = new HashMap<>();
    private int number = -1;
    private ArrayList<DagNode> dagNodes = new ArrayList<>();

    private int getArrayNumber(String name) {
        Value value = new Value(name, new Value(-1), false);
        if (valueTable.containsKey(value))
            return valueTable.get(value);
        else {
            number++;
            valueTable.put(value, number);
            DagNode dagNode = new DagNode(number, "Array");
            dagNode.addValue(value);
            dagNodes.add(dagNode);
            return number;
        }
    }

    private int getValueNumber(Value value) {
        if (valueTable.containsKey(value))
            return valueTable.get(value);
        else {
            if (value.getType() == ValueType.Array) {
                int offset = getValueNumber(value.getOffset());
                int array = getArrayNumber(value.getName());
                number++;
                DagNode dagNode = new DagNode(number, "Load");
                dagNode.addValue(value);
                dagNode.getChildren().add(array);
                dagNode.getChildren().add(offset);
                dagNodes.add(dagNode);
            } else {
                number++;
                valueTable.put(value, number);
                DagNode dagNode = new DagNode(number, "None");
                dagNode.addValue(value);
                dagNodes.add(dagNode);
            }
            return number;
        }
    }

    public void read(IRCode irCode) {
        if (irCode instanceof BinaryAssign) {
            BinaryAssign binaryAssign = (BinaryAssign) irCode;
            Value left = binaryAssign.getLeft();
            Value right1 = binaryAssign.getRight(0);
            Value right2 = binaryAssign.getRight(1);
            int r1 = getValueNumber(right1);
            int r2 = getValueNumber(right2);
            for (DagNode dagNode : dagNodes) {
                if (binaryAssign.getOperator().toName().equals(dagNode.getOperator()) && dagNode.getChildren().size() == 2) {
                    if (r1 == dagNode.getChildren().get(0) && r2 == dagNode.getChildren().get(1)) {
                        valueTable.put(left, dagNode.getNumber());
                        dagNode.addValue(left);
                        return;
                    }
                }
            }
            number++;
            DagNode dagNode = new DagNode(number, binaryAssign.getOperator().toName());
            valueTable.put(left, number);
            dagNode.addValue(left);
            dagNode.getChildren().add(r1);
            dagNode.getChildren().add(r2);
            dagNodes.add(dagNode);
        } else if (irCode instanceof Branch) {
            Branch branch = (Branch) irCode;
            int c = getValueNumber(branch.getCondition());
            number++;
            DagNode dagNode = new DagNode(number, "Branch");
            dagNode.getChildren().add(c);
            dagNode.setLabel(branch.getLabel());
            dagNodes.add(dagNode);
        } else if (irCode instanceof FuncCall) {
            FuncCall funcCall = (FuncCall) irCode;
            ArrayList<Integer> numbers = new ArrayList<>();
            ArrayList<Value> shouldRemove = new ArrayList<>();//函数调用移除所有全局变量和数组
            for (Value value : funcCall.getParams()) {
                numbers.add(getValueNumber(value));
                if (value.getType() == ValueType.Array && value.isAddress()) {
                    shouldRemove.add(new Value(value.getName(), new Value(-1), false));
                }
            }
            number++;
            DagNode dagNode = new DagNode(number, "Call");
            dagNode.getChildren().addAll(numbers);
            dagNode.setFunction(funcCall.getName());
            if (funcCall.getResult() != null) {
//                System.out.println("result: " + funcCall.getResult());
                valueTable.put(funcCall.getResult(), number);
                dagNode.addValue(funcCall.getResult());
            }
            dagNodes.add(dagNode);
            for (Value key : valueTable.keySet()) {
                if ((key.getType() == ValueType.Array || key.getType() == ValueType.Variable) && key.getName().matches(".*\\_0")) {
                    if (key.getType() == ValueType.Array) {
                        shouldRemove.add(new Value(key.getName(), new Value(-1), false));
                    } else {
                        shouldRemove.add(key);
                    }
                }
            }
            for (Value key : shouldRemove) {
                valueTable.remove(key);
            }
        } else if (irCode instanceof GetInt) {
            number++;
            DagNode dagNode = new DagNode(number, "GetInt");
            dagNode.addValue(((GetInt) irCode).getValue());
            dagNodes.add(dagNode);
            valueTable.put(((GetInt) irCode).getValue(), number);
        } else if (irCode instanceof PrintNumber) {
            int r = getValueNumber(((PrintNumber) irCode).getValue());
            number++;
            DagNode dagNode = new DagNode(number, "PrintNumber");
            dagNode.getChildren().add(r);
            dagNode.addValue(((PrintNumber) irCode).getValue());
            dagNodes.add(dagNode);
        } else if (irCode instanceof Jump) {
            number++;
            DagNode dagNode = new DagNode(number, "Jump");
            dagNode.setLabel(((Jump) irCode).getLabel());
            dagNodes.add(dagNode);
        } else if (irCode instanceof PrintString) {
            number++;
            DagNode dagNode = new DagNode(number, "PrintString");
            dagNode.setPrint(((PrintString) irCode).getContent());
            dagNodes.add(dagNode);
        } else if (irCode instanceof Return) {
            Return returnCode = (Return) irCode;
            if (returnCode.getValue() == null) {
                number++;
                DagNode dagNode = new DagNode(number, "Return");
                dagNodes.add(dagNode);
            } else {
                int r = getValueNumber(returnCode.getValue());
                number++;
                DagNode dagNode = new DagNode(number, "Return");
                dagNode.getChildren().add(r);
                dagNodes.add(dagNode);
            }
        } else if (irCode instanceof UnaryAssign) {
            UnaryAssign unaryAssign = (UnaryAssign) irCode;
            int r = getValueNumber((unaryAssign.getRight()));
            if (unaryAssign.getOperator() == Operator.PLUS && unaryAssign.getLeft().getType() == ValueType.Array) {
                int offset = getValueNumber(unaryAssign.getLeft().getOffset());
                number++;
                DagNode dagNode = new DagNode(number, "Store");
                dagNode.getChildren().add(offset);
                dagNode.getChildren().add(r);
                dagNode.addValue(unaryAssign.getLeft());
                dagNodes.add(dagNode);
                valueTable.remove(new Value(unaryAssign.getLeft().getName(), new Value(-1), false));
            } else {
                for (DagNode dagNode : dagNodes) {
                    if (dagNode.getOperator().equals(unaryAssign.getOperator().toName())&&dagNode.getChildren().size()==1) {
                        if (dagNode.getChildren().get(0) == r) {
                            valueTable.put(unaryAssign.getLeft(), dagNode.getNumber());
                            dagNode.addValue(unaryAssign.getLeft());
                            return;
                        }
                    }
                }
//                if (unaryAssign.getOperator() == Operator.PLUS) {
//                    valueTable.put(unaryAssign.getLeft(), r);
//                    for (DagNode dagNode : dagNodes) {
//                        if (dagNode.getNumber() == r) {
//                            dagNode.addValue(unaryAssign.getLeft());
//                            return;
//                        }
//                    }
//                }
                number++;
                DagNode dagNode = new DagNode(number, unaryAssign.getOperator().toName());
                dagNode.getChildren().add(r);
                dagNode.addValue(unaryAssign.getLeft());
                valueTable.put(unaryAssign.getLeft(), number);
                dagNodes.add(dagNode);
            }
        }
    }

    public ArrayList<IRCode> export(ArrayList<String> out) {
        ArrayList<IRCode> res = new ArrayList<>();
        HashMap<Integer, Value> valueMap = new HashMap<>();
        for (DagNode dagNode : dagNodes) {
            if (dagNode.getOperator().equals("None")) {
                valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
            } else if (dagNode.getOperator().equals("PLUS")) {
                if (dagNode.getChildren().size() == 1) {
                    for (Value value : dagNode.getValues()) {
                        if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                            if (valueMap.containsKey(dagNode.getNumber())) {
                                res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                            } else {
                                res.add(new UnaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), Operator.PLUS));
                                valueMap.put(dagNode.getNumber(), value);
                            }
                        }
                    }
                    if (!valueMap.containsKey(dagNode.getNumber())) {
                        res.add(new UnaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), Operator.PLUS));
                        valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                    }
                } else {
                    for (Value value : dagNode.getValues()) {
//                        System.out.println(value);
                        if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                            if (valueMap.containsKey(dagNode.getNumber())) {
                                res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                            } else {
                                res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.PLUS));
                                valueMap.put(dagNode.getNumber(), value);
                            }
                        }
                    }
                    if (!valueMap.containsKey(dagNode.getNumber())) {
                        res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.PLUS));
                        valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                    }
                }
            } else if (dagNode.getOperator().equals("MINUS")) {
                if (dagNode.getChildren().size() == 1) {
                    for (Value value : dagNode.getValues()) {
                        if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                            if (valueMap.containsKey(dagNode.getNumber())) {
                                res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                            } else {
                                res.add(new UnaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), Operator.MINUS));
                                valueMap.put(dagNode.getNumber(), value);
                            }
                        }
                    }
                    if (!valueMap.containsKey(dagNode.getNumber())) {
                        res.add(new UnaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), Operator.MINUS));
                        valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                    }
                } else {
                    for (Value value : dagNode.getValues()) {
                        if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                            if (valueMap.containsKey(dagNode.getNumber())) {
                                res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                            } else {
                                res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.MINUS));
                                valueMap.put(dagNode.getNumber(), value);
                            }
                        }
                    }
                    if (!valueMap.containsKey(dagNode.getNumber())) {
                        res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.MINUS));
                        valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                    }
                }
            } else if (dagNode.getOperator().equals("MULTI")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.MULTI));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.MULTI));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("DIV")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.DIV));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.DIV));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("MOD")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.MOD));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.MOD));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("EQL")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.EQL));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.EQL));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("NEQ")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.NEQ));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.NEQ));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("GRE")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.GRE));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.GRE));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("LSS")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.LSS));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.LSS));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("LEQ")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.LEQ));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.LEQ));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("GEQ")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new BinaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.GEQ));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new BinaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), valueMap.get(dagNode.getChildren().get(1)), Operator.GEQ));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("NOT")) {
                for (Value value : dagNode.getValues()) {
                    if (out.contains(value.getName()) && valueTable.getOrDefault(value, dagNode.getNumber()) == dagNode.getNumber()) {
                        if (valueMap.containsKey(dagNode.getNumber())) {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getNumber()), Operator.PLUS));
                        } else {
                            res.add(new UnaryAssign(value, valueMap.get(dagNode.getChildren().get(0)), Operator.NOT));
                            valueMap.put(dagNode.getNumber(), value);
                        }
                    }
                }
                if (!valueMap.containsKey(dagNode.getNumber())) {
                    res.add(new UnaryAssign(dagNode.getValues().get(0), valueMap.get(dagNode.getChildren().get(0)), Operator.NOT));
                    valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
                }
            } else if (dagNode.getOperator().equals("Branch")) {
                res.add(new Branch(valueMap.get(dagNode.getChildren().get(0)), dagNode.getLabel(), true));
            } else if (dagNode.getOperator().equals("Jump")) {
                res.add(new Jump(dagNode.getLabel()));
            } else if (dagNode.getOperator().equals("Return")) {
                if (dagNode.getChildren().isEmpty()) {
                    res.add(new Return(null));
                } else {
                    res.add(new Return(valueMap.get(dagNode.getChildren().get(0))));
                }
            } else if (dagNode.getOperator().equals("Call")) {
                ArrayList<Value> params = new ArrayList<>();
                for (Integer child : dagNode.getChildren()) {
                    params.add(valueMap.get(child));
                }
                Value result = null;
//                System.out.println("---" + dagNode.getFunction() + dagNode.getValues());
                if (dagNode.getValues().size() > 0) {
                    result = dagNode.getValues().get(0);
                    valueMap.put(dagNode.getNumber(), result);
                }
                res.add(new FuncCall(params, dagNode.getFunction(), result));
            } else if (dagNode.getOperator().equals("PrintNumber")) {
                res.add(new PrintNumber(valueMap.get(dagNode.getChildren().get(0))));
            } else if (dagNode.getOperator().equals("PrintString")) {
                res.add(new PrintString(dagNode.getPrint()));
            } else if (dagNode.getOperator().equals("GetInt")) {
                res.add(new GetInt(dagNode.getValues().get(0)));
                valueMap.put(dagNode.getNumber(), dagNode.getValues().get(0));
            } else if (dagNode.getOperator().equals("Load")) {
                Value left = dagNode.getValues().get(0);
                left.setOffset(valueMap.get(dagNode.getChildren().get(1)));
                valueMap.put(dagNode.getNumber(), left);
            } else if (dagNode.getOperator().equals("Store")) {
                Value value = dagNode.getValues().get(0);
                value.setOffset(valueMap.get(dagNode.getChildren().get(0)));
                res.add(new UnaryAssign(value, valueMap.get(dagNode.getChildren().get(1)), Operator.PLUS));
            } else if (dagNode.getOperator().equals("Array")) {

            }
        }
        return res;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (DagNode dagNode : dagNodes) {
            stringBuilder.append(dagNode.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
