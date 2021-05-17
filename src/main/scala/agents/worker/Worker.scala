package agents.worker

import akka.actor.Actor
import akka.event.Logging
import command.{Exam, Examined, Learn, Learned}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.dataset.DataSet

trait Worker extends Actor {

  protected val log = Logging(context.system, this)

  override def receive: Receive = studying

  def studying: Receive = {
    case Learn(sample) =>
      log.debug("[Learning] Worker " + id + " is learn")
      fit(sample)
      sender ! Learned
      context become studying
    case Exam(sample) =>
      log.debug("[Examining] Worker " + id + " complete exam")
      val predictions = predict(sample)
      sender ! Examined(predictions.toList)
      context become studying
    case msg: Any => log.warning("[Worker] Received unknown message: " + msg.toString)
  }

  def id: Int
  def network: MultiLayerNetwork
  def fit(sample: DataSet): Unit
  def predict(sample: DataSet): Array[Int]
}
