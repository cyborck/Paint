package com.cyborck.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Paint extends JFrame implements KeyListener {
    private final DrawPanel drawPanel;

    public Paint () {
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        drawPanel = new DrawPanel( 1000, 1000 );
        add( drawPanel );
        addKeyListener( this );
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    }

    public static void main ( String[] args ) {
        new Paint();
    }

    @Override
    public void keyPressed ( KeyEvent e ) {
        if ( e.getKeyChar() == 'c' )
            drawPanel.clear();
        else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
            System.exit( 0 );
    }

    //not used
    @Override
    public void keyTyped ( KeyEvent e ) {}

    @Override
    public void keyReleased ( KeyEvent e ) {}

    private static class DrawPanel extends JPanel {
        private final BufferedImage image;
        private final Graphics2D g2d;
        private final int radius;

        public DrawPanel ( int width, int height ) {
            setLayout( null );
            setPreferredSize( new Dimension( width, height ) );
            DrawListener d = new DrawListener();
            addMouseListener( d );
            addMouseMotionListener( d );

            image = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            g2d = image.createGraphics();
            g2d.setColor( new Color( 231, 114, 31 ) );
            g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            radius = 5;
        }

        public void clear () {
            g2d.clearRect( 0, 0, getPreferredSize().width, getPreferredSize().height );
        }

        @Override
        protected void paintComponent ( Graphics g ) {
            g.drawImage( image, 0, 0, null );
            repaint();
        }

        private void draw ( int x, int y ) {
            g2d.fillOval( x - radius, y - radius, radius * 2, radius * 2 );
        }

        private class DrawListener extends MouseAdapter {
            private Point mousePos = new Point();

            @Override
            public void mousePressed ( MouseEvent e ) {
                draw( e.getX(), e.getY() );
            }

            @Override
            public void mouseDragged ( MouseEvent e ) {
                int startX = mousePos.x;
                int startY = mousePos.y;
                int currX = e.getX();
                int currY = e.getY();
                float gradient = ( float ) ( currY - startY ) / ( currX - startX );

                java.util.List<Integer> xList = fillCoordinates( startX, currX );
                java.util.List<Integer> yList = fillCoordinates( startY, currY );

                for ( int x: xList ) {
                    int y = Math.round( startY + ( x - startX ) * gradient );
                    draw( x, y );
                    yList.remove( ( Integer ) y );
                }
                for ( int y: yList ) {
                    int x = Math.round( startX + ( y - startY ) / gradient );
                    draw( x, y );
                }

                mousePos = e.getPoint();
            }

            @Override
            public void mouseMoved ( MouseEvent e ) {
                mousePos = e.getPoint();
            }

            private java.util.List<Integer> fillCoordinates ( int a, int b ) {
                List<Integer> list = new ArrayList<>();

                if ( a < b )
                    for ( int i = a; i <= b; i++ )
                        list.add( i );
                else if ( a > b )
                    for ( int i = a; i >= b; i-- )
                        list.add( i );

                return list;
            }
        }
    }
}
