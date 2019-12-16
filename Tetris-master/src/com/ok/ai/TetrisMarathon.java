package com.ok.ai;
/*

This program was written by Jacob Jackson. You may modify,
copy, or redistribute it in any way you wish, but you must
provide credit to me if you use it in your own program.

*/
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class TetrisMarathon extends Tetris
{
	public int score = 0;
	public  static int finalScore;
	
	public static final int[] VALUES = {0, 100, 175, 350, 700, 1000};
	
	TetrisMarathon()
	{}
	TetrisMarathon(PieceGenerator gen)
	{
		super(gen);
	}
	
	public void onLinesCleared(int cleared)
	{
		score += VALUES[cleared] * (combo + 1);
		if (cleared >= 3)
			combo++;
		if(score>=550) setLevel(2);
		if(score>=1400) setLevel(3);
		if(score>=2850) setLevel(4);
		if(score>=5200) setLevel(5);
		if(score>=8750) setLevel(6);
		if(score>=13800) setLevel(7);
		if(score>=20650) setLevel(8);
		if(score>=29600) setLevel(9);
		if(score>=40950) setLevel(10);
		if(score>=55000) setLevel(11);
	}
	public void onTSpin(int cleared, int x, int y, int rotation)
	{
		onLinesCleared(cleared + 2);
		tSpinEffect(x, y, rotation);
	}
	
	public void drawTo(Graphics2D g, int x, int y)
	{
		x += DSP_W;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setColor(Color.WHITE);
		g.setFont(F_LINES);
		g.drawString("" + score, x + (int)(SQR_W*1.5), y + (int)(SQR_W/2));
		
		g.setFont(F_TIME);
		g.drawString("Level: "+getLevel(), x+DSP_W*2, y+SQR_W);
		g.drawString("" + linesCleared + " lines", x+DSP_W*2, y+ DSP_W/2);
		g.drawString(getTimeString(), x + (int)(SQR_W*1.5), y + DSP_W/2);
		if (combo > 0)
			g.drawString("combo x" + (combo + 1), x+(int)(DSP_W*1.8), y + DSP_W/10);

		
		super.drawTo(g, x, y);
		
		if(isOver()==true) { 
			finalScore = score;
		};
				
		
	}
	

	public int evaluate()
	{
		return super.evaluate() + score / 100;
	}

	public Tetris[] children()
	{
		int len = (W+3) * piece.length + 1;
		Tetris[] ans = new Tetris[len];
		int pos = 0;

		for (int i = -3; i < W; i++)
		{
			for (int r = 0; r < piece.length; r++)
			{
				TetrisMarathon t = new TetrisMarathon();
				t.piece = piece;
				t.ty = -4;
				t.tx = i;
				t.rotation = r;
				t.pieceID = pieceID;
				t.combo = combo;

				if (t.pieceLegal() != LEGAL)
				{
					pos++;
					continue;
				}

				copy(t.board, board);
				copy(t.fMoves, fMoves);
				t.drop();
				ans[pos++] = t;
			}
		}
		if (!(hasStored))
		{
			TetrisMarathon t = new TetrisMarathon();
			t.piece = piece;
			t.pieceID = pieceID;
			t.combo = combo;
			copy(t.board, board);
			copy(t.fMoves, fMoves);
			t.store();
			ans[pos++] = t;
		}
		
		return ans;
	}
	
	public TetrisMarathon newGame()
	{
		gen.newGame();
		TetrisMarathon t = new TetrisMarathon(gen);
		t.tickInterval = tickInterval;
		t.ticksPerSecond = ticksPerSecond;
		gen = null;
		return t;
	}
	
}
