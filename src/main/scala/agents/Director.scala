package agents

import akka.actor.{Actor, Props}
import akka.event.Logging
import command.{BeginExam, BeginYear, ExamEnded, Learn, Teached}

case class Director(students: Students) extends Actor {

  private val log = Logging(context.system, this)

  private val teacher = context.system.actorOf(Props(Teacher(students, self)), "Teacher")
  private val examiner = context.system.actorOf(Props(Examiner(students, self)), "Examiner")

  override def receive: Receive = manage

  private var lessons = List[Object]()
  private var exams = List[Object]()

  private var results = List[List[Object]]()

  def manage: Receive = {
    case BeginYear(training, test) =>
      log.debug("[Holidays] Begin learning")
      exams = test
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
      results = predictions :: results
      if (exams.isEmpty) {
        log.debug("[Examining] Examining complete, begin holidays")
        context become manage
      }
      else {
        examiner ! BeginExam(exams.head)
        exams = exams.tail
        context become examining
      }
    case msg: Any => log.warning("[Examining] Received unknown message: " + msg.toString)
  }
}
