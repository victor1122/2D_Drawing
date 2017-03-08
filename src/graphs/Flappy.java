/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graphs;

/**
 *
 * @author anhnh
 */
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


class Topka extends JPanel implements MouseListener {
	int numBars = 200;
	Rectangle[] bars = new Rectangle[numBars];
	Topka()
	{
		 x = 150;
		 y = 50;
		 topka = new Rectangle(x, y, radius, radius);
		 kreirajScena();
		 this.addMouseListener(this);
		 vreme = 0;
	}
    void dodajVreme(int v)
    {
        vreme += v;
    }

    void kreirajScena()
    {
    	Random rand = new Random();
    	for (int i = 0; i < numBars; i++)
    	{
    		int rint = rand.nextInt(20);
    		int rint2 = rand.nextInt(150);
    		
    		Rectangle r = new Rectangle(scenaPos.x + i*500, scenaPos.y, 100 + rint, 100 + rint2);
    		bars[i] = r;
    	}
    }
    int minGap = 150;
    boolean nacrtajScena(Graphics g)
    {
    	Random rand = new Random();
    	scenaPos.x -= 1;
    	Color col = g.getColor();
    	
    	boolean kolizija = false;
    	
    	for (int i = 0; i < numBars; i++)
    	{
    		Rectangle bar = bars[i];
    		bar.x -= 1;
    		Rectangle low = new Rectangle(bar);
    		low.height += (h - low.y);
    		low.y = bar.height + minGap;
    		if (bar.intersects(topka) || low.intersects(topka))
    			kolizija = true;
    		else
    		{
    			if (bar.x + bar.width < topka.x)
    				poeni = i+1;
    		}
    		g.setColor(orange);
    		g.fillRect(bar.x, bar.y, bar.width, bar.height);
    		g.fillRect(low.x, low.y, low.width, low.height);
    		g.setColor(Color.black);
    		g.drawRect(bar.x, bar.y, bar.width, bar.height);
    		g.drawRect(low.x, low.y, low.width, low.height);
    	}
    	g.setColor(col);
    	return kolizija;
    }
    
    void updateForce()
    {
    	if (force.x > 0)
    	{
    		force.x -= 1;
    	}
    	else if (force.x < 0)
    	{
    		force.x += 1;
    	}
    	if (force.y > 0)
    	{
    		force.y -= 1;
    	}
    	else if (force.y < 0)
    	{
    		force.y += 1;
    	}
    }
    void update()
    {
    	int maxY = h-topka.height;

    	updateForce();
    	//System.out.println("Force: " + force);
    	topka.x += force.x;
    	topka.y += (delta*gravity+force.y/2);
    	if (topka.y >= maxY)
    	{
    		topka.y = maxY;
    		gameOver = true;
    		return;
    	}
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	Graphics2D g2d = (Graphics2D)g;
    	Color topkaBoja = new Color(0, 223, 161);
    	w = this.getWidth(); h = this.getHeight();
    	cw = w/2; ch = h/2;
    	int bgHeight = 50;
    	h -= bgHeight;
    	update();
    	g.setColor(black);
        g.fillRect(0, 0, w, h);
        g.setColor(orange);
        g.fillRect(0, h, w, bgHeight);
        g2d.setStroke(new BasicStroke(4));
        g.setColor(Color.black);
        g.drawLine(0, h, w, h);
        g.setColor(orange);
        gameStarted = vreme > 3000;
        if (!gameStarted)
        {
        	g.setFont(font);
        	String poraka = Integer.toString(4 - vreme / 1000);
        	int w = g2d.getFontMetrics(font).stringWidth(poraka);
        	g.drawString(poraka, cw-w/2, ch);
        	return ;
        }
        
        if (gameOver)
        {
        	g.setFont(font);
        	String poraka = "GAME OVER";
        	int w = g2d.getFontMetrics(font).stringWidth(poraka);
        	g.drawString(poraka, cw-w/2, ch);
        	return;
        }
        
    	boolean kolizija = nacrtajScena(g);
        if (kolizija)
        	gameOver = true;
    	g.setColor(topkaBoja);

        g.fillOval(topka.x, topka.y, topka.width, topka.height);
        g.setColor(Color.black);
        g.drawOval(topka.x, topka.y, topka.width, topka.height);
        g.setFont(malFont);
        g.setColor(Color.white);
        g.drawString("Points: " + poeni, 5, 20);
    }
    Font font = new Font("TimesRoman", Font.PLAIN, 40);
    Font malFont = new Font("TimesRoman", Font.BOLD, 15);
    
    Point force = new Point(0, -20);
    Point scenaPos = new Point(500, 0);
    
    boolean gameOver = false;
    boolean gameStarted = false;
    int radius = 30;
    int gravity = 9;
    Color orange = new Color(255, 87, 41), black = new Color(60, 60, 60);
    int w = this.getWidth(), h = this.getHeight();
    int cw = w/2, ch = h/2;
    int delta = 1;
    int x, y;
    Rectangle topka;
    int poeni = 0;
    int vreme = 0;
	@Override
	public void mouseClicked(MouseEvent arg0) {

	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		force.y += -20;
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

/**
 *
 * @author root
 */

public class Flappy extends JApplet implements Runnable {
   
    Topka krug;
   
    @Override
    public void init()
    {
        setSize(500, 500);
        krug = new Topka();
        this.getContentPane().add(krug);
        Thread anim = new Thread(this);
        anim.start();
    }
    void sleep(int n)
    {
        try {
            Thread.sleep(n);
        } catch (InterruptedException ex) {
            Logger.getLogger(Flappy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
    	repaint();
    	for (int i = 0; i < 3; i++)
    	{
    		
    		krug.dodajVreme(1000);
    		repaint();
    		sleep(1000);
            repaint();
    	}
    	krug.dodajVreme(1000);
        while (true)
        {
            this.repaint();
            sleep(15);
        }
       
    }
    int vreme = 0;
}