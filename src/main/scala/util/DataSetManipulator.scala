package util

import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.dataset.DataSet

object DataSetManipulator {
  def binarize(dataSet: DataSet, typ: Int): DataSet = {
    val features = dataSet.getFeatures
    val labels = dataSet.getLabels.toIntMatrix.map(labels => if (labels(typ) == 1) Array(0.0, 1.0) else Array(1.0, 0.0))
    val newLabels = new NDArray(labels)
    new DataSet(features, newLabels)
  }

  def choose_features(dataSet: DataSet, partitionNumber: Int, partitionIndex: Int): DataSet = {
    val partialFeatures = dataSet.getFeatures.toDoubleMatrix.map(features => {
      val partitionLen = features.length / partitionNumber
      features.slice(partitionIndex * partitionLen, (partitionIndex+1) * partitionLen)
    })
    new DataSet(new NDArray(partialFeatures), dataSet.getLabels)
  }
}
