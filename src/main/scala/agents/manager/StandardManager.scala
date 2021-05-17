package agents.manager

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import command._
import org.nd4j.linalg.dataset.DataSet

case class StandardManager(studentRefs: List[ActorRef]) extends Manager(studentRefs) {

  def beginLearn(dataSet: DataSet): Unit = {
    studentRefs.foreach(_ ! Learn(dataSet))
  }

  def beginExam(dataSet: DataSet): Unit = {
    studentRefs.foreach(_ ! Exam(dataSet))
  }
}
