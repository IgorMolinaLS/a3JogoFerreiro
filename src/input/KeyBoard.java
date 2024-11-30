    package input;

    import cena.Cena;
    import com.jogamp.newt.event.KeyEvent;
    import com.jogamp.newt.event.KeyListener;
    import com.jogamp.opengl.GL2;

    public class KeyBoard implements KeyListener {
        private Cena cena;

        public KeyBoard(Cena cena) {
            this.cena = cena;
        }

        private long furnacePlacedTime = 0;
        private long anvilPlacedTime = 0;
        private static final long ANVIL_WAIT_TIME = 1000;
        private static final long WAIT_TIME = 3000;

        //checa se colide com o objeto
        public boolean checkAnvilCollision(float newX, float newY) {
            //colisão X em relação à bigorna
            boolean anvilXCollision = newX + cena.heroWidth > cena.anvilPosition[0] - cena.anvilPosition[2]
                    && newX < cena.anvilPosition[0] + cena.anvilPosition[2];
            //colisão Y em relação à bigorna
            boolean anvilYCollision = newY + cena.heroHeigth > cena.anvilPosition[1]
                    && newY < cena.anvilPosition[1] + cena.anvilPosition[2];

            return anvilXCollision && anvilYCollision;
        }

        public boolean checkTableCollision(float newX, float newY) {
            //colisão X em relação à mesa
            boolean tableXCollision = newX + cena.heroWidth > cena.tablePosition[0] - cena.tablePosition[2] * 2
                    && newX < cena.tablePosition[0] + cena.tablePosition[2] * 2;
            //colisão Y em relação à mesa
            boolean tableYCollision = newY + cena.heroHeigth > cena.tablePosition[1]
                    && newY < cena.tablePosition[1] + cena.tablePosition[2];

            return tableXCollision && tableYCollision;
        }

        public boolean checkChestCollision(float newX, float newY) {
            //colisão X em relação ao baú
            boolean tableXCollision = newX + cena.heroWidth > cena.chestPosition[0] - cena.chestPosition[2]
                    && newX < cena.chestPosition[0] + cena.chestPosition[2];
            //colisão Y em relação ao baú
            boolean tableYCollision = newY + cena.heroHeigth > cena.chestPosition[1]
                    && newY < cena.chestPosition[1] + cena.chestPosition[2];

            return tableXCollision && tableYCollision;
        }

        public boolean checkFurnaceCollision(float newX, float newY) {
            //colisão X em relação à fornalha
            boolean anvilXCollision = newX + cena.heroWidth > cena.furnacePosition[0] - cena.furnacePosition[2]
                    && newX < cena.furnacePosition[0] + cena.furnacePosition[2];
            //colisão Y em relação à fornalha
            boolean anvilYCollision = newY + cena.heroHeigth > cena.furnacePosition[1]
                    && newY < cena.furnacePosition[1] + cena.furnacePosition[2];

            return anvilXCollision && anvilYCollision;
        }

        public boolean checkObjectProximity(float newX, float newY, float[] object) {//cena.anvilPosition
            // Define uma distância máxima (threshold) para "perto"
            float proximityThreshold = 25.0f;

            // Calcula a distância entre o personagem e o objeto
            float distanceX = Math.abs(newX - object[0]);
            float distanceY = Math.abs(newY - object[1]);

            // Verifica se a distância é menor que o limite de proximidade
            return distanceX < proximityThreshold && distanceY < proximityThreshold;
        }

        //Checa se a nova posição do personagem colidirá com algum objeto, se sim, impede que ele se mova
        public void move(int diffX, int diffY) {
            float newX = cena.heroX + diffX;
            float newY = cena.heroY + diffY;

            //se NÃO colidir, pode andar
            if (!checkAnvilCollision(newX, newY) &&
                !checkTableCollision(newX, newY) && 
                !checkChestCollision(newX, newY) &&
                !checkFurnaceCollision(newX, newY)) {
                cena.heroX = newX;
                cena.heroY = newY;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //Esc fecha o jogo
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
                //Se as instruções estiverem na tela, enter as faz sumir
                if (cena.isDialog1Open) {
                    cena.isDialog1Open = false;
                }

                //Lógica da fornalha
                if (checkObjectProximity(cena.heroX, cena.heroY, cena.furnacePosition)
                                          && cena.oreCount > 0 && cena.furnaceCount < 3) {
                    cena.oreCount--;
                    cena.furnaceCount++;
                    cena.isFurnaceActive = true;

                    //Armazena o tempo em que foi colocado na fornalha
                    furnacePlacedTime = System.currentTimeMillis();
                }
                
                //Lógica da bigorna
                if (checkObjectProximity(cena.heroX, cena.heroY, cena.anvilPosition)
                        && cena.barCount > 0 && cena.anvilCount < 3) {
                    if (!cena.isAnvilActive) {
                        cena.barCount--;
                        cena.anvilCount++;
                        cena.isAnvilActive = true;
                        
                        //Armazena o tempo em que foi colocado na bigorna
                        anvilPlacedTime = System.currentTimeMillis();
                    } else {
                        System.out.println("Termine de modelar uma esfera antes de fazer outra!");
                    }
                }

                //Lógica da mesa
                if (checkObjectProximity(cena.heroX, cena.heroY, cena.tablePosition)
                                          && cena.diskCount > 0) {
                    //Controla o posicionamento dos discos na mesa
                    if (!cena.disk1OnTable) {
                        cena.disk1OnTable = true;
                        cena.diskCount--;
                    } else if (cena.disk1OnTable && !cena.disk2OnTable) {
                        cena.disk2OnTable = true;
                        cena.diskCount--;
                    } else if ((cena.disk1OnTable && cena.disk2OnTable) && !cena.disk3OnTable) {
                        cena.disk3OnTable = true;
                        cena.diskCount--;
                    } else if (cena.disk1OnTable && cena.disk2OnTable && cena.disk3OnTable) {
                        System.out.println("Espere os discos serem vendidos!");
                    }
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                //Lógica do baú
                if (checkObjectProximity(cena.heroX, cena.heroY, cena.chestPosition)
                                          && cena.oreCount < 5) {
                    cena.oreCount++;
                }
                
                //Lógica da fornalha
                if (checkObjectProximity(cena.heroX, cena.heroY, cena.furnacePosition)
                                          && cena.furnaceCount > 0) {
                    //Se o tempo tiver passado, desligue a fornalha
                    if (furnacePlacedTime != -1 && System.currentTimeMillis() - furnacePlacedTime >= WAIT_TIME) {
                        cena.furnaceCount--;
                        cena.barCount++;
                        cena.isFurnaceActive = false;
                        
                    } else {
                        System.out.println("Ainda não pode retirar o item, aguarde 3 segundos.");
                    }
                }
                if (checkObjectProximity(cena.heroX, cena.heroY, cena.anvilPosition)
                                          && cena.anvilCount > 0) {
                    //Se o tempo tiver passado, pare a bigorna
                    if (furnacePlacedTime != -1 && System.currentTimeMillis() - anvilPlacedTime >= ANVIL_WAIT_TIME) {
                        cena.anvilCount--;
                        cena.diskCount++;
                    } else {
                        System.out.println("Ainda não pode retirar o item, aguarde 3 segundos.");
                    }
                }
            }

            switch (e.getKeyChar()) {
                //Permite que o personagem se mova, apenas se não estiver usando a bigorna
                case 'w':
                    if (cena.heroY < 50.0f && !cena.isAnvilActive) {
                        move(0, 10);
                    }
                    break;
                case 's':
                    if (cena.heroY > -70.0f && !cena.isAnvilActive) {
                        move(0, -10);
                    }
                    break;
                case 'a':
                    if (cena.heroX > -80.0f && !cena.isAnvilActive) {
                        move(-10, 0);
                    }
                    break;
                case 'd':
                    if (cena.heroX < 80.0f && !cena.isAnvilActive) {
                        move(10, 0);
                    }
                    break;
                case 't':
                    cena.tonalizacao = cena.tonalizacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
                    break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
        }  
    }