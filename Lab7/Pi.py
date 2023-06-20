#/* Disciplina: Programacao Concorrente */
#/* Profa: Silvana Rossetto */
#/* Módulo 4 - Laboratório: 7 */
#/* Aluno: Eduardo Kota Otomo - DRE: 118147443 */ 
#/* Atividade 5 */ 


from threading import Thread, Lock
import time, math

class Pi():
  def __init__(self):
    self.somatorio = 0
    self.lock = Lock()

  def incrementa(self,valor):
    self.lock.acquire()
    self.somatorio += valor
    self.lock.release()

  def getValor(self):
    return (self.somatorio*4)

class IncrementaPi(Thread):
  def __init__(self, id):
    super().__init__()
    self.id=id
    self.lock = Lock()
  def run(self):
    for i in range(self.id,n,k):
      self.lock.acquire()
      pi.incrementa(pow(-1,i)/(2*i+1))
      self.lock.release()

if __name__ == '__main__':

  try:
    # pede ao usuario: digitar o numero de threads
    k = int(input("N# Threads: "))
  except ValueError as e: # trata erro
    # caso a conversao para inteiro falhe, por default:
    k = 4 
  print(k,"threads")

  #cria variavel Pi compartilhada
  pi = Pi()

  n = 100000 #termos

  threads = [IncrementaPi(i) for i in range(0,k)]
  start = time.time()

  #inicia as threads
  for thread in threads:
      thread.start()
  #aguarda as threads terminarem
  for thread in threads:
      thread.join()

  #exibe resultados
  print("Pi =",pi.getValor())
  print("Tempo de execução:",time.time() - start, "s")
  print("%Erro:",(math.pi - pi.getValor())/math.pi, "%")

