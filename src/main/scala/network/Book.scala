package network

import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.lossfunctions.LossFunctions

object Book {
  private val batchSize = 128
  private val emnistSet = EmnistDataSetIterator.Set.BALANCED
  val emnistTrain = new EmnistDataSetIterator(emnistSet, batchSize, true)
  val emnistTest = new EmnistDataSetIterator(emnistSet, batchSize, false)

  private val outputNum = EmnistDataSetIterator.numLabels(emnistSet)
  private val rngSeed = 123
  private val numRows = 28
  private val numColumns = 2


  def network: MultiLayerNetwork = {
    val conf = new NeuralNetConfiguration.Builder()
      .seed(rngSeed)
      .updater(new Adam())
      .l2(1e-4)
      .list()
      .layer(new DenseLayer.Builder()
        .nIn(numRows * numColumns)
        .nOut(1000)
        .activation(Activation.RELU)
        .weightInit(WeightInit.XAVIER)
        .build())
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
        .nIn(1000)
        .nOut(outputNum)
        .activation(Activation.SOFTMAX)
        .weightInit(WeightInit.XAVIER)
        .build())
      .build()

    new MultiLayerNetwork(conf)
  }
}
