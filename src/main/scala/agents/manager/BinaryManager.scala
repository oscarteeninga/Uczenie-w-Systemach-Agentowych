package agents.manager

import akka.actor.ActorRef
import command.{Exam, Learn}
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class BinaryManager(workerRefs: List[ActorRef]) extends Manager(workerRefs) {

  def workerRefToType: List[(Int, ActorRef)] = workerRefs.map(ref => (workerRefs.indexOf(ref) % 10, ref))

  def beginLearn(dataSet: DataSet): Unit = {
    workerRefToType.foreach {
      case (typ, ref) => ref ! Learn(DataSetManipulator.binarize(dataSet, typ))
    }
  }

  def beginExam(dataSet: DataSet): Unit = {
    workerRefToType.foreach {
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
