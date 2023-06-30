/*
  Trabalho: Integracao numerica por regra do Trapezio.
  Disciplina: Programação Concorrente (ICP361)
  Profesora: Silvana Rossetto
  Autor: Eduardo Kota Otomo
  DRE: 118147443
*/

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.Math;
import java.util.function.Function;
import java.time.Duration;

class Total {
  //recurso compartilhado
  private double total;
  public Total() { 
     this.total = 0; 
  }
  //escrita sobre o recurso compartilhado
  public synchronized void inc(double incremento) { 
     this.total += incremento; 
  }
  //leitura sobre o recurso compartilhado
  public synchronized double get() { 
     return this.total;
  }
}

class Soma extends Thread{

  Function<Double, Double> f;

  private int id; // id da thread
  private int nT; // numero de threads
  private int n; // quantidade de subintervalos
  private double a; // a em [a,b]
  private double b; // b em [a,b]
  private double subtotal;
  private Total total;

  // construtor
  public Soma(double a, double b, int n, int id, int nT, Total total, Function<Double, Double> f) { 
    this.a = a;
    this.b = b;
    this.n = n;
    this.id = id;
    this.nT = nT;
    this.subtotal = 0;
    this.total = total;
    this.f = f;
  }

  public void run() {
    double h = (b - a)/n;
    for (int i = id; i < n; i += nT) {
      double termo = f.apply(a + i * h) ;
      termo += f.apply(a + (i + 1) * h) ;
      termo *= h;
      termo /= 2;
      subtotal += termo;
    }
    total.inc(subtotal);
  }

  public double get() {
    return this.subtotal;
  }
}

// classe do metodo main
class Trabalho {
    public static void main(String[] args) {

    Function<Double, Double> f;
    Function<Double, Double> F;
    int nT = 1;
    int n = 1;
    double a = 0;
    double b = 0;
    double integralExata = 0;
    double erroAbsoluto = 0;
    double erroRelativo = 0;


    Scanner sc = new Scanner(System.in);

    System.out.println("Funcoes teste: ");
    System.out.println("1: x");
    System.out.println("2: x^2");
    System.out.println("3: x^3");
    System.out.println("4: cos(x)");
    System.out.println("5: sen(x)");
    System.out.print("Escolha: ");
    String escolha = sc.nextLine();
    System.out.println();
    switch (escolha) {
      case "2":
        System.out.println("f(x)=x^2,  F(x)=x^3/3\n");
        f = x -> x*x;
        F = x -> x*x*x/3;
        break;
      case "3":
        System.out.println("f(x)=x^3,  F(x)=x^4/4\n");
        f = x -> x*x*x;
        F = x -> x*x*x/4;
        break;
      case "4":
        System.out.println("f(x)=cos(x),  F(x)=sen(x)\n");
        f = x -> Math.cos(x);
        F = x -> Math.sin(x);
        break;
      case "5":
        System.out.println("f(x)=sen(x),  F(x)=-cos(x)\n");
        f = x -> Math.sin(x);
        F = x -> -Math.cos(x);
        break;
      default:
        System.out.println("f(x)=x,  F(x)=x^2/2\n");
        f = x -> x;
        F = x -> x*x/2;   
    }

    System.out.print("Numero de Threads: ");
    try{
      nT = sc.nextInt();
      if (nT < 1) {
        nT = 1;
      }
    } catch(InputMismatchException e1) {
      nT = 1;
      sc.next();
    } finally {
      if (nT==1){
        System.out.println("\n1 Thread\n");
      } else {
        System.out.printf("%d thread(s)\n\n", nT);
      }
    }
    System.out.print("Numero de subintervalos: ");
    try{
      n = sc.nextInt();
      if (n < 1) {
        n = 10;
        sc.next();
      }
    } catch(InputMismatchException e2) {
      n = 10;
      sc.next();
    } finally {
      System.out.printf("n = %d subintervalos\n\n", n);
    }

    try{
      System.out.print("a = ");
      a = sc.nextDouble();
    } catch(InputMismatchException e3) {
      a = 0;
      sc.next();
    }

    try{
      System.out.print("b = ");
      b = sc.nextDouble();
    } catch(InputMismatchException e4) {
      b = 1;
      sc.next();
    }

    System.out.printf("\nIntervalo [a,b] = [%f,%f]\n", a,b);

    Total integral = new Total();

    // timer
    long inicio = System.nanoTime();

    if (a != b){

      Thread[] threads = new Thread[nT];

      // cria threads
      for (int id = 0; id < threads.length; id++) {
        // final String m = "Ola da thread " + i;
        threads[id] = new Soma(a,b, n, id, nT, integral, f);
      }

      // inicia threads
      for (int id = 0; id < threads.length; id++) {
        threads[id].start();
      }

      // esperar pelo termino de todas as threads
      for (int id = 0; id < threads.length; id++) {
        try {
          threads[id].join();
        } catch (InterruptedException e) {
          return;
        }
      }
    }

    long tempo = System.nanoTime() - inicio;

    integralExata = F.apply(b)-F.apply(a);
    erroAbsoluto = Math.abs(integral.get() - integralExata);
    erroRelativo = erroAbsoluto;
    if (integralExata !=0){
      erroRelativo = erroAbsoluto / integralExata;
    }


    
    System.out.printf("Integral numerica : %.10f \n",integral.get());

    System.out.printf("Integral exata: %.10f \n", integralExata);

    System.out.printf("Erro relativo: %.10f %% \n", erroRelativo/100 );

    System.out.printf("Tempo: %d ns \n\n", tempo);
  
    }
}


