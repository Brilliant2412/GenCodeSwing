package exceptions;

public class WrongExcelTypeException extends Exception {
    public String getMessage(){
        return "Wrong Excel Type";
    }
}
