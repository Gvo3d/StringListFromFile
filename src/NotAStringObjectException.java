/**
 * Created by Gvozd on 04.02.2016.
 */
public class NotAStringObjectException extends Throwable {
    public NotAStringObjectException() {
        System.err.println("List has at least one not a string object!");
    }
}
