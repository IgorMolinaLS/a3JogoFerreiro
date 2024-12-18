package cena;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import input.KeyBoard;

public class Renderer {
    private static GLWindow window = null;
    public static int screenWidth = 600;  //1280  - 640
    public static int screenHeight = 600; //960  - 480

    //Cria a janela de rendeziracao do JOGL
    public static void init(){        
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);        
        window = GLWindow.create(caps);
        window.setSize(screenWidth, screenHeight);
        window.setResizable(false);
        
        //Impede que o tamanho da janela mude
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowResized(WindowEvent e) {
                int currentWidth = window.getWidth();
                int currentHeight = window.getHeight();
                
                if (currentWidth != screenWidth || currentHeight != screenHeight) {
                    window.setSize(screenWidth, screenHeight); // Reset to original size
                }
            }
        });
        
        Cena cena = new Cena();
        
        window.addGLEventListener(cena); //adiciona a Cena a Janela  
        //Habilita o teclado : cena
        window.addKeyListener(new KeyBoard(cena));
        
        //window.requestFocus();
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); //inicia o loop de animacao
        
        //encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });       
        window.setVisible(true);
    }
  
    public static void main(String[] args) {
        init();
    }
}
