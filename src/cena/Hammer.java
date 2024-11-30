package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;


public class Hammer {
    GLUT glut = new GLUT();
    
    public void generateHammer(GL2 gl){
        //Instancia o cabo
        gl.glPushMatrix();
        gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
        gl.glColor3f(0.54f, 0.27f, 0.07f); // Cor marrom
        glut.glutSolidCylinder(1.0f, 15.0f, 15, 15);
        gl.glPopMatrix();

        //Instancia a cabeça
        gl.glPushMatrix();
        gl.glTranslatef(5.0f, 15.0f, 0.0f); // Posiciona a cabeça
        gl.glRotatef(-90, 0.0f, 50.0f, 0.0f);
        gl.glColor3f(0.5f, 0.5f, 0.5f); // Cor cinza
        glut.glutSolidCylinder(3.0f, 10.0f, 15, 15);
        gl.glPopMatrix();
    }
}
