# Relatório final

# Descrição do problema

Programa concorrente escrito em Java para Integração numérica via regra do trapézio.

# Projeto e implementacão da solução concorrente.

A interface `Function<Double, Double> f;` permite escolher diferentes funções `f(x)` escolhidas pelo usuário no menu inicial.   
A interface `Function<Double, Double> F;` é a primitiva de f(x), usado para calcular a integral exata `F(b)-F(a)`

Por exemplo:   
`f = x -> x*x*x` <=> `f(x)=x^3`   
`f = x -> x*x*x*x/4` <=> `F(x)=x^4/4\n`   



Cada thread calcula um subtotal da integral e em seguida soma o resultado ao valor total do recurso compartilhado.
``` java
class Soma extends Thread{
// ...
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
// ...
}
```

Exclusão mútua entre threads foi implementado via synchronized para evitar condição de corrida sobre o recurso compartilhado  
``` java
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
```



Caso o usuário insira entradas inválidas, gerando erro de entrada inválida então o erro é tratado e se assume o valor default.
``` java
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
```
