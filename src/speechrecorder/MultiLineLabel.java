package speechrecorder;
import java.awt.ComponentOrientation;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JTextArea;

//by sindisil  see this post: http://www.gamedev.net/community/forums/topic.asp?topic_id=383461
@SuppressWarnings("serial")
public  class MultiLineLabel extends JTextArea {

  public MultiLineLabel(Container parent, String text,
                        int maxWidth, boolean labelFont, boolean rightToLeft) {
     super();
     JLabel lbl = new JLabel();
     setFocusable(false);
     setEditable(false);
     setBackground(lbl.getBackground());
     setLineWrap(true);
     setWrapStyleWord(true);
     if ( rightToLeft )
     {
    	 setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
         setFont(lbl.getFont().deriveFont(14.0f));
     }
     else
     {
    	 setFont(lbl.getFont());
     }
     setHighlighter(null);
     if (maxWidth > 0) {
         //setColumns(Math.min(maxWidth, text.length()));
         setColumns(Math.min(maxWidth, 100));
     }
     append((text == null || text.length() == 0) ? " " : text);
  }

  public MultiLineLabel(Container parent, String text, int maxWidth, boolean rightToLeft) {
     this(parent, text, maxWidth, true, rightToLeft);
  }
  
}
