package zeroxgame;
import java.awt.*;

enum UIColors {
	
	Button(new Color(0x00E1D9)),//0x1abc9c
	Border(new Color(0x34495e)),//0xF8EFBA
	NougthAndCross(new Color(0x34495e));
	
	Color color;
	
	UIColors(Color uicolor) {
		color = uicolor;
	}
	
	Color getColor() {
		return color;
	}
}
