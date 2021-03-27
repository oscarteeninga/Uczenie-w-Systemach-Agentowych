import akka.actor.{Actor, ActorRef}
import akka.event.Logging

case class Examiner(students: Students, director: ActorRef ) extends Actor {
  private val log = Logging(context.system, this)

  private var predictions = List[Object]()

  override def receive: Receive = holidays

  def holidays: Receive = {
    case BeginExam(exam) =>
      log.debug("[Holidays] Begin new exam")
      students ! (self, Exam(exam))
      context become examination
    case msg: Any => log.warning("[Holidays] Received unknown message: " + msg.toString)
  }

  def examination: Receive = {
    case Examined(sample) =>
      predictions = sample :: predictions
      if (predictions.size == students.count) {
        log.debug("[Examination] All student completed exam")
        director ! ExamEnded(predictions)
        predictions = List[Object]()
        context become holidays
      } else {
        context become examination
      }
    case msg: Any => log.warning("[Examination] Received unknown message: " + msg.toString)
  }
}
