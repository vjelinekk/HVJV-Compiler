package compiler.error;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class LexerErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        System.err.println("Lexical error at line " + line + ":" + charPositionInLine + " " + msg);
        System.exit(EErrorCode.LEXER.getCode());
    }
}
