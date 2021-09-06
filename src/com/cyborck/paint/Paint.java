package com.cyborck.paint;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
}
