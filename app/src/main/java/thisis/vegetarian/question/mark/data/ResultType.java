package thisis.vegetarian.question.mark.data;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class ResultType <T>{

    @Override
    public String toString() {
        if (this instanceof Success){
            return "Is Success class.";
        } else if (this instanceof Error){
            return "Is Error class.";
        }
        return "";
    }

    public final static class Success<T> extends ResultType{
        private T data;
        public Success(T data){ this.data = data;}

        public T getData() {
            return data;
        }
    }

    public final static class Error extends ResultType{
        private Exception exception;
        public Error(Exception e) { this.exception = e;}

        public Exception getException() {
            return exception;
        }
    }
}
