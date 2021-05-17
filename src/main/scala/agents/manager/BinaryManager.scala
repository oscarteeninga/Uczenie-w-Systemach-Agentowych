package agents.manager

import akka.actor.ActorRef
import command.{Exam, Learn}
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class BinaryManager(workerRefs: List[ActorRef]) extends Manager(workerRefs) {

  def workerRefToType: List[(Int, ActorRef)] = workerRefs.map(ref => (workerRefs.indexOf(ref) % workerRefs.size, ref))

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
    // TODO: prediction(idx) jest listą wyników dla workera(idx), klasyfikującego idx % 10
    //  oraz mającą jedynkę jeżeli próbka jest klasy idx % n lub zero jeżeli nie
    ???
  }
}
