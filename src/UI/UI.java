package UI;

import filosofoscomensales.Filosofo;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public final class UI extends javax.swing.JFrame {
    public static Filosofo[] fs = new Filosofo[5];
    static ArrayList<Semaphore> palillo = new ArrayList<>();
    public static JLabel[] iterations, states, dones, dishes, chopsticks, statesImages;
    public boolean active;
    ImageIcon philosopher, edish, ldish, rdish, fdish, chop, nothing, eating, thinking, waiting, satisfied;
    
    public UI() {
        initComponents();
        initLabels();
        active = false;
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        continueButton.setEnabled(false);
    }
    
    public void initLabels(){
        philosopher = new ImageIcon(getClass().getResource("/misc/philosopher.png"));
        edish = new ImageIcon(getClass().getResource("/misc/emptydish.png"));
        rdish = new ImageIcon(getClass().getResource("/misc/rdish.png"));
        ldish = new ImageIcon(getClass().getResource("/misc/ldish.png"));
        fdish = new ImageIcon(getClass().getResource("/misc/bothdish.png"));
        chop = new ImageIcon(getClass().getResource("/misc/chop.png"));
        nothing = new ImageIcon(getClass().getResource("/misc/nothing.png"));
        eating = new ImageIcon(getClass().getResource("/misc/eating.png"));
        thinking = new ImageIcon(getClass().getResource("/misc/thinking.png"));
        waiting = new ImageIcon(getClass().getResource("/misc/wating.png"));
        satisfied = new ImageIcon(getClass().getResource("/misc/done.png"));
        philo2.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(philosopher.getImage()), 360/5)));
        philo3.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(philosopher.getImage()), (360/5)*2)));
        philo4.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(philosopher.getImage()), (360/5)*3)));
        philo5.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(philosopher.getImage()), (360/5)*4)));
        dish1.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(edish.getImage()), (360/5)*0)));
        dish2.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(edish.getImage()), (360/5)*1)));
        dish3.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(edish.getImage()), (360/5)*2)));
        dish4.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(edish.getImage()), (360/5)*3)));
        dish5.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(edish.getImage()), (360/5)*4)));
        chop1.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(chop.getImage()), 330)));
        chop2.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(chop.getImage()), 330 + 65)));
        chop3.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(chop.getImage()), 330 + (65*2))));
        chop4.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(chop.getImage()), 330 + (70*3))));
        chop5.setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(chop.getImage()), 330 + (70*4))));
    }
    
    public static BufferedImage toBufferedImage(Image img){
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
    
    public void startrun(){
        Random r = new Random();
        for(int i = 0; i < 5; i++){
            palillo.add(new Semaphore(1, true));
        }
        for(int i = 0; i < 5;i++){
            Filosofo f = new Filosofo(palillo, i, 5, r.nextInt(10-1)+1);
            f.start();
            fs[i] = f;
        }
        iterations = new JLabel[5];
        iterations[0] = iter1;
        iterations[1] = iter2;
        iterations[2] = iter3;
        iterations[3] = iter4;
        iterations[4] = iter5;
        states = new JLabel[5];
        states[0] = state1;
        states[1] = state2;
        states[2] = state3;
        states[3] = state4;
        states[4] = state5;
        dones = new JLabel[5];
        dones[0] = done1;
        dones[1] = done2;
        dones[2] = done3;
        dones[3] = done4;
        dones[4] = done5;
        dishes = new JLabel[5];
        dishes[0] = dish1;
        dishes[1] = dish2;
        dishes[2] = dish3;
        dishes[3] = dish4;
        dishes[4] = dish5;
        chopsticks = new JLabel[5];
        chopsticks[0] = chop2;
        chopsticks[1] = chop3;
        chopsticks[2] = chop4;
        chopsticks[3] = chop5;
        chopsticks[4] = chop1;
        statesImages = new JLabel[5];
        statesImages[0] = stateImage2;
        statesImages[1] = stateImage3;
        statesImages[2] = stateImage4;
        statesImages[3] = stateImage5;
        statesImages[4] = stateImage1;
        active = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        pauseButton.setEnabled(true);
        continueButton.setEnabled(false);
        execResult.setText("");
        Thread check = new Thread(){
            public void run(){
                while(active){
                    boolean alldone = true, interblock = true;
                    for(int i = 0; i < 5; i++){
                        if(!fs[i].isDone()) alldone = false;
                        if(fs[i].estado != 1) interblock = false;
                    }
                    redo();
                    if(alldone){
                        active = false;
                        execResult.setText("Termino toda la ejecucion, no hubo interbloqueo.");
                        startButton.setEnabled(true);
                    }
                    if(interblock){
                        active = false;
                        execResult.setText("Hubo interbloqueo");
                        startButton.setEnabled(true);
                    }
                }
                redo();
            }
        };
        check.start();
    }
    
    public void redo(){
        for(int i = 0; i < 5; i++){
            iterations[i].setText(fs[i].iteraciones + "");
            states[i].setText(num2satate(fs[i].estado));
            dones[i].setText(fs[i].hecho + "");
            if(fs[i].rigth && fs[i].left){
                dishes[i].setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(fdish.getImage()), (360/5)*i)));
            }else if(fs[i].rigth){
                dishes[i].setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(rdish.getImage()), (360/5)*i)));
            }else if(fs[i].left){
                dishes[i].setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(ldish.getImage()), (360/5)*i)));
            }else{
                dishes[i].setIcon(new ImageIcon(rotateImageByDegrees(toBufferedImage(edish.getImage()), (360/5)*i)));
            }
            if(available(i)){
                chopsticks[i].setVisible(true);
            }else{
                chopsticks[i].setVisible(false);
            }
            switch (fs[i].estado) {
                case 0:
                    statesImages[i].setIcon(thinking);
                    break;
                case 1:
                    statesImages[i].setIcon(waiting);
                    break;
                case 2:
                    statesImages[i].setIcon(eating);
                    break;
                case 3:
                    statesImages[i].setIcon(satisfied);
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
    
    public boolean available(int i){
        if(palillo.get(i).availablePermits() == 1){
            return true;
        }
        return false;
    }

    public String num2satate(int num){
        //0 = Pensando, 1 = Esperando, 2 = Comiendo, 3 = Saciado.
        if(num == 0) return "Pensando";
        if(num == 1) return "Esperando";
        if(num == 2) return "Comiendo";
        if(num == 3) return "Saciado";
        return "Invalid";
    }
    
    public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filo1 = new javax.swing.JLabel();
        filo2 = new javax.swing.JLabel();
        filo3 = new javax.swing.JLabel();
        filo4 = new javax.swing.JLabel();
        filo5 = new javax.swing.JLabel();
        iter1 = new javax.swing.JLabel();
        iter2 = new javax.swing.JLabel();
        iter3 = new javax.swing.JLabel();
        iter4 = new javax.swing.JLabel();
        iter5 = new javax.swing.JLabel();
        done1 = new javax.swing.JLabel();
        done2 = new javax.swing.JLabel();
        done3 = new javax.swing.JLabel();
        done4 = new javax.swing.JLabel();
        done5 = new javax.swing.JLabel();
        state1 = new javax.swing.JLabel();
        state2 = new javax.swing.JLabel();
        state3 = new javax.swing.JLabel();
        state4 = new javax.swing.JLabel();
        state5 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        iter = new javax.swing.JLabel();
        done = new javax.swing.JLabel();
        state = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        execResult = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        continueButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        stateImage1 = new javax.swing.JLabel();
        stateImage2 = new javax.swing.JLabel();
        stateImage3 = new javax.swing.JLabel();
        stateImage4 = new javax.swing.JLabel();
        stateImage5 = new javax.swing.JLabel();
        chop5 = new javax.swing.JLabel();
        chop4 = new javax.swing.JLabel();
        chop3 = new javax.swing.JLabel();
        chop2 = new javax.swing.JLabel();
        chop1 = new javax.swing.JLabel();
        philo5 = new javax.swing.JLabel();
        philo4 = new javax.swing.JLabel();
        philo3 = new javax.swing.JLabel();
        philo2 = new javax.swing.JLabel();
        philo1 = new javax.swing.JLabel();
        dish5 = new javax.swing.JLabel();
        dish4 = new javax.swing.JLabel();
        dish3 = new javax.swing.JLabel();
        dish2 = new javax.swing.JLabel();
        dish1 = new javax.swing.JLabel();
        table = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(254, 254, 254));
        setResizable(false);

        filo1.setText("Filosofo 1:");

        filo2.setText("Filosofo 2:");

        filo3.setText("Filosofo 3:");

        filo4.setText("Filosofo 4:");

        filo5.setText("Filosofo 5:");

        iter1.setText("0");

        iter2.setText("0");

        iter3.setText("0");

        iter4.setText("0");

        iter5.setText("0");

        done1.setText("0");

        done2.setText("0");

        done3.setText("0");

        done4.setText("0");

        done5.setText("0");

        state1.setText("estado");

        state2.setText("estado");

        state3.setText("estado");

        state4.setText("estado");

        state5.setText("estado");

        name.setFont(new java.awt.Font("Ubuntu", 1, 10)); // NOI18N
        name.setText("Nombre");

        iter.setFont(new java.awt.Font("Ubuntu", 1, 10)); // NOI18N
        iter.setText("Iteraciones totales");

        done.setFont(new java.awt.Font("Ubuntu", 1, 10)); // NOI18N
        done.setText("Hechas");

        state.setFont(new java.awt.Font("Ubuntu", 1, 10)); // NOI18N
        state.setText("Estados");

        startButton.setText("Iniciar ejecucion");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Detener ejecucion");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        pauseButton.setText("Pausar ejecucion");
        pauseButton.setToolTipText("");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        continueButton.setText("Continuar ejecucion");
        continueButton.setToolTipText("");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(315, 315));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        stateImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/nothing.png"))); // NOI18N
        jPanel1.add(stateImage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, -1, -1));

        stateImage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/nothing.png"))); // NOI18N
        jPanel1.add(stateImage2, new org.netbeans.lib.awtextra.AbsoluteConstraints(222, 23, -1, -1));

        stateImage3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/nothing.png"))); // NOI18N
        jPanel1.add(stateImage3, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 170, -1, -1));

        stateImage4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/nothing.png"))); // NOI18N
        jPanel1.add(stateImage4, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 274, -1, -1));

        stateImage5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/nothing.png"))); // NOI18N
        jPanel1.add(stateImage5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        chop5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/chop.png"))); // NOI18N
        jPanel1.add(chop5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, -1, -1));

        chop4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/chop.png"))); // NOI18N
        jPanel1.add(chop4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, -1, -1));

        chop3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/chop.png"))); // NOI18N
        jPanel1.add(chop3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, -1, -1));

        chop2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/chop.png"))); // NOI18N
        jPanel1.add(chop2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, -1, -1));

        chop1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/chop.png"))); // NOI18N
        jPanel1.add(chop1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        philo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/philosopher.png"))); // NOI18N
        jPanel1.add(philo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        philo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/philosopher.png"))); // NOI18N
        jPanel1.add(philo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 160, -1, -1));

        philo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/philosopher.png"))); // NOI18N
        jPanel1.add(philo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, -1, -1));

        philo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/philosopher.png"))); // NOI18N
        jPanel1.add(philo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, -1, -1));

        philo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/philosopher.png"))); // NOI18N
        jPanel1.add(philo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        dish5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/emptydish.png"))); // NOI18N
        jPanel1.add(dish5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, -1, -1));

        dish4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/emptydish.png"))); // NOI18N
        jPanel1.add(dish4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, -1, -1));

        dish3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/emptydish.png"))); // NOI18N
        jPanel1.add(dish3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, -1, -1));

        dish2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/emptydish.png"))); // NOI18N
        jPanel1.add(dish2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, -1, -1));

        dish1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/emptydish.png"))); // NOI18N
        jPanel1.add(dish1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, -1, -1));

        table.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misc/referencetable.png"))); // NOI18N
        jPanel1.add(table, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(153, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(stopButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pauseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(continueButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(execResult)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(filo5)
                                            .addComponent(filo4)
                                            .addComponent(filo3)
                                            .addComponent(filo1)
                                            .addComponent(name))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(iter5)
                                            .addComponent(iter)
                                            .addComponent(iter4)
                                            .addComponent(iter3)
                                            .addComponent(iter1)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(filo2)
                                        .addGap(18, 18, 18)
                                        .addComponent(iter2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(done2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(done5)
                                    .addComponent(done4)
                                    .addComponent(done3)
                                    .addComponent(done1)
                                    .addComponent(done, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(state2)
                                    .addComponent(state5)
                                    .addComponent(state4)
                                    .addComponent(state3)
                                    .addComponent(state)
                                    .addComponent(state1)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pauseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(continueButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(state, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(done, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(state1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(done1)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(iter1)
                                            .addComponent(filo1)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(iter, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(name, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(26, 26, 26)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(state2)
                                .addComponent(done2))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(filo2)
                                .addComponent(iter2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(state3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(done3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(iter3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(filo3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(state4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(done4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(iter4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(filo4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(state5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(done5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(iter5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(filo5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(execResult)))
                .addGap(36, 36, 36)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        filo1.getAccessibleContext().setAccessibleName("filo1_name");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        startrun();
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        active = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        continueButton.setEnabled(false);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        active = false;
        for(int i = 0; i < 5; i++){
            try {
                palillo.get(i).wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        pauseButton.setEnabled(false);
        continueButton.setEnabled(true);
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueButtonActionPerformed
        active = true;
        for(int i = 0; i < 5; i++){
            palillo.get(i).notify();
        }
        pauseButton.setEnabled(true);
        continueButton.setEnabled(false);
    }//GEN-LAST:event_continueButtonActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel chop1;
    private javax.swing.JLabel chop2;
    private javax.swing.JLabel chop3;
    private javax.swing.JLabel chop4;
    private javax.swing.JLabel chop5;
    private javax.swing.JButton continueButton;
    private javax.swing.JLabel dish1;
    private javax.swing.JLabel dish2;
    private javax.swing.JLabel dish3;
    private javax.swing.JLabel dish4;
    private javax.swing.JLabel dish5;
    private javax.swing.JLabel done;
    private javax.swing.JLabel done1;
    private javax.swing.JLabel done2;
    private javax.swing.JLabel done3;
    private javax.swing.JLabel done4;
    private javax.swing.JLabel done5;
    private javax.swing.JLabel execResult;
    private javax.swing.JLabel filo1;
    private javax.swing.JLabel filo2;
    private javax.swing.JLabel filo3;
    private javax.swing.JLabel filo4;
    private javax.swing.JLabel filo5;
    private javax.swing.JLabel iter;
    private javax.swing.JLabel iter1;
    private javax.swing.JLabel iter2;
    private javax.swing.JLabel iter3;
    private javax.swing.JLabel iter4;
    private javax.swing.JLabel iter5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel name;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel philo1;
    private javax.swing.JLabel philo2;
    private javax.swing.JLabel philo3;
    private javax.swing.JLabel philo4;
    private javax.swing.JLabel philo5;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel state;
    private javax.swing.JLabel state1;
    private javax.swing.JLabel state2;
    private javax.swing.JLabel state3;
    private javax.swing.JLabel state4;
    private javax.swing.JLabel state5;
    private javax.swing.JLabel stateImage1;
    private javax.swing.JLabel stateImage2;
    private javax.swing.JLabel stateImage3;
    private javax.swing.JLabel stateImage4;
    private javax.swing.JLabel stateImage5;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel table;
    // End of variables declaration//GEN-END:variables
}
