import agents.student.StandardWorker
import agents.Director
import agents.students.StandardManager
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Data

object Standard extends App {
  val system = ActorSystem("school")

  val studentList = (1 to 5).toList.map(id => system.actorOf(Props(StandardWorker(id)), "Student" + id))
  val students = system.actorOf(Props(StandardManager(studentList)), "Students")
  val director = system.actorOf(Props(Director(students)), "Director")

  val training = (1 to 10).toList.flatMap(_ => Data.mnistTrainDatasets)
  val test = Data.mnistTestDatasets.take(3)

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
