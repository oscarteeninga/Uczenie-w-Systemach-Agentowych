package agents.students

import akka.actor.ActorRef
import command.{Exam, Learn}
import org.nd4j.linalg.dataset.DataSet
import util.DataSetManipulator

case class BinaryManager(studentRefs: List[ActorRef]) extends Manager(studentRefs) {

  def studentRefToType: List[(Int, ActorRef)] = studentRefs.map(ref => (studentRefs.indexOf(ref) % studentRefs.size, ref))

  def beginLearn(dataSet: DataSet): Unit = {
    studentRefToType.foreach {
      case (typ, ref) => ref ! Learn(DataSetManipulator.binarize(dataSet, typ))
    }
  }

  def beginExam(dataSet: DataSet): Unit = {
    studentRefToType.foreach {
      case (typ, ref) => ref ! Exam(DataSetManipulator.binarize(dataSet, typ))
    }
  }

  def vote(predictions: List[List[Int]]): List[Int] = {
    ???
  }
}
