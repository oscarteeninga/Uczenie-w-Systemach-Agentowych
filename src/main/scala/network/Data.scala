package network

import java.util

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

  private val trainBatchSize = 600
  private val testBatchSize = 200

  private def rngSeed: Int = Random.nextInt()

  def mnistTrain = new MnistDataSetIterator(trainBatchSize, true, rngSeed)
  def mnistTest = new MnistDataSetIterator(testBatchSize, false, rngSeed)

  def mnistTrainDatasets: List[DataSet] = (1 to 6000/trainBatchSize).map(_ => mnistTrain.next()).toList
  def mnistTestDatasets: List[DataSet] = (1 to 1000/testBatchSize).map(_ => mnistTest.next()).toList

  private val topLabelRatio = 0.8

  def mnistTrainSpecializedDatasets: List[List[DataSet]] = (1 to 6000/trainBatchSize).map(_ => mnistTrainDatasetsByLabel).toList

  def mnistTrainDatasetsByLabel: List[DataSet] = {
    val topLabelNumber = (topLabelRatio * trainBatchSize).round.intValue()
    val restLabelsNumber = ((1-topLabelRatio) * trainBatchSize).round.intValue()
    val topFilled = Array.fill(10)(0)
    val restFilled = Array.fill(10)(0)
    val topExpected = Array.fill(10)(topLabelNumber)
    val restExpected = Array.fill(10)(restLabelsNumber)
    val mnistIt = new MnistDataSetIterator(1, true, rngSeed)
    val resultDatasets = Array.fill(10)(new util.ArrayList[DataSet]())
    while (!topFilled.sameElements(topExpected) || !restFilled.sameElements(restExpected)){
      val nextDataset = mnistIt.next()
      val label = nextDataset.getLabels.toIntVector.indexOf(1)
      for (i <- 0 until 10){
        if (i == label){
          if (topFilled(i) < topLabelNumber){
            resultDatasets(i).add(nextDataset)
            topFilled(i) = topFilled(i) + 1
          }
        }else{
          if (restFilled(i) < restLabelsNumber){
            resultDatasets(i).add(nextDataset)
            restFilled(i) = restFilled(i) + 1
          }
        }
      }
    }
    val res = resultDatasets.map(data => DataSet.merge(data)).toList
    res.foreach(dataset => dataset.shuffle())
    res
  }

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
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.L2)
        .nOut(10)
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
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.L2)
        .nOut(2)
        .activation(Activation.RELU)
        .build())
      .build()
    new MultiLayerNetwork(conf)
  }
}
