package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import java.awt.Font;

public class Cena implements GLEventListener{    
    private float xMin, xMax, yMin, yMax, zMin, zMax;   
    
    public int oreCount, barCount, diskCount, furnaceCount, anvilCount, moedas = 0;
    private Background background;
    private InstructionsPage dialogPage;
    public boolean isDialog1Open = true;
    public boolean isDialog2Open;
    
    private Anvil anvil;
    public float[] anvilPosition = {60f, 30f, 20f};
    private Hammer hammer;
    public boolean isAnvilActive = false;
    
    private Table table;
    public float[] tablePosition = {0, -50f, 20f};
    
    private Disc disc;
    public boolean disk1OnTable, disk2OnTable, disk3OnTable;
    private long disk1StartTime = -1;
    private long disk2StartTime = -1;
    private long disk3StartTime = -1;
    private static final long DISK_TIMEOUT = 5000;
    
    private Chest chest;
    public float[] chestPosition = {-80f, 10f, 20f};
    
    private Furnace furnace;
    public float[] furnacePosition = {10f, 50f, 20f};
    public boolean isFurnaceActive = false;
    
    private long AnvilStartTime = -1;
    private static final long ANVIL_TIMEOUT = 1000;
    
    private float angle = 0.0f;
    
    public int heroWidth, heroHeigth = 20;
    public float heroX, heroY = 0; 
    
    
    public int tonalizacao = GL2.GL_SMOOTH;                
    private TextRenderer textRenderer;
    
    private int filtro = GL2.GL_LINEAR; ////GL_NEAREST ou GL_LINEAR
    private int wrap = GL2.GL_REPEAT;  //GL.GL_REPEAT ou GL.GL_CLAMP
    private int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND
        
    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena        
        GL2 gl = drawable.getGL().getGL2();
        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -100;
        xMax = yMax = zMax = 100;  
        
        textRenderer = new TextRenderer(new Font("Comic Sans MS Negrito", Font.PLAIN, 17));
        
        background = new Background(filtro, wrap, modo);
        dialogPage = new InstructionsPage(filtro, wrap, modo);
        chest = new Chest(filtro, wrap, modo);
        furnace = new Furnace(filtro, wrap, modo);
        anvil = new Anvil(filtro, wrap, modo);
        hammer = new Hammer();
        table = new Table(filtro, wrap, modo);
        disc = new Disc();
        
        //Habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable drawable) {  
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();                
        //objeto para desenho 3D
        GLUT glut = new GLUT();

        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(1, 1, 1, 1);        
        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);       
        gl.glLoadIdentity(); //ler a matriz identidade

        //Ativa as luzes
        iluminacaoAmbiente(gl);
        ligaLuz(gl);

        //Checa se o objetivo foi concluido e parabeniza o jogador
        if(moedas >= 100){
            isDialog2Open = true;
        }
        if(isDialog1Open){
            desenhaInstrucoes(gl);
        }
        if(isDialog2Open){
            desenhaParabenizacoes(gl);
        }
        
        //Desenha o background da cena
        gl.glPushMatrix();
            background.generateBG(gl);
        gl.glPopMatrix();

        //Desenha o protagonista
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.5f, 0.0f); // laranja
        gl.glTranslatef(heroX, heroY, 0);
        glut.glutSolidSphere(10, 15, 15);
        gl.glPopMatrix();
        
        //Se estiver usando a fornalha, gera o martelo e a animação
        if(isAnvilActive){
            gl.glPushMatrix();
            updateRotation();
            gl.glTranslatef(anvilPosition[0] - 15, anvilPosition[1] + 5, 50);
            gl.glRotatef(angle, 0, 0, -60);
            hammer.generateHammer(gl);
            gl.glPopMatrix();
            
            // Marca o tempo de início do uso
            if (AnvilStartTime == -1) {
                AnvilStartTime = System.currentTimeMillis(); 
            } else {
                // Verifica se o tempo passou e cessa a animação/permite que o jogador se mova
                if (System.currentTimeMillis() - AnvilStartTime >= ANVIL_TIMEOUT) {
                    isAnvilActive = false;
                    AnvilStartTime = -1; // Reseta o tempo
                }
            } 
        }
        
        //Renderiza bigorna, baú e fornalha
        gl.glPushMatrix();
            gl.glTranslatef(anvilPosition[0], anvilPosition[1], 0);
            anvil.generateAnvil(gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
            gl.glTranslatef(chestPosition[0], chestPosition[1], 0);
            chest.generateChest(gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
            gl.glTranslatef(furnacePosition[0], furnacePosition[1], 0);
            furnace.generateFurnace(gl);
        gl.glPopMatrix();

        //Renderiza as 3 mesas da cena
        gl.glPushMatrix();
        gl.glTranslatef(tablePosition[0], tablePosition[1], 0);
        table.generateTable(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(tablePosition[0] + tablePosition[2], tablePosition[1], 0);
        table.generateTable(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(tablePosition[0] - tablePosition[2], tablePosition[1], 0);
        table.generateTable(gl);
        gl.glPopMatrix();

        //Logica dos discos
        if (disk1OnTable) {
            renderDisc(gl, tablePosition[0] - 20, tablePosition[1] -3.5f);
            // Marca o tempo de início do uso
            if (disk1StartTime == -1) {
                disk1StartTime = System.currentTimeMillis(); // Marca o tempo de início
            } else {
                //Checa se o tempo passou, se sim o disco é vendido e o dinheiro aumenta
                if (System.currentTimeMillis() - disk1StartTime >= DISK_TIMEOUT) {
                    disk1OnTable = false;
                    disk1StartTime = -1; // Reseta o tempo
                    moedas += 20;
                }
            }
        }
        if (disk2OnTable) {
            renderDisc(gl, tablePosition[0], tablePosition[1] -3.5f);

            // Marca o tempo de início do uso
            if (disk2StartTime == -1) {
                disk2StartTime = System.currentTimeMillis(); // Marca o tempo de início
            } else {
                //Checa se o tempo passou, se sim o disco é vendido e o dinheiro aumenta
                if (System.currentTimeMillis() - disk2StartTime >= DISK_TIMEOUT) {
                    disk2OnTable = false;
                    disk2StartTime = -1; // Reseta o tempo
                    moedas += 20;
                }
            }
        }
        if (disk3OnTable) {
            renderDisc(gl, tablePosition[0] + 20, tablePosition[1] -3.5f);

            // Marca o tempo de início do uso
            if (disk3StartTime == -1) {
                disk3StartTime = System.currentTimeMillis(); // Marca o tempo de início
            } else {
                //Checa se o tempo passou, se sim o disco é vendido e o dinheiro aumenta
                if (System.currentTimeMillis() - disk3StartTime >= DISK_TIMEOUT) {
                    disk3OnTable = false;
                    disk3StartTime = -1; // Reseta o tempo
                    moedas += 20;
                }
            }
        }
        
        // Desenha o fogo ao redor da fornalha
        if (isFurnaceActive) {
            drawFire(gl, furnacePosition[0], furnacePosition[1] -10);
        }

        //Lógica dos textos de menu
        desenhaTexto(gl, 10, 15, Color.WHITE, 
                     "ores: " + oreCount + 
                     "  bars: " + barCount + 
                     "  disks: " + diskCount +
                     "                  " +
                     "      moedas: " + moedas);
        desenhaTexto(gl, 325, 490, Color.DARK_GRAY, String.valueOf(furnaceCount));
        desenhaTexto(gl, 475, 430, Color.DARK_GRAY, String.valueOf(anvilCount));
       
        gl.glFlush();      
    }
    
    //Mostra as instruções na tela
    public void desenhaInstrucoes(GL2 gl){
        gl.glPushMatrix();
            dialogPage.generateIP(gl);
        gl.glPopMatrix();
        
        desenhaTexto(gl, 220, 410, Color.BLACK, "Olá! Seja bem vindo!");
        desenhaTexto(gl, 170, 380, Color.BLACK, "Pressione BACKSPACE para re-");
        desenhaTexto(gl, 170, 360, Color.BLACK, "tirar os itens das estruturas!");
        desenhaTexto(gl, 170, 340, Color.BLACK, "Pressione ENTER para inserir");
        desenhaTexto(gl, 170, 320, Color.BLACK, "os itens nas estruturas!");
        desenhaTexto(gl, 170, 300, Color.BLACK, "(e para fechar esse diálogo)");
        desenhaTexto(gl, 170, 270, Color.BLACK, "Pegue minérios no baú, forge");
        desenhaTexto(gl, 170, 250, Color.BLACK, "e coloque os discos na mesa");
        desenhaTexto(gl, 170, 230, Color.BLACK, "para vender!");
        desenhaTexto(gl, 170, 200, Color.BLACK, "Seu objetivo é acumular 100");
        desenhaTexto(gl, 170, 180, Color.BLACK, "unidades de moedas, boa sorte!");
    }
    
    //Parabeniza o jogador por concluir o objetivo
    public void desenhaParabenizacoes(GL2 gl){
        gl.glPushMatrix();
        dialogPage.generateIP(gl);
        gl.glPopMatrix();
        
        desenhaTexto(gl, 220, 410, Color.BLACK, "Parabéns! Você concluiu");
        desenhaTexto(gl, 170, 350, Color.BLACK, "Você concluiu o seu objetivo!");
        desenhaTexto(gl, 170, 330, Color.BLACK, "Pressione ESC para fechar");
        desenhaTexto(gl, 170, 310, Color.BLACK, "o jogo!");
        desenhaTexto(gl, 170, 250, Color.BLACK, "Obrigado por jogar!");
        desenhaTexto(gl, 170, 180, Color.BLACK, "                              :D");
    }
        
    public void desenhaTexto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase){         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);       
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();        
    }

    public void iluminacaoAmbiente(GL2 gl) {
        float luzAmbiente[] = {0.2f, 0.2f, 0.2f, 1.0f}; //cor
        float posicaoLuz[] = {-150f, 0.0f, 100.0f, 1.0f}; //pontual

        // define parametros de luz de n�mero 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }

    public void ligaLuz(GL2 gl) {
        // habilita a defini��o da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // habilita o uso da ilumina��o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de n�mero 0
        gl.glEnable(GL2.GL_LIGHT0);
  
        gl.glShadeModel(tonalizacao);
    }

    public void desligaluz(GL2 gl) {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
        //desliga a iluminacao
        gl.glDisable(GL2.GL_LIGHTING);
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {    
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();  
        
        //evita a divisao por zero
        if(height == 0) height = 1;
           
        //ativa a matriz de projecao
        gl.glMatrixMode(GL2.GL_PROJECTION);      
        gl.glLoadIdentity(); //ler a matriz identidade

        //projecao ortogonal sem a correcao do aspecto
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
        
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //ler a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }   
    
    //Desenha o fogo da fornalha
    public void drawFire(GL2 gl, float furnaceX, float furnaceY) {
        gl.glPointSize(3.0f);
        gl.glBegin(GL2.GL_POINTS);

        // Número de pontos de fogo
        int firePointCount = 30;
        for (int i = 0; i < firePointCount; i++) {
            // Posições aleatórias ao redor da fornalha
            float offsetX = (float) (Math.random() * 20 - 10);  // Posição aleatória em X
            float offsetY = (float) (Math.random() * 10);  // Posição aleatória em Y
            float offsetZ = 90.0f;

            gl.glColor3f(1.0f, 0.0f, 0.0f);  // Cor do fogo (vermelho)
            gl.glVertex3f(furnaceX + offsetX, furnaceY + offsetY, offsetZ);  // Desenha o ponto de fogo
        }
        gl.glEnd();  // Finaliza o desenho de pontos
    }
    
    //Helper para renderizar o disco
    public void renderDisc(GL2 gl, float x, float y) {
            gl.glPushMatrix(); 
            gl.glTranslatef(x, y + 5.0f, 40); 
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            disc.generateDisk(gl);
            gl.glPopMatrix();
    }
    
    //Helper da animação da marreta na bigorna, passa o ângulo necessário
    public void updateRotation() {
        angle += 6.2f;  // Incrementa o ângulo (pode ajustar a velocidade da rotação)
        if (angle > 90.0f) {
            angle = 0.0f;  // Reseta o ângulo após uma rotação completa
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}         
}