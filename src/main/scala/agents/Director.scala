package agents

import akka.actor.{Actor, Props}
import akka.event.Logging
import command.{BeginExam, BeginYear, ExamEnded, Learn, Teached}
import org.nd4j.linalg.dataset.DataSet

case class Director(students: Students) extends Actor {

  private val log = Logging(context.system, this)

  private val teacher = context.system.actorOf(Props(Teacher(students, self)), "Teacher")
  private val examiner = context.system.actorOf(Props(Examiner(students, self)), "Examiner")

  override def receive: Receive = manage

  private var lessons: List[DataSet] = Nil
  private var exams: List[DataSet] = Nil

  private var answers: List[List[List[Int]]] = Nil
  private var corrects: List[List[Int]] = Nil

  def manage: Receive = {
    case BeginYear(training, test) =>
      log.debug("[Holidays] Begin learning")
      exams = test
      corrects = test.map(getCorrectLabels)
      lessons = training.tail
      teacher ! Learn(training.head)
      context become learning
    case msg: Any => log.warning("[command.Manage] Received unknown message: " + msg.toString)
  }

  def learning: Receive = {
    case Teached =>
      if (lessons.isEmpty) {
        log.debug("[Learning] Learning complete, begin exam")
        examiner ! BeginExam(exams.head)
        exams = exams.tail
        context become examining
      } else {
        teacher ! Learn(lessons.head)
        lessons = lessons.tail
        context become learning
      }
    case msg: Any => log.warning("[Learning] Received unknown message: " + msg.toString)
  }

  def examining: Receive = {
    case ExamEnded(predictions) if sender.equals(examiner) =>
      answers = predictions :: answers
      if (exams.isEmpty) {
        log.debug("[Examining] Examining complete, begin holidays")
        printResults()
        context become manage
      } else {
        examiner ! BeginExam(exams.head)
        exams = exams.tail
        context become examining
      }
    case msg: Any => log.warning("[Examining] Received unknown message: " + msg.toString)
  }

  private def getCorrectLabels(dataSet: DataSet): List[Int] = {
    val labels = dataSet.getLabels.toIntMatrix
    labels.map(labelArray => labelArray.toList.indexOf(labelArray.max)).toList
  }

  private def printResults(): Unit = {
    for ((correct, answer) <- corrects.zip(answers)) {
      println("Results for " + corrects.indexOf(correct) + " exam")
      println("Correct:\t\t" + correct.mkString(", "))
      println("Answers: ")
      for (answerOne <- answer) {
        println("\tStudent " + answer.indexOf(answerOne) + ":\t" + answerOne.mkString(", "))
      }
      println()
    }
  }
}
