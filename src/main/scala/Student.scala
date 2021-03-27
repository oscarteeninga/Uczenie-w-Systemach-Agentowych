import akka.actor.{Actor}
import akka.event.Logging

case class Student(id: Int) extends Actor {

  private val log = Logging(context.system, this)

  override def receive: Receive = studying

  def studying: Receive = {
    case Learn(sample) =>
      log.debug("[Studying] Student " + id + " is learning")
      //TODO: Tutaj trzeba stworzyć obsługę skorzystania z sieci
      sender ! Learned
      context become studying
    case Exam(sample) =>
      log.debug("[Studying] Student " + id + " is predicting")
      //TODO: Tutaj trzeba stworzyć obsługę skorzystania z sieci
      sender ! Examined(sample)
      context become studying
    case msg: Any => log.warning("[InLearning] Received unknown message: " + msg.toString)
  }
}
