package Car;

import javax.swing.text.*;

public class UppercaseDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        fb.insertString(offset, text.toUpperCase(), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        fb.replace(offset, length, text.toUpperCase(), attrs);
    }
}
