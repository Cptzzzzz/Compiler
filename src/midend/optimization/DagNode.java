package midend.optimization;

import midend.util.Value;

import java.util.ArrayList;

public class DagNode {
    private String operator;
    /* PLUS MINUS MULTI DIV MOD EQL NEQ GRE LSS LEQ GEQ NOT Branch Jump Return Call PrintNumber PrintString
     *  GetInt Label Load Store None Array
     * */
    private ArrayList<Integer> children;//子节点
    private int number;
    private ArrayList<Value> values;//当前节点的变量 最终确定一个变量来当作这个节点的值
    private String function;//函数名
    private String print;//打印的字符串
    private String label;//label名

    public ArrayList<Value> getValues() {
        return values;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public DagNode(int number, String operator) {
        this.number = number;
        this.operator = operator;
        children = new ArrayList<>();
        values = new ArrayList<>();
    }

    public String getOperator() {
        return operator;
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }

    public int getNumber() {
        return number;
    }

    public void addValue(Value value) {
        if (!values.contains(value))
            values.add(value);
    }

    public String toString() {
        return String.format("Number: %d", number) +
                String.format(" Operator: %s\n", operator) +
                String.format("Values: %s\n", values.toString())+
                String.format("Children: %s\n", children.toString())+
                function+" "+print+" "+label;
    }
}
