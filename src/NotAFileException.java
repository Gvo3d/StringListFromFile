/**
 * Created by Gvozd on 03.02.2016.
 */
public class NotAFileException extends Throwable {
    public NotAFileException(){
        System.err.println("Target file is a directory, wrong file name");
    }
}
