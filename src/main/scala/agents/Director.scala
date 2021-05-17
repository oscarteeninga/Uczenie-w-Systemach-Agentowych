package agents

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import command.{BeginExam, BeginYear, ExamEnded, Learn, Teach, Teached}
import org.nd4j.linalg.dataset.DataSet

case class Director(students: ActorRef) extends Actor {

  private val log = Logging(context.system, this)

  override def receive: Receive = manage

  def manage: Receive = {
    case BeginYear(lessons, exams) =>
      log.debug("[Holidays] Begin learning")
      val corrects = exams.map(getCorrectLabels)
      students ! Teach(lessons.head)
      context become learning(exams, lessons, corrects)
    case msg: Any => log.warning("[Manage] Received unknown message: " + msg.toString)
  }

  def learning(exams: List[DataSet], lessons: List[DataSet], corrects: List[List[Int]]): Receive = {
    case Teached =>
      if (lessons.isEmpty) {
        log.debug("[Learning] Learning complete, begin exam")
        students ! BeginExam(exams.head)
        context become examining(exams.tail, Nil, corrects)
      } else {
        students ! Teach(lessons.head)
        context become learning(exams, lessons.tail, corrects)
      }
    case msg: Any => log.warning("[Learning] Received unknown message: " + msg.toString)
  }

  def examining(exams: List[DataSet], answers: List[List[Int]], corrects: List[List[Int]]): Receive = {
    case ExamEnded(answer) =>
      val allAnswers = answers ++ Seq(answer)
      if (exams.isEmpty) {
        log.debug("[Examining] Examining complete, begin holidays")
        printResults(allAnswers, corrects)
        context become manage
      } else {
        students ! BeginExam(exams.head)
        context become examining(exams.tail, allAnswers, corrects)
      }
    case msg: Any => log.warning("[Examining] Received unknown message: " + msg.toString)
  }

  private def getCorrectLabels(dataSet: DataSet): List[Int] = {
    val labels = dataSet.getLabels.toIntMatrix
    labels.map(labelArray => labelArray.toList.indexOf(labelArray.max)).toList
  }

  private def printResults(answers: List[List[Int]], corrects: List[List[Int]]): Unit = {
    for ((correct, answer) <- corrects.zip(answers)) {
      println("Results for " + corrects.indexOf(correct) + " exam")
      println("Correct:\t" + correct.mkString(", "))
      println("Answer:\t\t" + answer.mkString(", "))
      println("Accuracy:\t" + (correct.zip(answer).count{ case (a, b) => a == b }.toDouble / correct.size))
      println()
    }
  }
}
