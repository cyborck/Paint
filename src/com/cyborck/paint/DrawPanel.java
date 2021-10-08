package com.cyborck.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DrawPanel extends JPanel {
    private final int radius;
    private final Color color;

    private final Stack<BufferedImage> undoStack;
    private final Stack<BufferedImage> redoStack;

    private BufferedImage currentImage;
    private Graphics2D graphics;

    public DrawPanel ( int width, int height ) {
        setLayout( null );
        setPreferredSize( new Dimension( width, height ) );
        DrawListener d = new DrawListener();
        addMouseListener( d );
        addMouseMotionListener( d );

        radius = 5;
        color = new Color( 231, 114, 31 );

        updateCurrentImage( new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB ) );

        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    private void updateCurrentImage ( BufferedImage newImage ) {
        currentImage = newImage;
        graphics = currentImage.createGraphics();
        graphics.setColor( color );
        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    }

    public void clear () {
        graphics.clearRect( 0, 0, getPreferredSize().width, getPreferredSize().height );
    }

    public void undo () {
        if ( undoStack.isEmpty() )
            return;

        redoStack.add( currentImage );
        updateCurrentImage( undoStack.pop() );
    }

    public void redo () {
        if ( redoStack.isEmpty() )
            return;

        undoStack.add( currentImage );
        updateCurrentImage( redoStack.pop() );
    }

    private void beforeEditing () {
        //called when the user has drawn or cleared, but before the action has taken place
        if ( undoStack.size() >= 50 ) //max size 50
            undoStack.remove( 0 );

        undoStack.add( copyImage( currentImage ) );
        redoStack.clear();
    }

    private BufferedImage copyImage ( BufferedImage image ) {
        BufferedImage copiedImage = new BufferedImage( image.getWidth(), image.getHeight(), image.getType() );
        Graphics copyGraphics = copiedImage.createGraphics();
        copyGraphics.drawImage( image, 0, 0, null );
        return copiedImage;
    }

    @Override
    protected void paintComponent ( Graphics g ) {
        g.drawImage( currentImage, 0, 0, null );
        repaint();
    }

    private void draw ( int x, int y ) {
        graphics.fillOval( x - radius, y - radius, radius * 2, radius * 2 );
    }

    private class DrawListener extends MouseAdapter {
        private Point mousePos = new Point();
        private boolean pressed = false;

        @Override
        public void mouseReleased ( MouseEvent e ) {
            pressed = false;
        }

        @Override
        public void mousePressed ( MouseEvent e ) {
            if ( !pressed && !mousePos.equals( e.getPoint() ) ) {
                beforeEditing();
                draw( e.getX(), e.getY() );
            }
        }

        @Override
        public void mouseDragged ( MouseEvent e ) {
            if ( !pressed )
                beforeEditing();

            pressed = true;

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