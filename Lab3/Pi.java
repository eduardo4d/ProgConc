
/*  Disciplina: Computacao Concorrente
    Prof.: Silvana Rossetto
    Laboratório: 3
    Programa concorrente em Java que calcula série que gera Pi
    Autor: Eduardo Kota Otomo
    DRE: 118147443
*/

// $ java Pi <número de termos> <número de threads>

//--PASSO 1: cria uma classe que estende a classe Thread 
class Calc extends Thread {
  private int n, ts, t;
  // n = número de termos da série
  // ts = número de threads
  // t = id das threads
  private double part = 0; //parcela da soma

  //--construtor
  public Calc(int n, int ts, int t){
      this.n = n;
      this.ts = ts;
      this.t = t;
  }

  public double getPart(){
    return this.part;
  }

  //--executado pela thread
  public void run() {
    for(int i=t; i<n; i+=ts){
      this.part += Math.pow(-1, i) * (1.0 / (2*i + 1));
    }
  }

}

//--classe do metodo main
class Pi{
  static int n, ts;
  // n = número de termos da série
  // tN = número de threads
  static double total = 0;

  public static void main (String[] args) {
    
    // java Pi <número de termos> <número de threads>
    if(args.length!=2){
      System.out.println("Digite: java Pi <n#termos> <n#threads>");
        System.exit(1);
    }

    n = Integer.parseInt(args[0]);
    ts = Integer.parseInt(args[1]);

    //--reserva espaço para um vetor de threads
    Thread[] threads = new Thread[ts];

    //--PASSO 2: cria threads da classe que estende Thread
    for (int i=0; i<threads.length; i++) {
        threads[i] = new Calc(n,ts,i);
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
    System.out.printf(
      "%nPara %d termos e %d threads %n"+
      "%n  Pi calculado: %.15f %n"+
      "%n  Pi exato:     %.15f %n"+
      "%n  Erro:         %f %n %n", 
      n, ts, total, Math.PI, Math.abs(Math.PI - total) );    
  }
}
