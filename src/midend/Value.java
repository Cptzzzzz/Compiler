package midend;

public class Value {
    private boolean isNumber;

    private int value;//if number, contains its value

    private String name;//if not number, contains its name
    private boolean isArray;
    private Value offset;
    private int space;
}
