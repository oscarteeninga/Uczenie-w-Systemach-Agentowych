package util

import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.cpu.nativecpu.buffer.IntBuffer
import org.nd4j.linalg.dataset.DataSet

object DataSetManipulator {
  def binarize(dataSet: DataSet, typ: Int): DataSet = {
    val features = dataSet.getFeatures
    val labels = dataSet.getLabels.toIntVector.map(label => if (label == typ) 1 else 0)
    val newLabels = new NDArray(new IntBuffer(labels))
    new DataSet(features, newLabels)
  }

}
