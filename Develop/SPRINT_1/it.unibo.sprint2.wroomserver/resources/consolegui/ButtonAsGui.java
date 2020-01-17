package consolegui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class ButtonAsGui extends Observable implements  ActionListener{

//Factory method
public static ButtonAsGui createButton(  String[] cmd  ){
	Frame fr = Utils.initFrame(300,300);
	fr.setLayout(new GridLayout(2,3));
	ButtonAsGui button = new ButtonAsGui();
	for( int i=0; i<cmd.length;i++)
		new ButtonBasic(fr, cmd[i], button);	//button is the listener
	return button;
}

	@Override  //from ActionListener
	public void actionPerformed(ActionEvent e) {
		this.setChanged();
		this.notifyObservers(e.getActionCommand());
	}
}
