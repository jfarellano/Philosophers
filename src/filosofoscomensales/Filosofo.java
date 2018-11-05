package filosofoscomensales;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Filosofo extends Thread {
    
    public ArrayList<Semaphore> palillo;
    public int idFilosofo;
    public int NumeroFilosofos;
    public int iteraciones;
    public int estado; //0 = Pensando, 1 = Esperando, 2 = Comiendo, 3 = Saciado.
    public int hecho;
    public boolean left, rigth, active, stop;
    
    public Filosofo(ArrayList<Semaphore> palillo, int idFilosofo, int NumeroFilosofos,int iteraciones) {
        this.palillo = palillo;
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
            this.sleep((long) (100 * Math.random()));
            synchronized(this) {
                while(stop) {
                    wait();
                    //System.out.println("Im in! - pensar");
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void comer(int iteracion){
        try {
            estado = 2;
            this.sleep((long) (100 * Math.random()));
            synchronized(this) {
                while(stop) {
                    wait();
                    //System.out.println("Im in! - comer");
                }
            }
            hecho += 1;
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void esperando(int iteracion) {
        estado = 1;
        try {
            synchronized(this) {
                while(stop) {
                    wait();
                    //System.out.println("Im in! - esperar");
                }
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
    
    public void stopThread(){
        stop = true;
    }
    
    public synchronized void resumeThread(){
        stop = false;
        notify();
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= iteraciones; i++) {
            try {
                pensar(i);
                esperando(i);
                palillo.get(rigthIndex(idFilosofo)).acquire();
                this.rigth = true;
                palillo.get(idFilosofo).acquire();
                this.left = true;
                comer(i);
                if(isDone()) estado = 3;
                palillo.get(idFilosofo).release();
                this.left = false;
                palillo.get(rigthIndex(idFilosofo)).release();
                this.rigth = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
