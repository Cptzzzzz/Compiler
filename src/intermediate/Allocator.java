package intermediate;

public class Allocator {
    private static int tableNumber=0;
    private static int variableNumber=0;
    public static int generateTableNumber(){
        ++tableNumber;
        return tableNumber;
    }
    public static String generateVariableName(){
        ++variableNumber;
        return String.format("temp%d",variableNumber);
    }
}