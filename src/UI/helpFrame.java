package UI;

public class helpFrame extends javax.swing.JFrame {

    public helpFrame() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("En este manual cubriremos el funcionamiento del programa, \npara esto lo dividiremos en las tres secciones principales: \nArea de control (Donde encontramos los diferentes botones\npara controlar el comportamiento del software), \nArea informativa (En esta area encontramos un resumen\nde cuantas iteraciones debe realizar cada filisofo en total,\ncuantas ha realizado y ademas su estado actual) y\npor ultimo el area de animacion (En esta podra ver\ngraficamente que esta haciendo cada filosofo ademas\nde poder visualizar la posicion de los palillos en tiempo real).\n\nEn el area de control encontraremos cuatro botones. El primero \nIniciar Ejecucion, este boton empezara la simulacion, las\nanimaciones comenzaran. El segundo, Detener Ejecucion, termina \ntoda la simulacion con la unica posibilidad de empezar una \nnueva. El tercero, Pausar ejecucion, este detiene la simulacion\ntemporalmente con la posibilidad de reanudar la simulacion con\nel quinto y ultimo boton Continuar ejecucion.\n\nEn el area informativa tenemos en la primera columna a los \nfilosofos por filas. En la segunda columna las iteraciones \ntotales osea cuantas veces tienen estipulado comer cada \nfilosofo. En la tercera columna, Hechas, cuantas iteraciones \nexitosas han hecho los filosofos osea cuantas veces han comido. \nEn la ultima la cuarta tenemos la columnda de Estados los \ncuales pueden ser Pensando, Esperando, Comiendo o Saciado.\n\nEn el area de animacion el primer filosofo seria el que esta \nmas arriba en la mesa, viendo la mesa como una circunferencia \nunitaria el que esta a noventa grados, luego la numeracion \nseguiria segun las manecillas del reloj osea que el segundo \nfilosofo seria el que esta a la derecha del primero, a 45 \ngrados y asi sucesivamente. Todos los filosofos son una \nrepresentancion en pixeles de aristoteles y todos tienen una \nnube, un plato, y un palillo en la mesa. En la nube se pone la \nrepresentacion animada de los estados de los filosofos.\nSi aparece un signo de interrogacion estan pensando   ");
        jTextArea1.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        jLabel1.setText("Manual de usuario.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
