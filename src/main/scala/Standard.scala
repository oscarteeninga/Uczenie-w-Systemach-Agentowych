import agents.worker.StandardWorker
import agents.Director
import agents.manager.StandardManager
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Data

object Standard extends App {
  val system = ActorSystem("school")

  val workers = (1 to 100).toList.map(id => system.actorOf(Props(StandardWorker(id)), "Worker" + id))
  val manager = system.actorOf(Props(StandardManager(workers)), "Manager")
  val director = system.actorOf(Props(Director(manager)), "Director")

  val training = (1 to 10).toList.flatMap(_ => Data.mnistTrainDatasets)
  val test = Data.mnistTestDatasets.take(5)

  director ! BeginYear(training, test)

  Thread.sleep(400000)
  system.terminate()
}
