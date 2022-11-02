package syntax;

import util.Error;
import util.ErrorWriter;

import java.util.ArrayList;

public class VariableTable {
    ArrayList<Variable> variables;

    public VariableTable() {
        variables = new ArrayList<>();
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
            if (flag)
                this.variables.add(variable1);
        }
    }

    public void add(Variable variable, int line) {
        for (Variable variable1 : variables) {
            if (variable1.getName().equals(variable.getName())) {
                ErrorWriter.add(new Error(line, 'b'));
                return;
            }
        }
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


}