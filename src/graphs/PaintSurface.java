/*
 *  -------------------------
 *  Author: AnhNHT - SE61750
 *  -------------------------
 */
package graphs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JComponent;

public class PaintSurface extends JComponent {

    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();
    private ArrayList<Integer> fill = new ArrayList<>();

    boolean filled = false;
    Color color = Color.BLACK;
    private Point startDrag, endDrag;
    private int type;

    public PaintSurface() {
    }

    public PaintSurface(final int type) {
        this.type = type;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startDrag = new Point(e.getX(), e.getY());
                endDrag = startDrag;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (type == 0) {
                    Shape r = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
                    shapes.add(r);
                    if (filled) {
                        addAttribute(color, 1);
                    } else {
                        addAttribute(color, 0);
                    }
                } else if (type == 1) {
                    Shape r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
                    shapes.add(r);
                    if (filled) {
                        addAttribute(color, 1);
                    } else {
                        addAttribute(color, 0);
                    }
                } else {
                    Shape r = makeEllipse(startDrag.x, startDrag.y, e.getX(), e.getY());
                    shapes.add(r);
                    if (filled) {
                        addAttribute(color, 1);
                    } else {
                        addAttribute(color, 0);
                    }
                }
                startDrag = null;
                endDrag = null;
                repaint();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endDrag = new Point(e.getX(), e.getY());
                repaint();
            }
        });
    }

    public void setColors(Color color) {
        this.color = color;

    }

    public void setFill(boolean filled) {
        this.filled = filled;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public void clearAll(){
        this.shapes = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.fill = new ArrayList<>();
        repaint();
    }

    public void addAttribute(Color color, int fill) {
        this.colors.add(color);
        this.fill.add(fill);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int colorIndex = 0;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//            g2.setStroke(new BasicStroke(2));
        /**
         * setComposite: specifies how new pixels are to be combined with the
         * existing pixels on the graphics device during the rendering process
         * AlphaComposite: achieve blending and transparency effects with
         * graphics and images getInstance: Creates an AlphaComposite object
         * with the specified rule and the constant alpha to multiply with the
         * alpha of the source. The source is multiplied with the specified
         * alpha before being composited with the destination.
         */
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        for (int i = 0; i < shapes.size(); i++) {
            if (colors.isEmpty()) {
                g2.setPaint(color);
            } else {
                g2.setPaint(colors.get(i));
            }
            g2.draw(shapes.get(i));
            if (fill.get(i) == 1 && (type == 1 || type == 2)) {
                g2.fill(shapes.get(i));
            }
        }

        if (startDrag != null && endDrag != null) {
            g2.setPaint(color);
            if (type == 0) {
                Shape r = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                g2.draw(r);
            } else if (type == 1) {
                Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                g2.draw(r);
                if (filled == true) {
                    g2.fill(r);
                }
            } else {
                Shape r = makeEllipse(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                g2.draw(r);
                if (filled == true) {
                    g2.fill(r);
                }
            }
        }
    }

    private Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
        return new Line2D.Float(x1, y1, x2, y2);
    }

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    private Ellipse2D.Float makeEllipse(int x1, int y1, int x2, int y2) {
        return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}
