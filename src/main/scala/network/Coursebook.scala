package network

import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.evaluation.classification.{Evaluation, ROCMultiClass}

object Coursebook {
  def main(args: Array[String]): Unit = {
    val emnistTrain = Book.emnistTrain
    val emnistTest = Book.emnistTest

    val network = Book.network

    network.init()

    val eachIterations = 10
    network.addListeners(new ScoreIterationListener(eachIterations))

    val numEpochs = 2
    network.fit(emnistTrain, numEpochs)

    val eval = network.evaluate[Evaluation](emnistTest)
    println(eval.accuracy())
    println(eval.precision())
    println(eval.recall())

    val roc = network.evaluateROCMultiClass[ROCMultiClass](emnistTest, 0)
    roc.calculateAUC(1)

    print(eval.stats())
    print(roc.stats())
  }
}
