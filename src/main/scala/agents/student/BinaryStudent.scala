package agents.student

import network.Book
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class BinaryStudent(id: Int, typ: Int) extends Student {

  override val network: MultiLayerNetwork = Book.binaryNetwork

  override def fit(sample: DataSet): Unit = {
    network.fit(DataSetManipulator.binarize(sample, typ))
  }

  override def predict(sample: DataSet): Array[Int] = {
    network.predict(sample.getFeatures)
  }
}
