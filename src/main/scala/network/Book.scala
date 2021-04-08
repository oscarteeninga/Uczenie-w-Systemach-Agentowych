package network

import org.deeplearning4j.datasets.iterator.impl.{EmnistDataSetIterator, MnistDataSetIterator}
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.lossfunctions.LossFunctions

import scala.util.Random

object Book {
  private val trainSize = 5000
  private val testSize = 10
  def rngSeed: Int = Random.nextInt()

  val mnistTrain = new MnistDataSetIterator(trainSize, true, rngSeed)
  val mnistTest = new MnistDataSetIterator(testSize, false, rngSeed)

  private val outputNum = 10

  private val numRows = 28
  private val numColumns = 28

  def network: MultiLayerNetwork = {
    val conf = new NeuralNetConfiguration.Builder()
      .seed(rngSeed)
      .updater(new Adam())
      .l2(1e-4)
      .list()
      .layer(new DenseLayer.Builder()
        .nIn(numRows * numColumns)
        .nOut(128)
        .activation(Activation.RELU)
        .build())
      .layer(new DenseLayer.Builder()
        .nOut(64)
        .activation(Activation.RELU)
        .build())
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
        .nOut(outputNum)
        .activation(Activation.SOFTMAX)
        .build())
      .build()

    new MultiLayerNetwork(conf)
  }
}
