package com.cyborck.paint;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Paint extends JFrame implements KeyListener {
    private final DrawPanel drawPanel;

    public Paint () {
        setTitle( "Paint" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        drawPanel = new DrawPanel( 800, 800 );
        add( drawPanel );
        addKeyListener( this );
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    }

    public static void main ( String[] args ) {
        System.setProperty( "sun.java2d.uiScale", "1.0" );
        new Paint();
    }

    @Override
    public void keyPressed ( KeyEvent e ) {
        if ( ( e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0 ) {
            //if control is pressed
            switch ( e.getKeyCode() ) {
                case KeyEvent.VK_C -> drawPanel.clear();
                case KeyEvent.VK_Z -> drawPanel.undo();
                case KeyEvent.VK_Y -> drawPanel.redo();
            }
        } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
            System.exit( 0 );
    }

    //not used
    @Override
    public void keyTyped ( KeyEvent e ) {}

    @Override
    public void keyReleased ( KeyEvent e ) {}
}
