package network

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.lossfunctions.LossFunctions

import scala.util.Random

object Data {
  private val trainBatchSize = 6000
  private val testBatchSize = 25
  private def rngSeed: Int = Random.nextInt()

  def mnistTrain = new MnistDataSetIterator(trainBatchSize, true, rngSeed)
  def mnistTest = new MnistDataSetIterator(testBatchSize, false, rngSeed)

  def mnistTrainDatasets: List[DataSet] = (1 to 60000/trainBatchSize).map(_ => mnistTrain.next()).toList
  def mnistTestDatasets: List[DataSet] = (1 to 10000/testBatchSize).map(_ => mnistTest.next()).toList

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
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.L1)
        .nOut(outputNum)
        .activation(Activation.SOFTMAX)
        .build())
      .build()
    new MultiLayerNetwork(conf)
  }

  def binaryNetwork: MultiLayerNetwork = {
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
        .nOut(2)
        .activation(Activation.SOFTMAX)
        .build())
      .build()
    new MultiLayerNetwork(conf)
  }
}
