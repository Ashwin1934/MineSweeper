import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public class Minesweeper extends JPanel implements ActionListener, MouseListener{

	JFrame frame;
	JMenuBar menuBar;
	JMenu menu, menuIcon, menuControls;
	JMenuItem item1,item2,item3;
	int dimR=9;
	int dimC=9;
	JPanel panel;
	JToggleButton[][] togglers;
	int [][] mineArray;
	ImageIcon mine,flag,redMine;
	ImageIcon [] num;
	boolean firstClick=true;
	int correctFlags = 0;
	Timer timer = null;
	boolean gameEnd = false;


	public Minesweeper(){

		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(40*dimC,40*dimR);

		menuBar = new JMenuBar();
		menu = new JMenu("Game");
		item1 = new JMenuItem("Beginner");
		item2 = new JMenuItem("Intermediate");
		item3 = new JMenuItem("Expert");


		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menuBar.add(menu);
		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);

		menuIcon = new JMenu("Icons");
		menuBar.add(menuIcon);
		menuControls = new JMenu("Controls");
		menuBar.add(menuControls);

		frame.add(menuBar,BorderLayout.NORTH);
		panel = new JPanel();
		panel.setLayout(new GridLayout(dimR,dimC));

		mineArray = new int[dimR][dimC];
		togglers = new JToggleButton[dimR][dimC];
		for(int i = 0;i<dimR;i++)
			for(int j = 0;j<dimC;j++){
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		frame.add(panel,BorderLayout.CENTER);

		mine = new ImageIcon("mine.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance(35,35,Image.SCALE_SMOOTH));
		flag = new ImageIcon("flag.png");
		flag = new ImageIcon(flag.getImage().getScaledInstance(35,35,Image.SCALE_SMOOTH));
		redMine = new ImageIcon("redMine.png");
		redMine = new ImageIcon(redMine.getImage().getScaledInstance(35,35,Image.SCALE_SMOOTH));

		num = new ImageIcon[9];
		for(int x  =1;x<9;x++){
			num[x] = new ImageIcon("number"+x+".png");
			num[x] = new ImageIcon(num[x].getImage().getScaledInstance(35,35,Image.SCALE_SMOOTH));
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}



	public void actionPerformed(ActionEvent e){
		if(e.getSource()==item1){
			dimR = 9;
			dimC = 9;
			//ten mines

		}
		if(e.getSource()==item2){
			dimR = 16;
			dimC = 16;
			//40 mines
		}
		if(e.getSource()==item3){
			dimR = 16;
			dimC = 30;
			//99mines
		}
				frame.setSize(40*dimC,40*dimR);

		mineArray=new int[dimR][dimC];
		frame.remove(panel);
		panel=new JPanel();
		panel.setLayout(new GridLayout(dimR,dimC));
		//for loop for buttons add
		togglers = new JToggleButton[dimR][dimC];
		for(int i = 0;i<dimR;i++)
			for(int j = 0;j<dimC;j++){
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		//re-add the panel to the frame
		frame.add(panel,BorderLayout.CENTER);
		frame.revalidate();

	}
	public void mouseClicked(MouseEvent e){

	}
	public void mousePressed(MouseEvent e){



		if(e.getButton() == MouseEvent.BUTTON1){
			for(int i = 0;i<togglers.length;i++)
				for(int j = 0;j<togglers[0].length;j++)
				{
					if(e.getSource() == togglers[i][j])
					{
							if(mineArray[i][j]==-1)
							{
								togglers[i][j].setSelected(true);
								togglers[i][j].setIcon(redMine);

								//exposes all mines after clicking one
								for(int x = 0;x<mineArray.length;x++)
								{
									for(int y = 0;y<mineArray[0].length;y++)
									{
										if(mineArray[x][y]==-1 && (x!=i || y!=j))
										{
											togglers[x][y].setSelected(true);
											togglers[x][y].setIcon(mine);

											//open up the whole grid and end the game
											System.out.println("Game Over");
										}
									}
								}
							}else	expand(i,j);

						}else{
							togglers[i][j].setSelected(false);
							togglers[i][j].setIcon(null);
						}

					}
				}

		if(e.getButton() == MouseEvent.BUTTON3){
			if(firstClick)
			{

					int upperLimit = 0;
					int lowerLimit = 0;
					int numOfMines = 0;
					if(dimR == 9){
						lowerLimit = 10;
						upperLimit = 10;
						numOfMines = 10;
					}
					if(dimC == 16){
						lowerLimit = 17;
						upperLimit = 17;
						numOfMines = 40;
					}
					if(dimC == 30){
						lowerLimit = 17;
						upperLimit = 31;
						numOfMines = 99;
					}
					int randRow = (int)(Math.random()*lowerLimit);
					int randCol = (int)(Math.random()*upperLimit);

					for(int i = 0;i<numOfMines;i++){
						do{
							 randRow = (int)(Math.random()*dimR);
						 	 randCol = (int)(Math.random()*dimC);
						}while(mineArray[randRow][randCol]==-1);

						mineArray[randRow][randCol]=-1;
						//togglers[randRow][randCol].setIcon(mine);
					}
					firstClick=false;
					for(int x=0;x<mineArray.length;x++)
					{

						for(int y=0;y<mineArray[0].length;y++)
						{
							if(mineArray[x][y]!=-1)
							{
								int mineCount=0;
								for(int r=x-1;r<=x+1;r++)
									for(int c=y-1;c<=y+1;c++)
									{
										if(r>=0 && r<dimR && c>=0 && c<dimC && mineArray[r][c]==-1)
											mineCount++;
									}
								mineArray[x][y]=mineCount;
							}
						}


					}
					for(int x=0;x<mineArray.length;x++)
					{
						for(int y=0;y<mineArray[0].length;y++)
						{
							System.out.print(mineArray[x][y]+" ");
						}
						System.out.println();
					}

			}
			for(int i = 0;i<togglers.length;i++)
			{
				for(int j = 0;j<togglers[0].length;j++)
				{
					if(e.getSource() == togglers[i][j])
					{
						if(!togglers[i][j].isSelected())
						{
							togglers[i][j].setIcon(flag);
							if(mineArray[i][j]==-1)
							{
								//togglers[i][j].setSelected(true);

								System.out.println("Mine here");
								correctFlags++;
								System.out.println(correctFlags);
							}
						}else
						{
							//togglers[i][j].setSelected(false);
							togglers[i][j].setIcon(null);
						}
					}
				}
			}
		}
		revalidate();
	}

	public void expand(int r, int c)
	{

		if(togglers[r][c].getIcon()==null)
		{
			togglers[r][c].setSelected(true);
			if(mineArray[r][c]>0)
			{
				//set the number
				int mineCount = mineArray[r][c];
				togglers[r][c].setIcon(num[mineCount]);

			}
			else
				{
					for(int x=r-1;x<=r+1;x++)
						for(int y=c-1;y<=c+1;y++)
						{
							try{
								if(!togglers[x][y].isSelected())
									expand(x,y);
							}catch(ArrayIndexOutOfBoundsException e){

							}
						}

				}

		}


	}



	public void mouseReleased(MouseEvent e){

	}
	public void mouseEntered(MouseEvent e){

	}
	public void mouseExited(MouseEvent e){

	}

	public static void main(String[]args){
		Minesweeper app = new Minesweeper();
	}

	/*public class UpdateTimer extends TimerTask{
		public void run(){
			if(!gameEnd){
				timePassed++;
				time.setText("   "+timePassed);
			}

		}
	}*/

}