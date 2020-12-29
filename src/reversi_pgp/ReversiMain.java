package reversi_pgp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ReversiMain extends BoardSquare implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private boolean hasPiece;
	private static boolean enableWhiteAI;
	private static boolean enableBlackAI;
	
	private static String playerColour;
	
	static JFrame frame[] = new JFrame[2];	
	static JLabel[] Labels = new JLabel[2];
	
	static List<Integer> emptySquaresUpdated = new ArrayList<Integer>();

	static List<Integer> whitePieces = new ArrayList<Integer>();
	static List<Integer> blackPieces = new ArrayList<Integer>();
	
	static BoardSquare[] BoardSquareButtonsWhite = new BoardSquare[64];
	static BoardSquare[] BoardSquareButtonsBlack = new BoardSquare[64];

	ReversiMain(boolean hasPiece, Color pieceColor, Color borderCol, int i, int j) 			
	{	
		super(50,110,pieceColor,10,Color.GREEN,borderCol,i, j);
		addActionListener(this);			
	}
	
	public synchronized boolean containsPiece()
	{
		return hasPiece; 	
	}
	
	private synchronized void addPiece(boolean insertPiece, Color piece)
	{
		if (piece == Color.WHITE)
			{
			if (insertPiece == Boolean.TRUE)
				{
				hasPiece = Boolean.TRUE;
				super.setColor(Color.WHITE);
				super.CircleColor(Color.BLACK);
				}
			}
		else if (piece == Color.BLACK)
			{
			if (insertPiece == Boolean.TRUE)
				{
				hasPiece = Boolean.TRUE;
				super.setColor(Color.BLACK);
				super.CircleColor(Color.WHITE);
				}
			}		
	}
	
	private static boolean inBlackArray(int gridNumber) 
	{	
		for (int i=0; i<blackPieces.size(); i++) 
      		{
    	  	if (gridNumber == blackPieces.get(i)) 
	         	{
	        	return true;
	         	}
      		} 	
      	return false;
	}
	
	private static boolean inWhiteArray(int gridNumber) 
	{
		for (int i=0; i<whitePieces.size(); i++) 
			{
		    if (gridNumber == whitePieces.get(i)) 
		    	{
		    	return true;
		     	}
		    }
		return false;
	}

	private static void reversiLogic(int windowNumber, int gridNumber)
	{
		int directions[] = {-8, -7, 1, 9, 8, 7, -1, -9};
		
		//special case for left and right grids - override direction array
			if (gridNumber % 8 == 0)
				{
				directions = new int[] {-8, -7, 1, 9, 8};
				}
			else if (gridNumber == 7 || gridNumber == 15 || gridNumber == 23 || gridNumber == 31 || gridNumber == 39 || gridNumber == 47 || gridNumber == 55 || gridNumber == 63)
				{
				directions = new int[] {-8, 8, 7, -1, -9};
				}
								
		if (windowNumber == 0)
			{
			for(int i=0; i<directions.length; i++)
				{				
				int neighbour = gridNumber + directions[i];
				System.out.println("White player - checking black array for piece number: " + (63-neighbour));
				
				if (neighbour % 8 == 0)
					{
					if (directions[i] != 8 && directions[i] != -8)
						{
						neighbour = 500;
						}
					}
				
				if (neighbour == 7 || neighbour == 15 || neighbour == 23 || neighbour == 31 || neighbour == 39 || neighbour == 47 || neighbour == 55 || neighbour == 63) 
					{
					if (directions[i] != 8 && directions[i] != -8)
						{
						neighbour = 500;
						}
					}
				
				if (inBlackArray(63-neighbour) == Boolean.TRUE)
					{
					System.out.println("Black piece neighbour is in grid number: " + neighbour);
					List<Integer> blackToWhite = new ArrayList<Integer>();
					
					blackToWhite.add(neighbour);			//black piece grid number positions are in terms of positions as seen on the white player window
					
					int search = neighbour + directions[i];
					blackToWhite.add(search);			
					
					while (inBlackArray(63-search) == Boolean.TRUE || inWhiteArray(search) == Boolean.TRUE) 
						{
						if (search < 0)
							{
							break;
							}
						else if (search > 63)
							{
							break;
							}
						else if (inWhiteArray(search) == Boolean.TRUE)
							{
							System.out.println("White piece with black pieces in between is in grid number: " + search);
							
							for(int j = 0; j<blackToWhite.size(); j++)
								{
								int blackToWhite1 = blackToWhite.get(j);
								System.out.println("Black piece to capture is: " + blackToWhite1);
								for (int k=0; k<blackPieces.size(); k++)
									{
									if (blackPieces.get(k) == (63-blackToWhite1))
										{
										blackPieces.remove(k);
										}
									}
	
								whitePieces.add(blackToWhite1);
								}	
							
							//remove duplicate elements
							whitePieces = new ArrayList<Integer>(new LinkedHashSet<Integer>(whitePieces));
	
							blackToWhite.clear();
							break;
							}
						else if (search % 8 == 0 && inBlackArray(63-search) == Boolean.TRUE) 
							{
							if (directions[i] == -8 || directions[i] == 8)
								{
								search = search + directions[i]; 
								blackToWhite.add(search);
								}
							else
								{
								break;
								}
							}
						else if ((search == 7 || search == 15 || search == 23 || search == 31 || search == 39 || search == 47 || search == 55 || search == 63) && inBlackArray(63-search) == Boolean.TRUE)
							{
							if (directions[i] == -8 || directions[i] == 8)
								{
								search = search + directions[i];
								blackToWhite.add(search);
								}
							else
								{
								break;
								}
							}
						else
							{
							search = search + directions[i];
							blackToWhite.add(search);
							}
						}
					}
				}
			}
		else
			{			
			for(int i=0; i<directions.length; i++)
				{
				int neighbour = gridNumber + directions[i];		//piece number is relative to its position on the black window
				System.out.println("Black player - checking white array for piece number: " + (63-neighbour));
				
				if (neighbour % 8 == 0)
					{
					if (directions[i] != 8 && directions[i] != -8)
						{
						neighbour = 500;
						}
					}
			
				if (neighbour == 7 || neighbour == 15 || neighbour == 23 || neighbour == 31 || neighbour == 39 || neighbour == 47 || neighbour == 55 || neighbour == 63) 
					{
					if (directions[i] != 8 && directions[i] != -8)
						{
						neighbour = 500;
						}
					}
				
				if (inWhiteArray(63-neighbour) == Boolean.TRUE)
					{
					System.out.println("White piece neighbour is in grid number: " + neighbour);
					List<Integer> whiteToBlack = new ArrayList<Integer>();
					
					whiteToBlack.add(neighbour);
					
					int search = neighbour + directions[i];	
					whiteToBlack.add(search);					//piece position grid number as seen on the black player window
					
					while (inBlackArray(search) == Boolean.TRUE || inWhiteArray(63-search) == Boolean.TRUE) 
						{
						if (search < 0)
							{
							break;
							}
						else if (search > 63)
							{
							break;
							}
						else if (inBlackArray(search) == Boolean.TRUE)
							{
							System.out.println("Black piece with white pieces in between is in grid number: " + search);
							
							for(int j = 0; j<whiteToBlack.size(); j++)
								{
								int whiteToBlack1 = whiteToBlack.get(j);
								System.out.println("White piece to capture is: " + whiteToBlack1);
								for (int k=0; k<whitePieces.size(); k++)
									{
									if (whitePieces.get(k) == (63-whiteToBlack1))
										{
										whitePieces.remove(k);
										}
									}
								
								blackPieces.add(whiteToBlack1);
								}
							
							//remove duplicate elements
							blackPieces = new ArrayList<Integer>(new LinkedHashSet<Integer>(blackPieces));

							whiteToBlack.clear();
							break;
							}
						else if (search % 8 == 0 && inWhiteArray(63-search) == Boolean.TRUE)
							{
							if (directions[i] == -8 || directions[i] == 8)
								{
								search = search + directions[i];
								whiteToBlack.add(search);
								}
							else
								{
								break;
								}
							}
						else if ((search == 7 || search == 15 || search == 23 || search == 31 || search == 39 || search == 47 || search == 55 || search == 63) && inWhiteArray(63-search) == Boolean.TRUE)
							{
							if (directions[i] == -8 || directions[i] == 8)
								{
								search = search + directions[i];
								whiteToBlack.add(search);
								}
							else
								{
								break;
								}
							}
						else
							{
							search = search + directions[i];
							whiteToBlack.add(search);
							}
						}
					}
				}
			}
	}
	
	public void actionPerformed(ActionEvent e)			
	{
		if (hasPiece == Boolean.FALSE && this.windowNum == 0)
			{
			addPiece(true, Color.WHITE);
			this.setEnabled(false);
			whitePieces.add(this.gridNum);
			
			//reversi game logic
			reversiLogic(this.windowNum, this.gridNum);
			
			//redraw white player frame to show captured pieces
			for (int i=0; i<whitePieces.size(); i++)
				{
				int w = whitePieces.get(i);
				((ReversiMain) BoardSquareButtonsWhite[w]).addPiece(true, Color.WHITE);
				BoardSquareButtonsWhite[w].setEnabled(false);	
				}
			
			//add white pieces to black player grid
			for (int i=0; i<whitePieces.size(); i++)
				{
				int w = 63-(whitePieces.get(i));
				((ReversiMain) BoardSquareButtonsBlack[w]).addPiece(true, Color.WHITE);
				BoardSquareButtonsBlack[w].setEnabled(false);	
				}
									
			for (int j=0; j < 64; j++)
				{					
				BoardSquareButtonsWhite[j].setEnabled(false);
				}
			
			for (int k=0; k<whitePieces.size(); k++)
				{
				int w = 63-(whitePieces.get(k));
				BoardSquareButtonsBlack[w].setEnabled(false);
				}
			
			for (int l=0; l<blackPieces.size(); l++)
				{
				int b = blackPieces.get(l);
				BoardSquareButtonsBlack[b].setEnabled(false);
				}
			
			end();
			
			if (enableWhiteAI == Boolean.TRUE) 
				{
				Labels[0].setText("White player - not your turn");
				Labels[1].setText("Black player - click place to put piece");
				
				enableWhiteAI = Boolean.FALSE;
				enableBlackAI = Boolean.TRUE;
				
				validMoves(1);			//for black player window
				}
			}
		else 
			{
			addPiece(true, Color.BLACK);
			this.setEnabled(false);
			blackPieces.add(this.gridNum);
			
			//reversi game logic
			reversiLogic(this.windowNum, this.gridNum);
			
			//redraw black player frame to show captured pieces
			for (int i=0; i<blackPieces.size(); i++)
				{
				int w = blackPieces.get(i);
				((ReversiMain) BoardSquareButtonsBlack[w]).addPiece(true, Color.BLACK);
				BoardSquareButtonsBlack[w].setEnabled(false);	
				}
			
			//add black pieces to white player grid
			for (int i=0; i<blackPieces.size(); i++)
				{
				int b = 63-(blackPieces.get(i));
				((ReversiMain) BoardSquareButtonsWhite[b]).addPiece(true, Color.BLACK);
				BoardSquareButtonsWhite[b].setEnabled(false);	
				}
						
			for (int j=0; j < 64; j++)
				{	
				BoardSquareButtonsBlack[j].setEnabled(false);
				}
			
			for (int k=0; k<blackPieces.size(); k++)
				{
				int b = 63-(blackPieces.get(k));
				BoardSquareButtonsWhite[b].setEnabled(false);
				}
			
			for (int l=0; l<whitePieces.size(); l++)
				{
				int w = whitePieces.get(l);
				BoardSquareButtonsWhite[w].setEnabled(false);
				}
			
			end();
			
			if (enableBlackAI == Boolean.TRUE) 
				{
				Labels[0].setText("White player - click place to put piece");
				Labels[1].setText("Black player - not your turn");
				
				enableWhiteAI = Boolean.TRUE;
				enableBlackAI = Boolean.FALSE;
				
				validMoves(0);  		//for white player window
				}
			}
		
		System.out.println("Window number clicked is = " + this.windowNum);
		System.out.println("Grid number clicked is = " + this.gridNum);	
	}
	
	private static void validMoves(int windowNumber)
	{
		if (windowNumber == 0)			//valid moves for white
			{
			emptySquaresUpdated.clear();
			List<Integer> emptySquares = new ArrayList<Integer>();

			for (int i=0; i<64; i++)
				{
				if (inWhiteArray(i) == Boolean.FALSE && inBlackArray(63-i) == Boolean.FALSE)
					{
					emptySquares.add(i);
					}
				}
			
			for (int i=0; i<emptySquares.size(); i++)
				{
				int emptySquaresNumber = emptySquares.get(i);
				int directions[] = {-8, -7, 1, 9, 8, 7, -1, -9};
				
				if (emptySquaresNumber % 8 == 0)
					{
					directions = new int[] {-8, -7, 1, 9, 8};
					}
				else if (emptySquaresNumber == 7 || emptySquaresNumber == 15 || emptySquaresNumber == 23 || emptySquaresNumber == 31 || emptySquaresNumber == 39 || emptySquaresNumber == 47 || emptySquaresNumber == 55 || emptySquaresNumber == 63)
					{
					directions = new int[] {-8, 8, 7, -1, -9};
					}
				
				for(int j=0; j<directions.length; j++)
					{
					int neighbour = emptySquaresNumber + directions[j];
					
					if (neighbour % 8 == 0)
						{
						if (directions[j] != 8 && directions[j] != -8)
							{
							neighbour = 500;
							}
						}
				
					if (neighbour == 7 || neighbour == 15 || neighbour == 23 || neighbour == 31 || neighbour == 39 || neighbour == 47 || neighbour == 55 || neighbour == 63) 
							{
							if (directions[j] != 8 && directions[j] != -8)
								{
								neighbour = 500;
								}
							}
					
					if (inBlackArray(63-neighbour) == Boolean.TRUE)
						{
						int search = neighbour + directions[j];
												
						while (inBlackArray(63-search) == Boolean.TRUE || inWhiteArray(search) == Boolean.TRUE) 
							{
							if (search < 0)
								{
								break;
								}
							else if (search > 63)
								{
								break;
								}
							else if (inWhiteArray(search) == Boolean.TRUE)
								{
								emptySquaresUpdated.add(emptySquaresNumber);
								break;
								}
							else if (search % 8 == 0 && inBlackArray(63-search) == Boolean.TRUE) 
								{
								if (directions[j] == -8 || directions[j] == 8)
									{
									search = search + directions[j]; 
									}
								else
									{
									break;
									}
								}
							else if ((search == 7 || search == 15 || search == 23 || search == 31 || search == 39 || search == 47 || search == 55 || search == 63) && inBlackArray(63-search) == Boolean.TRUE)
								{
								if (directions[j] == -8 || directions[j] == 8)
									{
									search = search + directions[j];
									}
								else
									{
									break;
									}
								}
							else
								{
								search = search + directions[j];
								}
							}
						}		
					}
				}
					
			if (emptySquaresUpdated.size() == 0)
				{
				//return control to black player
				System.out.println("Returned control to black player\n");
				validMoves(1);

				Labels[0].setText("White player - not your turn");
				Labels[1].setText("Black player - click place to put piece");
				
				enableWhiteAI = Boolean.FALSE;
				enableBlackAI = Boolean.TRUE;
				}
			else 
				{
				for (int i=0; i<emptySquaresUpdated.size(); i++)
					{
					BoardSquareButtonsWhite[emptySquaresUpdated.get(i)].setEnabled(true);
					}	
				}			
			}
		else							//valid moves for black
			{
			emptySquaresUpdated.clear();
			List<Integer> emptySquares = new ArrayList<Integer>();

			for (int i=0; i<64; i++)
				{
				if (inBlackArray(i) == Boolean.FALSE && inWhiteArray(63-i) == Boolean.FALSE)
					{
					emptySquares.add(i);
					}
				}
						
			for (int i=0; i<emptySquares.size(); i++)
				{
				int emptySquaresNumber = emptySquares.get(i);
				int directions[] = {-8, -7, 1, 9, 8, 7, -1, -9};
				
				if (emptySquaresNumber % 8 == 0)
					{
					directions = new int[] {-8, -7, 1, 9, 8};
					}
				else if (emptySquaresNumber == 7 || emptySquaresNumber == 15 || emptySquaresNumber == 23 || emptySquaresNumber == 31 || emptySquaresNumber == 39 || emptySquaresNumber == 47 || emptySquaresNumber == 55 || emptySquaresNumber == 63)
					{
					directions = new int[] {-8, 8, 7, -1, -9};
					}
				
				for(int j=0; j<directions.length; j++)
					{
					int neighbour = emptySquaresNumber + directions[j];
					
					if (neighbour % 8 == 0)
						{
						if (directions[j] != 8 && directions[j] != -8)
							{
							neighbour = 500;
							}
						}
				
					if (neighbour == 7 || neighbour == 15 || neighbour == 23 || neighbour == 31 || neighbour == 39 || neighbour == 47 || neighbour == 55 || neighbour == 63) 
						{
						if (directions[j] != 8 && directions[j] != -8)
							{
							neighbour = 500;
							}
						}
					
					if (inWhiteArray(63-neighbour) == Boolean.TRUE)
						{
						int search = neighbour + directions[j];
												
						while (inBlackArray(search) == Boolean.TRUE || inWhiteArray(63-search) == Boolean.TRUE) 	
							{
							if (search < 0)
								{
								break;
								}
							else if (search > 63)
								{
								break;
								}
							else if (inBlackArray(search) == Boolean.TRUE)
								{
								emptySquaresUpdated.add(emptySquaresNumber);
								break;
								}
							else if (search % 8 == 0 && inWhiteArray(63-search) == Boolean.TRUE) 
								{
								if (directions[j] == -8 || directions[j] == 8)
									{
									search = search + directions[j]; 
									}
								else
									{
									break;
									}
								}
							else if ((search == 7 || search == 15 || search == 23 || search == 31 || search == 39 || search == 47 || search == 55 || search == 63) && inWhiteArray(63-search) == Boolean.TRUE)
								{
								if (directions[j] == -8 || directions[j] == 8)
									{
									search = search + directions[j];
									}
								else
									{
									break;
									}
								}
							else
								{
								search = search + directions[j];
								}
							}
						}		
					}
				}
			
			if (emptySquaresUpdated.size() == 0)
				{
				//return control to white player
				System.out.println("Returned control to white player\n");
				validMoves(0);
				
				Labels[0].setText("White player - click place to put piece");
				Labels[1].setText("Black player - not your turn");
				
				enableWhiteAI = Boolean.TRUE;
				enableBlackAI = Boolean.FALSE;
				}
			else
				{ 
				for (int i=0; i<emptySquaresUpdated.size(); i++)
					{
					BoardSquareButtonsBlack[emptySquaresUpdated.get(i)].setEnabled(true);
					}
				}
			} 
	}
	
	private static JPanel createGrid(int i) 
	{
		JPanel panel3 = new JPanel();
						
		if (i == 0)
			{	
			int j;	
			panel3.setLayout(new GridLayout(8,8));
			for (j=0; j < 64; j++)
				{			
				BoardSquareButtonsWhite[j] = new ReversiMain(Boolean.TRUE, null, null, i, j);
				((ReversiMain) BoardSquareButtonsWhite[j]).addPiece(false, null);
				panel3.add(BoardSquareButtonsWhite[j]);				
				BoardSquareButtonsWhite[j].setEnabled(false);
				}
			
			((ReversiMain) BoardSquareButtonsWhite[27]).addPiece(true, Color.WHITE);
			BoardSquareButtonsWhite[27].setEnabled(false);
			whitePieces.add(27);
			
			((ReversiMain) BoardSquareButtonsWhite[36]).addPiece(true, Color.WHITE);
			BoardSquareButtonsWhite[36].setEnabled(false);
			whitePieces.add(36);

			((ReversiMain) BoardSquareButtonsWhite[28]).addPiece(true, Color.BLACK);
			BoardSquareButtonsWhite[28].setEnabled(false);
			blackPieces.add(28);
			
			((ReversiMain) BoardSquareButtonsWhite[35]).addPiece(true, Color.BLACK);
			BoardSquareButtonsWhite[35].setEnabled(false);
			blackPieces.add(35);
			
			BoardSquareButtonsWhite[20].setEnabled(true);
			BoardSquareButtonsWhite[29].setEnabled(true);
			BoardSquareButtonsWhite[34].setEnabled(true);
			BoardSquareButtonsWhite[43].setEnabled(true);
			
			emptySquaresUpdated.add(20);
			emptySquaresUpdated.add(29);
			emptySquaresUpdated.add(34);
			emptySquaresUpdated.add(43);

			return panel3;						
			}
		else if (i == 1)
			{	
			int j;		
			panel3.setLayout(new GridLayout(8,8));
			for (j=0; j < 64; j++)
				{
				BoardSquareButtonsBlack[j] = new ReversiMain(Boolean.TRUE, null, null, i, j);
				((ReversiMain) BoardSquareButtonsBlack[j]).addPiece(false, null);
				panel3.add(BoardSquareButtonsBlack[j]);
				BoardSquareButtonsBlack[j].setEnabled(false);
				}
	
			((ReversiMain) BoardSquareButtonsBlack[27]).addPiece(true, Color.WHITE);
			BoardSquareButtonsBlack[27].setEnabled(false);
			
			((ReversiMain) BoardSquareButtonsBlack[36]).addPiece(true, Color.WHITE);
			BoardSquareButtonsBlack[36].setEnabled(false);

			((ReversiMain) BoardSquareButtonsBlack[28]).addPiece(true, Color.BLACK);
			BoardSquareButtonsBlack[28].setEnabled(false);
			
			((ReversiMain) BoardSquareButtonsBlack[35]).addPiece(true, Color.BLACK);
			BoardSquareButtonsBlack[35].setEnabled(false);
			
			return panel3;
			}
		else 
			{
			return panel3;
			}
	}
		
	private static void createFrames() 
	{
	frame[0] = new JFrame();		
	frame[1] = new JFrame();
			
	for (int i = 0; i < 2; i++) {
		frame[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame[i].setLocationRelativeTo(null); 
		frame[i].setLayout(new FlowLayout());
		
		if(i == 0)
			{
			playerColour="Reversi-White Player";
			}
		else
			{
			playerColour="Reversi-Black Player";
			}
		
		frame[i].setTitle(playerColour);
		frame[i].setSize(20, 30); 
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = createGrid(i);
		JPanel panel4 = new JPanel();
		
		frame[i].setLayout(new BorderLayout(2,2));
		
		panel1.setLayout(new BorderLayout(15,0)); 		
		panel2.setLayout(new GridLayout(1,1));
		panel3.setLayout(new GridLayout(8,8));
		
		frame[i].add(panel1, BorderLayout.CENTER);
		
		panel1.add(panel2, BorderLayout.NORTH);
		panel1.add(panel3, BorderLayout.CENTER);
		panel1.add(panel4, BorderLayout.SOUTH);
		
		
		if (i == 0)
			{
			Labels[0] = new JLabel("White player - click place to put piece");
			panel2.add(Labels[0]);
			}
		else
			{
			Labels[1] = new JLabel("Black player - not your turn");
			panel2.add(Labels[1]);
			}
				
			if(i == 0)
				{
				JButton buttonWhite = new JButton("Greedy AI (Play White)");				
				buttonWhite.addActionListener(new ActionListener() 
					{
				public void actionPerformed(ActionEvent evt) 
					  	{
					if (enableWhiteAI == Boolean.FALSE && enableBlackAI == Boolean.FALSE) 
					  	{
						buttonWhite.setEnabled(false);
					  	}
					else if (enableWhiteAI == Boolean.FALSE) 
					  	{
					    JOptionPane.showMessageDialog(null, "White AI is disabled - not white player's turn"); 
					    }
					else 
					  	{
					    List<Integer> numberOfCaptures = new ArrayList<Integer>();
					    int greedyAINumber = 0;
					    int c = 0;
					    
					    if (emptySquaresUpdated.size() == 1)
					    	{
					    	greedyAINumber = emptySquaresUpdated.get(0);
					    	}
					    
					    else 
						    {
						    for (int i=0; i<emptySquaresUpdated.size(); i++)
						    	{
							    int directions[] = {-8, -7, 1, 9, 8, 7, -1, -9};
								int gridNumber = emptySquaresUpdated.get(i);		
							    
								if (i != 0)
									{
									numberOfCaptures.add(c);
									c=0;
									}
	
							    //special case for left and right grids - override direction array
								if (gridNumber % 8 == 0)
									{
									directions = new int[] {-8, -7, 1, 9, 8};
									}
								else if (gridNumber == 7 || gridNumber == 15 || gridNumber == 23 || gridNumber == 31 || gridNumber == 39 || gridNumber == 47 || gridNumber == 55 || gridNumber == 63)
									{
									directions = new int[] {-8, 8, 7, -1, -9};
									}
									
								for(int i2=0; i2<directions.length; i2++)
									{				
									int neighbour = gridNumber + directions[i2];
										
									if (inBlackArray(63-neighbour) == Boolean.TRUE)
										{
										List<Integer> blackToWhite = new ArrayList<Integer>();
		
										blackToWhite.add(neighbour);			//black piece grid number positions are in terms of positions as seen on the white player window
										
										int search = neighbour + directions[i2];
										blackToWhite.add(search);			
															
										while (inBlackArray(63-search) == Boolean.TRUE || inWhiteArray(search) == Boolean.TRUE) 
											{
											if (search < 0)
												{
												break;
												}
											else if (search > 63)
												{
												break;
												}
											else if (inWhiteArray(search) == Boolean.TRUE)
												{
												c += blackToWhite.size();
												blackToWhite.clear();
												break;
												}
											else if (search % 8 == 0 && inBlackArray(63-search) == Boolean.TRUE) 
												{
												if (directions[i2] == -8 || directions[i2] == 8)
													{
													search = search + directions[i2];
													blackToWhite.add(search);
													}
												else
													{
													break;
													}
												}
											else if ((search == 7 || search == 15 || search == 23 || search == 31 || search == 39 || search == 47 || search == 55 || search == 63) && inBlackArray(63-search) == Boolean.TRUE)
												{
												if (directions[i2] == -8 || directions[i2] == 8)
													{
													search = search + directions[i2];
													blackToWhite.add(search);
													}
												else
													{
													break;
													}
												}
											else
												{
												search = search + directions[i2];
												blackToWhite.add(search);
												}
											}
										}		
									}		
						    	}
						   					    	
						  	//get position of largest element in numberofcaptures array  
						  	int max = Collections.max(numberOfCaptures);
						  	System.out.println("max number is: " + max);	
						  	for (int i=0; i<numberOfCaptures.size(); i++)
							  	{
								if (max == numberOfCaptures.get(i))
									{
									greedyAINumber = emptySquaresUpdated.get(i);
									break;
									}
							  	}
						    } 
					    
					  	((ReversiMain) BoardSquareButtonsWhite[greedyAINumber]). addPiece(true, Color.WHITE);
					  	BoardSquareButtonsWhite[greedyAINumber].setEnabled(false);
					  	whitePieces.add(greedyAINumber);
					    
					  	reversiLogic(0, greedyAINumber);
					  
					  	//redraw white player frame to show captured pieces
					  	for (int i=0; i<whitePieces.size(); i++)
							{
							int w = whitePieces.get(i);
							((ReversiMain) BoardSquareButtonsWhite[w]).addPiece(true, Color.WHITE);
							BoardSquareButtonsWhite[w].setEnabled(false);	
							}
					  
					  	//add white pieces to black player grid
						for (int i=0; i<whitePieces.size(); i++)
							{
							int w = 63-(whitePieces.get(i));
							((ReversiMain) BoardSquareButtonsBlack[w]).addPiece(true, Color.WHITE);
							BoardSquareButtonsBlack[w].setEnabled(false);	
							}
											
						for (int j=0; j < 64; j++)
							{					
							BoardSquareButtonsWhite[j].setEnabled(false);
							//BoardSquareButtonsBlack[j].setEnabled(true);
							}
						
						for (int k=0; k<whitePieces.size(); k++)
							{
							int w = 63-(whitePieces.get(k));
							BoardSquareButtonsBlack[w].setEnabled(false);
							}
						
						for (int l=0; l<blackPieces.size(); l++)
							{
							int b = blackPieces.get(l);
							BoardSquareButtonsBlack[b].setEnabled(false);
							}
						
						end();
						
						if (enableWhiteAI == Boolean.TRUE) 
							{
							Labels[0].setText("White player - not your turn");
							Labels[1].setText("Black player - click place to put piece");
							
							enableWhiteAI = Boolean.FALSE;
							enableBlackAI = Boolean.TRUE;
							validMoves(1);			//for black player window
							}
					  }
					}
				}); 
		        buttonWhite.setPreferredSize(new Dimension(370, 40));
				panel4.add(buttonWhite);
				}
			else
				{
				JButton buttonBlack = new JButton("Greedy AI (Play Black)");
				buttonBlack.addActionListener(new ActionListener() 
					{
				public void actionPerformed(ActionEvent evt) 
						{
					if (enableBlackAI == Boolean.FALSE && enableWhiteAI == Boolean.FALSE) 
					  	{
						buttonBlack.setEnabled(false);
						} 	 
					else if (enableBlackAI == Boolean.FALSE) 
					  	{
				    	JOptionPane.showMessageDialog(null, "Black AI is disabled - not black player's turn"); 
					  	}  
					else 
					    {
					    List<Integer> numberOfCaptures = new ArrayList<Integer>();
					    int greedyAINumber = 0;
					    int c = 0;
					    
					    if (emptySquaresUpdated.size() == 1)
					    	{
					    	greedyAINumber = emptySquaresUpdated.get(0);
					    	}
					    else 
						    {
						    for (int i=0; i<emptySquaresUpdated.size(); i++)
						    	{
							    int directions[] = {-8, -7, 1, 9, 8, 7, -1, -9};
								int gridNumber = emptySquaresUpdated.get(i);		
							    
								if (i != 0)
									{
									numberOfCaptures.add(c);
									c=0;
									}
	
							    //special case for left and right grids - override direction array
								if (gridNumber % 8 == 0)
									{
									directions = new int[] {-8, -7, 1, 9, 8};
									}
								else if (gridNumber == 7 || gridNumber == 15 || gridNumber == 23 || gridNumber == 31 || gridNumber == 39 || gridNumber == 47 || gridNumber == 55 || gridNumber == 63)
									{
									directions = new int[] {-8, 8, 7, -1, -9};
									}
									
								for(int i2=0; i2<directions.length; i2++)
									{				
									int neighbour = gridNumber + directions[i2];
										
									if (inWhiteArray(63-neighbour) == Boolean.TRUE)
										{
										List<Integer> whiteToBlack = new ArrayList<Integer>();
		
										whiteToBlack.add(neighbour);			//white piece grid number positions are in terms of positions as seen on the black player window
										
										int search = neighbour + directions[i2];
										whiteToBlack.add(search);			
															
										while (inWhiteArray(63-search) == Boolean.TRUE || inBlackArray(search) == Boolean.TRUE) 
											{
											if (search < 0)
												{
												break;
												}
											else if (search > 63)
												{
												break;
												}
											else if (inBlackArray(search) == Boolean.TRUE)
												{
												c += whiteToBlack.size();
												whiteToBlack.clear();
												break;
												}
											else if (search % 8 == 0 && inWhiteArray(63-search) == Boolean.TRUE) 
												{
												if (directions[i2] == -8 || directions[i2] == 8)
													{
													search = search + directions[i2];
													whiteToBlack.add(search);
													}
												else
													{
													break;
													}
												}
											else if ((search == 7 || search == 15 || search == 23 || search == 31 || search == 39 || search == 47 || search == 55 || search == 63) && inWhiteArray(63-search) == Boolean.TRUE)
												{
												if (directions[i2] == -8 || directions[i2] == 8)
													{
													search = search + directions[i2];
													whiteToBlack.add(search);
													}
												else
													{
													break;
													}
												}
											else
												{
												search = search + directions[i2];
												whiteToBlack.add(search);
												}
											}
										}		
									}		
						    	}
						  
						  //get position of largest element in numberofcaptures array  
						  int max = Collections.max(numberOfCaptures);
						  System.out.println("max number is: " + max);
						 
						  for (int i=0; i<numberOfCaptures.size(); i++)
						  	{
							if (max == numberOfCaptures.get(i))
								{
								greedyAINumber = emptySquaresUpdated.get(i);
								break;
								}
						  	}
						    }
						  
					  ((ReversiMain) BoardSquareButtonsBlack[greedyAINumber]).addPiece(true, Color.BLACK);
					  BoardSquareButtonsBlack[greedyAINumber].setEnabled(false);
					  blackPieces.add(greedyAINumber);
						
					  //reversi game logic
					  reversiLogic(1, greedyAINumber);
						
					  //redraw black player frame to show captured pieces
					  for (int i=0; i<blackPieces.size(); i++)
							{
							int w = blackPieces.get(i);
							((ReversiMain) BoardSquareButtonsBlack[w]).addPiece(true, Color.BLACK);
							BoardSquareButtonsBlack[w].setEnabled(false);	
							}
						
					  //add black pieces to white player grid
					  for (int i=0; i<blackPieces.size(); i++)
							{
							int b = 63-(blackPieces.get(i));
							((ReversiMain) BoardSquareButtonsWhite[b]).addPiece(true, Color.BLACK);
							BoardSquareButtonsWhite[b].setEnabled(false);	
							}
									
					  for (int j=0; j < 64; j++)
							{	
							BoardSquareButtonsBlack[j].setEnabled(false);
							}
						
					  for (int k=0; k<blackPieces.size(); k++)
							{
							int b = 63-(blackPieces.get(k));
							BoardSquareButtonsWhite[b].setEnabled(false);
							}
						
					  for (int l=0; l<whitePieces.size(); l++)
							{
							int w = whitePieces.get(l);
							BoardSquareButtonsWhite[w].setEnabled(false);
							}
						
					  end();
						
					  if (enableBlackAI == Boolean.TRUE) 
					  		{
							Labels[0].setText("White player - click place to put piece");
							Labels[1].setText("Black player - not your turn");
								
							enableWhiteAI = Boolean.TRUE;
							enableBlackAI = Boolean.FALSE;
							validMoves(0);  		//for white player window
					    	}
					    } 
					}
				}); 
		        
				buttonBlack.setPreferredSize(new Dimension(370, 40));
				panel4.add(buttonBlack);
				}
		
			frame[i].setSize(400, 500); 
			frame[i].setVisible(true); 
			
			enableWhiteAI = Boolean.TRUE;
			enableBlackAI = Boolean.FALSE;
			}	
	}
	
	private static void end()
	{
		int end = (whitePieces.size() + blackPieces.size())-1;
		if (end >= 63)
			{
			if (whitePieces.size() > blackPieces.size())
				{
				String st = "White wins:" + whitePieces.size() + ":" + blackPieces.size(); 
				JOptionPane.showMessageDialog(null,st); 
				
				for (int j=0; j < 64; j++)
					{					
					BoardSquareButtonsWhite[j].setEnabled(false);
					BoardSquareButtonsBlack[j].setEnabled(false);
					}
				
				enableWhiteAI = Boolean.FALSE;
				enableBlackAI = Boolean.FALSE;
				}
			else
				{
				String st = "Black wins:" + whitePieces.size() + ":" + blackPieces.size(); 
				JOptionPane.showMessageDialog(null,st); 
				
				for (int j=0; j < 64; j++)
					{					
					BoardSquareButtonsWhite[j].setEnabled(false);
					BoardSquareButtonsBlack[j].setEnabled(false);
					}
				
				enableWhiteAI = Boolean.FALSE;
				enableBlackAI = Boolean.FALSE;
				}
			}	
	}

	public static void main(String[] args) 
	{		
		createFrames();
	}
}