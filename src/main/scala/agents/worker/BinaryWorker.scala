package agents.worker

import network.Data
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.dataset.DataSet

case class BinaryWorker(id: Int, typ: Int) extends Worker {

  override val network: MultiLayerNetwork = Data.binaryNetwork

  override def fit(sample: DataSet): Unit = {
    network.fit(sample)
  }

  override def predict(sample: DataSet): Array[Int] = {
    network.predict(sample.getFeatures)
  }
}
