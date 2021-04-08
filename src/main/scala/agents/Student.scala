package agents

import akka.actor.Actor
import akka.event.Logging
import command.{Exam, Examined, Learn, Learned}
import network.Book
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork

case class Student(id: Int) extends Actor {

  private lazy val network: MultiLayerNetwork = Book.network

  private val log = Logging(context.system, this)

  override def receive: Receive = studying

  def studying: Receive = {
    case Learn(sample) =>
      log.debug("[Studying] Student " + id + " is learn")
      network.fit(sample)
      sender ! Learned
      context become studying
    case Exam(sample) =>
      log.debug("[Studying] Student " + id + " complete exam")
      val predictions = network.predict(sample.getFeatures)
      sender ! Examined(predictions.toList)
      context become studying
    case msg: Any => log.warning("[InLearning] Received unknown message: " + msg.toString)
  }
}
