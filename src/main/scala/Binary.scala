import agents.student.BinaryWorker
import agents.Director
import agents.students.BinaryManager
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Data

object Binary extends App {
  val system = ActorSystem("school")

  val studentList = (1 to 5).toList.map(id => system.actorOf(Props(BinaryWorker(id, id % 10)), "Student" + id))
  val students = system.actorOf(Props(BinaryManager(studentList)), "Students")
  val director = system.actorOf(Props(Director(students)), "Director")

  val training = (1 to 10).toList.map(_ => Data.mnistTrain.next())
  val test = (1 to 3).toList.map(_ => Data.mnistTest.next())

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
