/*  Disciplina: Computacao Concorrente
    Prof.: Silvana Rossetto
    Laboratório: 3
    Programa concorrente em Java que calcula a série que gera Pi
    Autor: Eduardo Kota Otomo
    DRE: 118147443
*/

// $ java Pi <número de termos> <número de threads>

import java.lang.Math;

//--PASSO 1: cria uma classe que estende a classe Thread 
class Calc extends Thread {
  private int k, n, id;
  // k = número de termos da série
  // n = número de threads
  // id = id das threads
  private double part = 0; //parcela da soma

  //--construtor
  public Calc(int k, int n, int id){
      this.k = k;
      this.n = n;
      this.id = id;
  }

  public double getPart(){
    return this.part;
  }

  //--executado pela thread
  public void run() {
    for(int i=id; i<k; i+=n){
      this.part += Math.pow(-1, i) * (1.0 / (2*i + 1));
    }
/*
  //--imprime o id de cada thread na ordem em que finalizam a conta
      System.out.printf(t+" ");
*/
  }

}

//--classe do metodo main
class Pi{
  static int k, n;
  // k = número de termos da série
  // n = número de threads
  static double total = 0, errAbs = 0, errRel = 0;

  public static void main (String[] args) {
    
    // java Pi <número de termos> <número de threads>
    if(args.length!=2){
      System.out.println("Digite:   java Pi <n#termos> <n#threads>");
        System.exit(1);
    }

    k = Integer.parseInt(args[0]);
    n = Integer.parseInt(args[1]);

    //--reserva espaço para um vetor de threads
    Thread[] threads = new Thread[n];

    System.out.printf("%nPara %d termos e %d threads :%n", k, n);

    //--PASSO 2: cria threads da classe que estende Thread
    for (int i=0; i<threads.length; i++) {
        threads[i] = new Calc(k,n,i);
    }

    //--PASSO 3: inicia as threads
    for (int i=0; i<threads.length; i++) {
        threads[i].start();
    }
    //--PASSO 4: espera termino de todas as threads
    for (int i=0; i<threads.length; i++) {
      try { threads[i].join(); } 
      catch (InterruptedException e) { return; }
      
      //soma parcelas em cada thread
      total += ((Calc) threads[i]).getPart();
    }

    total *= 4;

    errAbs = Math.abs(Math.PI - total);
    errRel = errAbs / Math.PI;

    System.out.printf("%n"+
      "%n  Pi calculado:  %.15f %n"+
      "%n  Pi exato:      %.15f %n"+
      "%n  Erro absoluto: %.15f %n "+
      "%n  Erro relativo: %f %% %n %n",      
      total, Math.PI, errAbs, errRel);    
  }
}
