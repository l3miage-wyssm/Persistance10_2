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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;

import edu.uga.miage.m1.polygons.gui.persistence.Visitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;

class CircleTest {

	private int y = 50;

	private int x = 50;

	private String typeExpected = "circle";

	private Circle circle;

	private JPanel mPanel;

	private Graphics2D g2D;

	private Graphics2D g2DExpected;

	@BeforeEach
	void setUp() {
		circle = new Circle(x, y);

		mPanel = new JPanel();
		mPanel.setBackground(Color.WHITE);
		mPanel.setLayout(null);
		mPanel.setMinimumSize(new Dimension(400, 400));

		// g2D = mock(Graphics2D.class);

		// g2DExpected = mock(Graphics2D.class);

		// when(g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING)).thenReturn(RenderingHints.VALUE_ANTIALIAS_OFF);

		// when(g2DExpected.getRenderingHint(RenderingHints.KEY_ANTIALIASING))
		// .thenReturn(RenderingHints.VALUE_ANTIALIAS_OFF);

	}

	@Test
	void testConstruct() {
		assertEquals(typeExpected, circle.getType());
		assertEquals(this.x - 25, circle.getX());
		assertEquals(this.y - 25, circle.getY());
	}

}