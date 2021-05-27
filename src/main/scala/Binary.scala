import agents.worker.BinaryWorker
import agents.Director
import agents.manager.BinaryManager
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Data

object Binary extends App {
  val system = ActorSystem("school")

  val workers = (1 to 100).toList.map(id => system.actorOf(Props(BinaryWorker(id, id % 10)), "Worker" + id))
  val manager = system.actorOf(Props(BinaryManager(workers)), "Manager")
  val director = system.actorOf(Props(Director(manager)), "Director")

  val training = (1 to 1).toList.flatMap(_ => Data.mnistTrainDatasets)
  val test = Data.mnistTestDatasets.take(3)

  director ! BeginYear(training, test)

  Thread.sleep(200000)
  system.terminate()
}
