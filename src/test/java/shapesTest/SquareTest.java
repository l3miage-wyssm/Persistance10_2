package shapesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import edu.uga.miage.m1.polygons.gui.persistence.Visitor;
import edu.uga.miage.m1.polygons.gui.shapes.Square;

class SquareTest {

	private int y = 50;

	private int x = 50;

	private String typeExpected = "square";

	private Square square;

	private JPanel mPanel;

	@Mock
	private Graphics2D g2D;

	private Graphics2D g2DExpected;

	@BeforeEach
	void setUp() {
		square = new Square(x, y);

		mPanel = new JPanel();
		mPanel.setBackground(Color.WHITE);
		mPanel.setLayout(null);
		mPanel.setMinimumSize(new Dimension(400, 400));

		g2D = mock(Graphics2D.class);

		g2DExpected = mock(Graphics2D.class);

		when(g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING)).thenReturn(RenderingHints.VALUE_ANTIALIAS_OFF);

		when(g2DExpected.getRenderingHint(RenderingHints.KEY_ANTIALIASING))
				.thenReturn(RenderingHints.VALUE_ANTIALIAS_OFF);

	}

	@Test
	void testConstruct() {
		assertEquals(square.getType(), typeExpected);
		assertEquals(square.getX(), this.x - 25);
		assertEquals(square.getY(), this.y - 25);
	}

	@Test
	void testDraw() {
		square.draw(g2D);

		float f = (float) x + 50;
		g2DExpected.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint gradient = new GradientPaint(x, y, Color.BLUE, f, y, Color.WHITE);
		g2DExpected.setPaint(gradient);
		g2DExpected.fill(new Rectangle2D.Double(x, y, 50, 50));
		BasicStroke wideStroke = new BasicStroke(2.0f);
		g2DExpected.setColor(Color.black);
		g2DExpected.setStroke(wideStroke);
		g2DExpected.draw(new Rectangle2D.Double(x, y, 50, 50));

		assertEquals(g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING),
				g2DExpected.getRenderingHint(RenderingHints.KEY_ANTIALIASING));
	}

	@Test
	void testAccept() {
		Visitor visitor = mock(Visitor.class);

		square.accept(visitor);

		verify(visitor, times(1)).visit(square);
	}

	@Test
	void testSetX() {
		square.setX(4);

		Assertions.assertEquals(4, square.getX());
	}

	@Test
	void testSetY() {
		square.setY(4);

		Assertions.assertEquals(4, square.getY());
	}

}