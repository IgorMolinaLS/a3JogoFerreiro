package cena;

import com.jogamp.opengl.GL2;
import textura.Textura;

public class InstructionsPage {
    private float limite = 1.0f;
    private Textura textura;
    private int filtro;
    private int wrap;
    private int modo;
    private static final String TEXTURE_PATH = "src/imagens/paper.jpg"; // Caminho da textura

    public InstructionsPage(int filtro, int wrap, int modo) {
        this.filtro = filtro;
        this.wrap = wrap;
        this.modo = modo;
        this.textura = new Textura(1); // Assumindo que só tem uma textura para o fundo
    }
    
    public void generateIP(GL2 gl) {
        // Aplica a textura do fundo
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);

        textura.gerarTextura(gl, TEXTURE_PATH, 0);

        // Desenha o fundo usando um quadrado
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, limite); gl.glVertex3f(-45.0f, 45.0f, 45.0f);
        gl.glTexCoord2f(limite, limite); gl.glVertex3f(45.0f, 45.0f, 45.0f);
        gl.glTexCoord2f(limite, 0.0f); gl.glVertex3f(45.0f, -45.0f, 45.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-45.0f, -45.0f, 45.0f);
        gl.glEnd();

        textura.desabilitarTextura(gl, 0);
    }
}
