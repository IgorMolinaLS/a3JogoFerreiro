package cena;

import com.jogamp.opengl.GL2;
import textura.Textura;

public class Chest {

    private final float limite = 1.0f;
    private final int filtro;
    private final int wrap;
    private final int modo;

    private Textura textura = null;
    private final int totalTextura = 6;

    public static final String FACETEXT = "src/imagens/chest.PNG";

    public Chest(int filtro, int wrap, int modo) {
        this.filtro = filtro;
        this.wrap = wrap;
        this.modo = modo;
        this.textura = new Textura(totalTextura); // Inicializa a instância de Textura
    }

    // Método genérico para desenhar uma face com textura
    private void drawFace(GL2 gl, float[] vertices, float[] texCoords) {
        // Configura a textura
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);

        // Desenha a face com a textura
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0; i < 4; i++) {
            gl.glTexCoord2f(texCoords[i * 2], texCoords[i * 2 + 1]);
            gl.glVertex3f(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
        }
        gl.glEnd();
    }

    public void generateChest(GL2 gl) {
        gl.glPushMatrix();

        // Carregar a textura uma vez, fora do loop de faces
        textura.setAutomatica(false);
        textura.gerarTextura(gl, FACETEXT, 0);

        // Define as coordenadas e as coordenadas de textura para as faces
        float[][] facesVertices = {
            // Face frontal
            {10.0f, 10.0f, 10.0f, -10.0f, 10.0f, 10.0f, -10.0f, -10.0f, 10.0f, 10.0f, -10.0f, 10.0f},
            // Face posterior
            {-10.0f, -10.0f, -10.0f, -10.0f, 10.0f, -10.0f, 10.0f, 10.0f, -10.0f, 10.0f, -10.0f, -10.0f},
            // Face superior
            {-10.0f, 10.0f, -10.0f, -10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, -10.0f},
            // Face inferior
            {-10.0f, -10.0f, -10.0f, 10.0f, -10.0f, -10.0f, 10.0f, -10.0f, 10.0f, -10.0f, -10.0f, 10.0f},
            // Face lateral direita
            {10.0f, -10.0f, -10.0f, 10.0f, 10.0f, -10.0f, 10.0f, 10.0f, 10.0f, 10.0f, -10.0f, 10.0f},
            // Face lateral esquerda
            {-10.0f, -10.0f, -10.0f, -10.0f, -10.0f, 10.0f, -10.0f, 10.0f, 10.0f, -10.0f, 10.0f, -10.0f}
        };

        float[][] facesTexCoords = {
            {0.0f, limite, limite, limite, limite, 0.0f, 0.0f, 0.0f}, // Frontal
            {limite, 0.0f, limite, limite, 0.0f, limite, 0.0f, 0.0f}, // Posterior
            {0.0f, limite, 0.0f, 0.0f, limite, 0.0f, limite, limite}, // Superior
            {limite, limite, 0.0f, limite, 0.0f, 0.0f, limite, 0.0f}, // Inferior
            {limite, 0.0f, limite, limite, 0.0f, limite, 0.0f, 0.0f}, // Lateral Direita
            {0.0f, 0.0f, limite, 0.0f, limite, limite, 0.0f, limite}  // Lateral Esquerda
        };

        // Desenha as 6 faces do cubo
        for (int i = 0; i < 6; i++) {
            drawFace(gl, facesVertices[i], facesTexCoords[i]);
        }

        // Desabilita a textura após o desenho de todas as faces
        textura.desabilitarTextura(gl, 0);

        gl.glPopMatrix();
        gl.glFlush();
    }
}
