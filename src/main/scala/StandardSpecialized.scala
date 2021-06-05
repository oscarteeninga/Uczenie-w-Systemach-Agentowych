import agents.{Director, DirectorSpecialized}
import agents.manager.{StandardManager, StandardSpecializedManager}
import agents.worker.StandardWorker
import akka.actor.{ActorSystem, Props}
import command.BeginSpecialYear
import network.Data

object StandardSpecialized extends App {
  val system = ActorSystem("school")

  val workers = (1 to 10).toList.map(id => system.actorOf(Props(StandardWorker(id)), "Worker" + id))
  val manager = system.actorOf(Props(StandardSpecializedManager(workers)), "Manager")
  val director = system.actorOf(Props(DirectorSpecialized(manager)), "Director")

  val training = (1 to 1).toList.flatMap(_ => Data.mnistTrainSpecializedDatasets)
  val test = Data.mnistTestDatasets.take(5)

  director ! BeginSpecialYear(training, test)

  Thread.sleep(400000)
  system.terminate()
}
