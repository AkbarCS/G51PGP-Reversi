package reversi_pgp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JButton;

public class BoardSquare extends JButton implements Runnable
{
	/** Colour of main part */
	protected Color drawColor; 
	/** Colour of rectangle in the background */
	protected Color backgroundColor;
	/** Colour of circle border */
	protected Color drawCircleColor;
	/** Width of border in pixels */
	protected int borderSize; 
	
	protected int windowNum;
	protected int gridNum;
		
	public BoardSquare(int width, int height, Color color, int borderWidth, Color backgroundCol, Color borderCol, int windowNumber, int gridNumber)
	{
		borderSize = borderWidth;
		drawColor = color;
		drawCircleColor = borderCol;
		backgroundColor = backgroundCol;
		windowNum = windowNumber;
		gridNum = gridNumber;
		setMinimumSize( new Dimension(width, height) );
		setPreferredSize( new Dimension(width, height) );
		setMaximumSize( new Dimension(width, height) );
	}

	public BoardSquare( int width, int height, Color backgroundCol )
	{
		// Call the other constructor with some default values - this produces a green square with no circle present
		this( width, height, null, 0, backgroundCol, null, 3, 65);
	}
	
	/**
	 * This is called by the system and your code needs to draw the component
	 * @param g The graphics object that the systems gives you to draw to
	 */
	protected void paintComponent(Graphics g)
	{
		if ( backgroundColor != null )
		{
			g.setColor(backgroundColor);
			g.fillRect(0, 0, getWidth(), getHeight());		//background colour
		}
		if ( drawColor != null )
		{
			g.setColor(drawColor);							//circle colour
			g.fillOval(3, 3, getWidth()-6, getHeight()-6);				
			g.setColor(drawCircleColor);					//border colour
			g.drawOval(3, 3, getWidth()-6, getHeight()-6);	//draw border around circle
		}
	}	
	
	/** Ask the even thread to redraw this button now */
	public void redrawSelf() 
	{
		// First line was my first implementation - make event thread run this
		// EventQueue.invokeLater( new Runnable() { public void run() { repaint(); } } );
		// Then I just reused the current object and made it support the run function
		EventQueue.invokeLater(this);
	}
	
	/** Implemented run() in this object for passing to invokeLater() above */
	public void run() 
	{ 
		repaint(); 
	}
	
	/** Set the colour of the button AND ask it to redraw now */
	public void setColor(Color newColor) { drawColor = newColor; redrawSelf(); }
	
	/** Set the border colour of the button AND ask it to redraw now */
	public void setBorderColor(Color newColor) { backgroundColor = newColor; redrawSelf(); }

	/** Set the border width of the button AND ask it to redraw now */
	public void setBorderWidth(int newWidth) { borderSize = newWidth; redrawSelf(); }
	
	/** Set the colour of the circle border AND ask it to redraw now */
	public void CircleColor(Color newColor) { drawCircleColor = newColor; redrawSelf(); }
	
	/** Added to get rid of warning, for serialisation */
	private static final long serialVersionUID = 9041257494324082543L;

	/** Main just does a test of the class */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new BoardSquare(50,110,Color.BLACK,10,Color.GREEN, Color.WHITE, 3, 65));
		frame.pack();
		frame.setVisible(true);
		
		JFrame frame2 = new JFrame();
		frame2.add( new BoardSquare(50, 110, Color.GREEN));
		frame2.pack();
		frame2.setVisible(true);
		
		JFrame frame3 = new JFrame();
		frame3.add( new BoardSquare(50,110,Color.WHITE,10,Color.GREEN, Color.BLACK, 3, 65) );
		frame3.pack();
		frame3.setVisible(true);
	}	
}