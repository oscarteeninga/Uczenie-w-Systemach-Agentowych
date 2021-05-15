package agents.students

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

  def vote(predictions: List[List[Int]]): List[Int] = {
    predictions.head.indices.map { i =>
      predictions.indices
        .map(j => predictions(j)(i))
        .groupBy(identity)
        .mapValues(_.size)
        .maxBy(_._2)
        ._1
    }.toList
  }
}
