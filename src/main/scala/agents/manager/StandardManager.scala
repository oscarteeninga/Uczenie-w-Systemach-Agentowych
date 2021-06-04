package agents.manager

import akka.actor.ActorRef
import command._
import network.Data
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class StandardManager(workerRefs: List[ActorRef]) extends Manager(workerRefs) {
  def workerRefToPartition: List[(Int, ActorRef)] = workerRefs.map(ref =>
    (workerRefs.indexOf(ref) % Data.features_partitions_number, ref))

  def beginLearn(dataSet: DataSet): Unit = {
    workerRefToPartition.foreach {
      case (partition, ref) => ref ! Learn(
        DataSetManipulator.choose_features(dataSet, Data.features_partitions_number, partition))
    }
  }

  def beginExam(dataSet: DataSet): Unit = {
    workerRefToPartition.foreach {
      case (partition, ref) => ref ! Exam(
        DataSetManipulator.choose_features(dataSet, Data.features_partitions_number, partition))
    }
  }
}
