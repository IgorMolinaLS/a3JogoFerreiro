package cena;

import com.jogamp.opengl.GL2;
import textura.Textura;

public class Anvil {
    private final float limite = 1.0f;
    private final int filtro;
    private final int wrap;
    private final int modo;
    
    private Textura textura = null;
    private final int totalTextura = 6;
    
    public static final String FACETEXT = "src/imagens/anvil_1.PNG";

    public Anvil(int filtro, int wrap, int modo) {
        this.filtro = filtro;
        this.wrap = wrap;
        this.modo = modo;
        this.textura = new Textura(totalTextura); // Assumindo que só tem uma textura para o fundo
    }
    
    public void generateAnvil(GL2 gl) {
        gl.glPushMatrix();
        
        //não é geração de textura automática
        textura.setAutomatica(false);
        
        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);  

        textura.gerarTextura(gl, FACETEXT, 0);
        
        // Face FRONTAL
        gl.glBegin (GL2.GL_QUADS );
            //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, limite);      gl.glVertex3f(10.0f, 10.0f,  10.0f);
        gl.glTexCoord2f(limite, limite);    gl.glVertex3f( -10.0f, 10.0f,  10.0f);
        gl.glTexCoord2f(limite, 0.0f);      gl.glVertex3f( -10.0f,  -10.0f,  10.0f);
        gl.glTexCoord2f(0.0f, 0.0f);        gl.glVertex3f(10.0f,  -10.0f,  10.0f);
        gl.glEnd();
        
        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 0);

        textura.gerarTextura(gl, FACETEXT, 1);
        
        // Face POSTERIOR
        gl.glBegin(GL2.GL_QUADS);
            //coordenadas da Textura            //coordenadas do quads
            gl.glTexCoord2f(limite, 0.0f);      gl.glVertex3f(-10.0f, -10.0f, -10.0f);
            gl.glTexCoord2f(limite, limite);    gl.glVertex3f(-10.0f,  10.0f, -10.0f);
            gl.glTexCoord2f(0.0f, limite);      gl.glVertex3f( 10.0f,  10.0f, -10.0f);
            gl.glTexCoord2f(0.0f, 0.0f);        gl.glVertex3f( 10.0f, -10.0f, -10.0f);
        gl.glEnd();

        textura.desabilitarTextura(gl, 1);

        textura.gerarTextura(gl, FACETEXT, 2);
        
        // Face SUPERIOR
        gl.glBegin(GL2.GL_QUADS);
            //coordenadas da Textura            //coordenadas do quads
            gl.glTexCoord2f(0.0f, limite);      gl.glVertex3f(-10.0f,  10.0f, -10.0f);
            gl.glTexCoord2f(0.0f, 0.0f);        gl.glVertex3f(-10.0f,  10.0f,  10.0f);
            gl.glTexCoord2f(limite, 0.0f);      gl.glVertex3f( 10.0f,  10.0f,  10.0f);
            gl.glTexCoord2f(limite, limite);    gl.glVertex3f( 10.0f,  10.0f, -10.0f);
       gl.glEnd();

        textura.desabilitarTextura(gl, 2);

        textura.gerarTextura(gl, FACETEXT, 3);
        
       // Face INFERIOR
       gl.glBegin(GL2.GL_QUADS);
            //coordenadas da Textura            //coordenadas do quads
            gl.glTexCoord2f(limite, limite);    gl.glVertex3f(-10.0f, -10.0f, -10.0f);
            gl.glTexCoord2f(0.0f, limite);      gl.glVertex3f( 10.0f, -10.0f, -10.0f);
            gl.glTexCoord2f(0.0f, 0.0f);        gl.glVertex3f( 10.0f, -10.0f,  10.0f);
            gl.glTexCoord2f(limite, 0.0f);      gl.glVertex3f(-10.0f, -10.0f,  10.0f);
       gl.glEnd();

        textura.desabilitarTextura(gl, 3);

        textura.gerarTextura(gl, FACETEXT, 4);
        
       // Face LATERAL DIREITA
       gl.glBegin(GL2.GL_QUADS);
            //coordenadas da Textura            //coordenadas do quads
            gl.glTexCoord2f(limite, 0.0f);      gl.glVertex3f( 10.0f, -10.0f, -10.0f);
            gl.glTexCoord2f(limite, limite);    gl.glVertex3f( 10.0f,  10.0f, -10.0f);
            gl.glTexCoord2f(0.0f, limite);      gl.glVertex3f( 10.0f,  10.0f,  10.0f);
            gl.glTexCoord2f(0.0f, 0.0f);        gl.glVertex3f( 10.0f, -10.0f,  10.0f);
       gl.glEnd();

        textura.desabilitarTextura(gl, 4);

        textura.gerarTextura(gl, FACETEXT, 5);
       // Face LATERAL ESQUERDA
       gl.glBegin(GL2.GL_QUADS);
            //coordenadas da Textura            //coordenadas do quads
            gl.glTexCoord2f(0.0f, 0.0f);        gl.glVertex3f(-10.0f, -10.0f, -10.0f);
            gl.glTexCoord2f(limite, 0.0f);      gl.glVertex3f(-10.0f, -10.0f,  10.0f);
            gl.glTexCoord2f(limite, limite);    gl.glVertex3f(-10.0f,  10.0f,  10.0f);
            gl.glTexCoord2f(0.0f, limite);      gl.glVertex3f(-10.0f,  10.0f, -10.0f);
        gl.glEnd();

        textura.desabilitarTextura(gl, 5);

        gl.glPopMatrix();
        gl.glFlush();
    }
}
