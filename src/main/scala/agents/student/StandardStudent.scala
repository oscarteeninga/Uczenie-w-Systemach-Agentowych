package agents.student

import network.Book
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.dataset.DataSet

case class StandardStudent(id: Int) extends Student {

  override val network: MultiLayerNetwork = Book.network

  override def fit(sample: DataSet): Unit = {
    network.fit(sample)
  }

  override def predict(sample: DataSet): Array[Int] = {
    network.predict(sample.getFeatures)
  }
}
