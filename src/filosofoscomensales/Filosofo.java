/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filosofoscomensales;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class Filosofo extends Thread {
    
    public ArrayList<Semaphore> palillo;
    public int idFilosofo;
    public int NumeroFilosofos;
    public int iteraciones;
    public int estado; //0 = Pensando, 1 = Esperando, 2 = Comiendo, 3 = Saciado.
    public int hecho;
    public boolean left, rigth;
    
    public Filosofo(ArrayList<Semaphore> palillo, int idFilosofo, int NumeroFilosofos,int iteraciones) {
        this.palillo = palillo;
        this.idFilosofo = idFilosofo;
        this.NumeroFilosofos = NumeroFilosofos;
        this.iteraciones = iteraciones;
        this.hecho = 0;
        this.left = false;
        this.rigth = false;
    }
    
    private void pensar(int iteracion){
        try {
            estado = 0;
            //System.out.println(iteracion + ": Filosofo " + idFilosofo + " Pensando! .. ");
            //this.sleep((long) (5000 * Math.sin(Math.random())));
            this.sleep((long) (1000 * Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void comer(int iteracion){
        try {
            estado = 2;
            //System.out.println(iteracion + ": Filosofo " + idFilosofo + " Comiendo!");
            //this.sleep((long) (5000 * Math.cos(Math.random())));
            this.sleep((long) (1000 * Math.random()));
            hecho += 1;
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void esperando(int iteracion) {
        estado = 1;
        //System.out.println(iteracion + ": Filosofo " + idFilosofo + " Esperando!");
    }
    
    public boolean isDone(){
        return hecho == iteraciones;
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= iteraciones; i++) {
            try {
                pensar(i);
                esperando(i);
                palillo.get((idFilosofo + 1) % NumeroFilosofos).acquire();
                this.rigth = true;
                palillo.get(idFilosofo).acquire();
                this.left = true;
                comer(i);
                if(isDone()) estado = 3;
                palillo.get(idFilosofo).release();
                this.left = false;
                palillo.get((idFilosofo + 1) % NumeroFilosofos).release();
                this.rigth = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
