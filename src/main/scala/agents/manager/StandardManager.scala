package agents.manager

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import command._
import org.nd4j.linalg.dataset.DataSet

case class StandardManager(workerRefs: List[ActorRef]) extends Manager(workerRefs) {

  def beginLearn(dataSet: DataSet): Unit = {
    workerRefs.foreach(_ ! Learn(dataSet))
  }

  def beginExam(dataSet: DataSet): Unit = {
    workerRefs.foreach(_ ! Exam(dataSet))
  }
}
