import akka.actor.{Actor, ActorRef}
import akka.event.Logging

case class Teacher(students: Students, director: ActorRef) extends Actor {
  private val log = Logging(context.system, this)

  override def receive: Receive = holidays

  private var learnedStudentsInLesson: Int = 0

  def holidays: Receive = {
    case Learn(sample: Object) =>
      log.debug("[Teaching] Teaching new sample ")
      students ! (self, Learn(sample))
      context become lesson
    case msg: Any => log.warning("[Holidays] Received unknown message: " + msg.toString)
  }

  def lesson: Receive = {
    case Learned =>
      learnedStudentsInLesson += 1
      if (learnedStudentsInLesson == students.count) {
        log.debug("[Lesson] All students completed teaching")
        director ! Teached
        learnedStudentsInLesson = 0
        context become holidays
      } else {
        context become lesson
      }
    case msg: Any => log.warning("[Lesson] Received unknown message: " + msg.toString)
  }
}
