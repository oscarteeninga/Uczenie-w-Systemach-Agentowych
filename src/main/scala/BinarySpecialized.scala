import agents.{Director, DirectorSpecialized}
import agents.manager.{BinaryManager, BinarySpecializedManager}
import agents.worker.BinaryWorker
import akka.actor.{ActorSystem, Props}
import command.{BeginSpecialYear, BeginYear}
import network.Data

object BinarySpecialized extends App {
  val system = ActorSystem("school")

  val workers = (1 to 10).toList.map(id => system.actorOf(Props(BinaryWorker(id, id % 10)), "Worker" + id))
  val manager = system.actorOf(Props(BinarySpecializedManager(workers)), "Manager")
  val director = system.actorOf(Props(DirectorSpecialized(manager)), "Director")

  val training = (1 to 1).toList.flatMap(_ => Data.mnistTrainSpecializedDatasets)
  val test = Data.mnistTestDatasets.take(3)

  director ! BeginSpecialYear(training, test)

  Thread.sleep(200000)
  system.terminate()
}
