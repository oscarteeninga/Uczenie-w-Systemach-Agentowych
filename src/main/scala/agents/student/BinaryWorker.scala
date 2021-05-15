package agents.student

import network.Data
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class BinaryWorker(id: Int, typ: Int) extends Worker {

  override val network: MultiLayerNetwork = Data.binaryNetwork

  override def fit(sample: DataSet): Unit = {
    network.fit(DataSetManipulator.binarize(sample, typ))
  }

  override def predict(sample: DataSet): Array[Int] = {
    network.predict(sample.getFeatures)
  }
}
