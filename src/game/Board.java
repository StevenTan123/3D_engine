package game;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	
	final int DELAY = 10;
	Timer timer;
	
	// Constructor
	public Board() {
		addKeyListener(new TAdapter());
		setBackground(Color.WHITE);
		setFocusable(true);
		
		timer = new Timer(DELAY, this);
        timer.start();
	}
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        drawMesh(g2d, Main.mesh, true);
    }
    
    // Essentially the game loop, called every DELAY milleseconds
    @Override
    public void actionPerformed(ActionEvent e) {  
        Main.cam.move();
        repaint();
    }
    
    // Draws the mesh on the screen
    void drawMesh(Graphics2D g, Mesh mesh, boolean fill) {
    	
    	// Sort triangles in mesh in order of shortest to longest distance from camera
        Collections.sort(mesh.tris, new Comparator<Triangle>() {
                @Override
                public int compare(Triangle a, Triangle b) {
                    float depth1 = Main.cam.cameraRot(a.centroid).z;
                    float depth2 = Main.cam.cameraRot(b.centroid).z;
                    if (depth1 > depth2) {
                        return -1;
                    } else if(depth2 > depth1) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        
        // Draw each triangle individually
    	for (Triangle tri : mesh.tris) {
    		drawTriangle(g, tri, fill);
    	}
    }
    
    // Draws triangle on screen
    void drawTriangle(Graphics2D g, Triangle tri, boolean fill) {
    	g.setColor(tri.c);
    	
    	// Project each vertex of the triangle onto the 2D screen
        Point2D[] proj = new Point2D[3];
        for (int i = 0; i < 3; i++) {
            proj[i] = Main.cam.projectPoint(tri.pts[i]);
            
            // Don't draw this triangle if it's behind the camera
            if(proj[i] == null) {
            	return;
            }
        }
        
        // Draw projected triangle on screen
        if(fill) {
            Path2D path = new Path2D.Float();
            path.moveTo(proj[0].x, proj[0].y);
            path.lineTo(proj[1].x, proj[1].y);
            path.lineTo(proj[2].x, proj[2].y);
            path.closePath();
            
            g.fill(path);
        } else {
            drawLine(g, proj[0], proj[1]);
            drawLine(g, proj[1], proj[2]);
            drawLine(g, proj[2], proj[0]);	
        }
    }
    
    // Draws a line on the screen
    void drawLine(Graphics2D g, Point2D a, Point2D b) {
    	if(a != null && b != null) {
        	g.draw(new Line2D.Float(a.x, a.y, b.x, b.y));	
    	}
    }
    
    // Key detection for movements
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_SPACE) {
            	if (Math.abs(Main.cam.vely) < Main.epsilon) {
            		Main.cam.vely = Main.cam.JUMP_SPEED;
            	}
            }
            
            if (key == KeyEvent.VK_W) {
                Main.cam.velf = Main.cam.MOVE_SPEED;
            }

            if (key == KeyEvent.VK_S) {
            	Main.cam.velf = -Main.cam.MOVE_SPEED;
            }
            
            if (key == KeyEvent.VK_A) {
                Main.cam.vels = -Main.cam.MOVE_SPEED;
            }

            if (key == KeyEvent.VK_D) {
            	Main.cam.vels = Main.cam.MOVE_SPEED;
            }
            
            if (key == KeyEvent.VK_LEFT) {
                Main.cam.dah = -Main.cam.Y_ROT_SPEED;
            }

            if (key == KeyEvent.VK_RIGHT) {
            	Main.cam.dah = Main.cam.Y_ROT_SPEED;
            }

            if (key == KeyEvent.VK_UP) {
            	Main.cam.dav = -Main.cam.X_ROT_SPEED;
            }

            if (key == KeyEvent.VK_DOWN) {
            	Main.cam.dav = Main.cam.X_ROT_SPEED;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_W) {
                Main.cam.velf = 0;
            }

            if (key == KeyEvent.VK_S) {
            	Main.cam.velf = 0;
            }
            
            if (key == KeyEvent.VK_A) {
                Main.cam.vels = 0;
            }

            if (key == KeyEvent.VK_D) {
            	Main.cam.vels = 0;
            }
            
            if (key == KeyEvent.VK_LEFT) {
                Main.cam.dah = 0;
            }

            if (key == KeyEvent.VK_RIGHT) {
            	Main.cam.dah = 0;
            }

            if (key == KeyEvent.VK_UP) {
            	Main.cam.dav = 0;
            }

            if (key == KeyEvent.VK_DOWN) {
            	Main.cam.dav = 0;
            }
        }

    }
}
