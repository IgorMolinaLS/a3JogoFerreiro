package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Disc {
    GLUT glut = new GLUT();
    
    public void generateDisk(GL2 gl) {
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        
        gl.glPushMatrix();
        gl.glRotatef(-45, 1.0f, 0.0f, 0.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        glut.glutSolidCylinder(5.0f, 4, 15, 15);
        gl.glPopMatrix();
    }
}
