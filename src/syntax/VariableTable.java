package syntax;

import util.Error;
import util.ErrorWriter;

import java.util.ArrayList;

public class VariableTable {
    ArrayList<Variable> variables;
    int number;

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public int getNumber() {
        return number;
    }

    public boolean isGlobal(){
        return number==1;
    }
    public VariableTable(int depth) {
        variables = new ArrayList<>();
        this.number =depth;
    }

    public void add(ArrayList<Variable> variables, int line) {
        for (Variable variable1 : variables) {
            boolean flag = true;
            for (Variable variable2 : this.variables) {
                if (variable1.getName().equals(variable2.getName())) {
                    ErrorWriter.add(new Error(line, 'b'));
                    flag = false;
                    break;
                }
            }
            if (flag){
                variable1.setTableNumber(number);
                this.variables.add(variable1);
            }
        }
    }

    public void add(Variable variable, int line) {
        for (Variable variable1 : variables) {
            if (variable1.getName().equals(variable.getName())) {
                ErrorWriter.add(new Error(line, 'b'));
                return;
            }
        }
        variable.setTableNumber(number);
        variables.add(variable);
    }
    public void add(Variable variable) {
        variables.add(variable);
    }
    public boolean isExist(String name){
        for(Variable variable1:variables){
            if(variable1.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public int getDimension(String name){
        for(Variable variable1:variables){
            if(variable1.getName().equals(name)){
                return variable1.getDimensions().size();
            }
        }
        return 0;
    }

    public boolean isConst(String name){
        for(Variable variable1:variables){
            if(variable1.getName().equals(name)){
                return variable1.isConst();
            }
        }
        return false;
    }

    public int getValue(String name,ArrayList<Integer> args){
        for(Variable variable1:variables){
            if(variable1.getName().equals(name)){
                return variable1.getValue(args);
            }
        }
        return 0;
    }

    public Variable getVariableInstance(String name){
        for(Variable variable:variables){
            if(name.equals(variable.getName()))
                return variable;
        }
        return null;
    }

    public int getSpace(){
        int res=0;
        for(Variable variable:variables){
            res+=variable.getLength()*4;
        }
        return res;
    }
}
