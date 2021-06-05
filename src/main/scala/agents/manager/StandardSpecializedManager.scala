package agents.manager

import akka.actor.ActorRef
import command._
import org.nd4j.linalg.dataset.DataSet

case class StandardSpecializedManager(workerRefs: List[ActorRef]) extends ManagerSpecialized(workerRefs) {
  def workerRefToSpecialization: List[(Int, ActorRef)] = workerRefs.map(ref => (workerRefs.indexOf(ref) % 10, ref))

  def beginLearn(dataSets: List[DataSet]): Unit = {
    workerRefToSpecialization.foreach {
      case (specialization, ref) => ref ! Learn(dataSets(specialization))
    }
  }

  def beginExam(dataSet: DataSet): Unit = {
    workerRefs.foreach(_ ! Exam(dataSet))
  }
}
