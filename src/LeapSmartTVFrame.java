import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LeapSmartTVFrame extends JFrame {
	// this line is needed to avoid serialization warnings  
    private static final long serialVersionUID = 1L;
    public JLabel lblChannel;
    public JLabel lblVolume;
    public JLabel lblBackground;
 
    LeapSmartTVFrame(String strImgPath, String strChannelLabel, String strVolumeLabel){
        this.setUndecorated(true);	// remove window frame 
        GraphicsEnvironment.getLocalGraphicsEnvironment().
        getDefaultScreenDevice().setFullScreenWindow(this); // switching to full screen mode
     	
    	setIconImage(Toolkit.getDefaultToolkit().getImage("./src/SmartTV.png"));
    	setTitle("SmartTV Mockup Screen");
    	setSize(1366, 768);

        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblBackground = new JLabel(new ImageIcon(strImgPath));
        lblBackground.setLayout(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();

        lblChannel = new JLabel(strChannelLabel);
        lblChannel.setFont(lblChannel.getFont().deriveFont(Font.BOLD, 48));
        lblChannel.setForeground(Color.BLUE);
        lblChannel.setHorizontalAlignment(JLabel.LEFT);
        lblChannel.setVerticalAlignment(JLabel.TOP);

        lblVolume = new JLabel(strVolumeLabel);
        lblVolume.setFont(lblVolume.getFont().deriveFont(Font.BOLD, 48));
        lblVolume.setForeground(Color.RED);
        lblVolume.setHorizontalAlignment(JLabel.RIGHT);
        lblVolume.setVerticalAlignment(JLabel.BOTTOM);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        add(lblVolume, c);
        add(lblBackground, c);
        lblBackground.add(lblChannel);
  
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

	public void ChangeChannel(int i)
	{
		lblBackground.setIcon(new ImageIcon("./src/JPGs/Channel (" + i +").jpg"));
		lblChannel.setText("Channel : " + i);
	}
	
	public void ChangeVolume(int i)
	{
		lblVolume.setText("Volume : " + i);
	}

    // Class constructor  
    LeapSmartTVFrame() {} 
}