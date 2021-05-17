package agents.manager

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import command.{BeginExam, ExamEnded, Examined, Learned, Teach, Teached}
import org.nd4j.linalg.dataset.DataSet

abstract class Manager(studentRefs: List[ActorRef]) extends Actor {

  def beginLearn(dataSet: DataSet): Unit

  def beginExam(dataSet: DataSet): Unit

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

  protected val log = Logging(context.system, this)

  override def receive: Receive = holidays

  def holidays: Receive = {
    case Teach(sample) =>
      log.debug("[Teaching] Teaching new sample ")
      beginLearn(sample)
      context become lesson(0, sender())
    case BeginExam(exam) =>
      log.debug("[Holidays] Begin new exam")
      beginExam(exam)
      context become examination(Nil, sender())
    case msg: Any => log.warning("[Holidays] Received unknown message: " + msg.toString)
  }

  def lesson(learned: Int, director: ActorRef): Receive = {
    case Learned =>
      if (learned + 1 == studentRefs.size) {
        log.debug("[Lesson] All workers completed teaching")
        director ! Teached
        context become holidays
      } else {
        context become lesson(learned + 1, director)
      }
    case msg: Any => log.warning("[Lesson] Received unknown message: " + msg.toString)
  }

  def examination(predictions: List[List[Int]], director: ActorRef): Receive = {
    case Examined(prediction) =>
      val allPredictions = predictions ++ Seq(prediction)
      if (allPredictions.size == studentRefs.size) {
        log.debug("[Examination] All workers completed exam")
        director ! ExamEnded(vote(allPredictions))
        context become holidays
      } else {
        context become examination(allPredictions, director)
      }
    case msg: Any => log.warning("[Examination] Received unknown message: " + msg.toString)
  }

}
