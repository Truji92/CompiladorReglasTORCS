package parser;

/**
 * Created by alejandro on 28/06/15.
 */
public class SemanticException extends Exception {
//TODO

    private Token token;

    private String msg;

    public SemanticException(Token token) {
        this.token = token;
        msg = token.getLexeme() + "    " + token.getRow();
    }

    public SemanticException(){msg = "balbalba";}

    @Override
    public String toString() {
        return msg;
    }
}
