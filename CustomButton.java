package zeroxgame;

import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CustomButton extends BasicButtonUI {

	public void installUI(JComponent b) {
		super.installUI(b);
		b.setFocusable(false);
		b.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public void paint(Graphics g, JComponent b) {
		super.paint(g, b);

		Dimension dimension = b.getSize();

		int width = dimension.width;
		int height = dimension.height;

		Graphics2D g2d = (Graphics2D) g;
		BasicStroke roundLines = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		g2d.setStroke(roundLines);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(UIColors.Button.getColor());
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(UIColors.Border.getColor());
		g2d.drawRect(0, 0, width, height);

		if (b.getClientProperty("NoughtOrCross") != null) drawNoughtCross(g2d, b, width, height);
	}

	public void drawNoughtCross(Graphics2D g, JComponent button, int width, int height) {
		String symbol = (String) button.getClientProperty("NoughtOrCross");

		g.setColor(UIColors.NougthAndCross.getColor());

		if (symbol.equals("X")) {
			g.drawLine(20, 20, width - 20, height - 20);
			g.drawLine(20, height - 20, width - 20, 20);
		} else {
			g.drawArc(10, 10, 80, 80, 0, 360);
		}
	}
}
