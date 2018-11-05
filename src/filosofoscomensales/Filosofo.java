package filosofoscomensales;

import UI.UI;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Filosofo extends Thread {
    
    public UI ui;
    public int idFilosofo;
    public int NumeroFilosofos;
    public int iteraciones;
    public int estado; //0 = Pensando, 1 = Esperando, 2 = Comiendo, 3 = Saciado.
    public int hecho;
    public boolean left, rigth, active, stop;
    
    public Filosofo(UI ui, int idFilosofo, int NumeroFilosofos,int iteraciones) {
        this.ui = ui;
        this.idFilosofo = idFilosofo;
        this.NumeroFilosofos = NumeroFilosofos;
        this.iteraciones = iteraciones;
        this.hecho = 0;
        this.left = false;
        this.rigth = false;
        this.active = true;
        this.stop = false;
    }
    
    private void pensar(int iteracion){
        try {
            estado = 0;
            ui.redoState(idFilosofo, estado);
            this.sleep((long) (10000 * Math.random()));
            synchronized(this) {
                while(stop) {
                    wait();
                    System.out.println("Im in! - pensar");
                }
               //this.sleep((long) (5000 * Math.random()));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void comer(int iteracion){
        try {
            estado = 2;
            ui.redoState(idFilosofo, estado);
            this.sleep((long) (10000 * Math.random()));
            synchronized(this) {
                while(stop) {
                    wait();
                    System.out.println("Im in! - comer");
                }
                //this.sleep((long) (5000 * Math.random()));
            }
            hecho += 1;
            ui.dones[idFilosofo].setText(hecho + "");
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void esperando(int iteracion) {
        estado = 1;
        ui.redoState(idFilosofo, estado);
        try {
            synchronized(this) {
                while(stop) {
                    wait();
                    System.out.println("Im in! - esperar");
                }
               //this.sleep((long) (10000 * Math.random()));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isDone(){
        return hecho == iteraciones;
    }
    
    public int rigthIndex(int i){
        if(i == 0) return 4;
        return i-1;
    }
    
    public void fixDispair(){
        ui.palillo.get(idFilosofo).release();
        this.left = false;
        ui.palillo.get(rigthIndex(idFilosofo)).release();
        this.rigth = false;
    }
    
    public void stopThread(){
        stop = true;
    }
    
    public synchronized void resumeThread(){
        stop = false;
        notify();
    }
 
    
    @Override
    public void run() {
        ui.dones[idFilosofo].setText(0 + "");
        ui.iterations[idFilosofo].setText(iteraciones + "");
        for (int i = 1; i <= iteraciones; i++) {
            try {
                //Think...!
                pensar(i);
                
                //Waiting...!
                esperando(i);
                
                //Right chopstick...!
                ui.palillo.get(rigthIndex(idFilosofo)).acquire();
                ui.redoAvailableChopstick(rigthIndex(idFilosofo), false);
                ui.redoChopstickOnDish(idFilosofo, true, false);
                this.rigth = true;
                
                //Left chopstick...!
                ui.palillo.get(idFilosofo).acquire();
                ui.redoAvailableChopstick(idFilosofo, false);
                ui.redoChopstickOnDish(idFilosofo, true, true);
                this.left = true;
                
                //Eat...!
                comer(i);
                if(isDone()){
                    estado = 3;
                    ui.redoState(idFilosofo, estado);
                    ui.dones[idFilosofo].setText(iteraciones + "");
                }
                
                //Left chopstick...!
                ui.redoChopstickOnDish(idFilosofo, true, false);
                ui.redoAvailableChopstick(idFilosofo, true);
                ui.palillo.get(idFilosofo).release();
                this.left = false;
                
                //Right chopstick...!
                ui.redoChopstickOnDish(idFilosofo, false, false);
                ui.redoAvailableChopstick(rigthIndex(idFilosofo), true);
                ui.palillo.get(rigthIndex(idFilosofo)).release();
                this.rigth = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
