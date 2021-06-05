package agents.manager

import akka.actor.ActorRef
import command.{Exam, Learn}
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class BinarySpecializedManager(workerRefs: List[ActorRef]) extends ManagerSpecialized(workerRefs) {

  def workerRefToSpecialization: List[(Int, ActorRef)] = workerRefs.map(ref => (workerRefs.indexOf(ref) % 10, ref))

  def beginLearn(dataSets: List[DataSet]): Unit = {
    workerRefToSpecialization.foreach {
      case (typ, ref) => ref ! Learn(DataSetManipulator.binarize(dataSets(typ), typ))
    }
  }

  def beginExam(dataSet: DataSet): Unit = {
    workerRefToSpecialization.foreach {
      case (typ, ref) => ref ! Exam(DataSetManipulator.binarize(dataSet, typ))
    }
  }

  override def vote(predictions: List[List[Int]]): List[Int] = {
    predictions.head.indices.toList.map { i =>
      var results: Map[Int, Int] = (0 to 9).map(x => (x, 0)).toMap
      predictions.indices.foreach { j =>
        val typ = j % 10
        if (predictions(j)(i) == 1) {
          val votes = results(typ)
          results = results.updated(typ, votes + 1)
        }
      }
      results.toList.maxBy(_._2)._1
    }
  }
}
