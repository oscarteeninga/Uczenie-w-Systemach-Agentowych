import agents.{Director, Student, Students}
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Book

object School extends App {
  val system = ActorSystem("school")

  val students =
    Students((1 to 5).toList.map(id => system.actorOf(Props(Student(id)), "Student" + id)))

  val director = system.actorOf(Props(Director(students)), "Director")

  val training = (1 to 10).toList.map(_ => Book.mnistTrain.next())
  val test = (1 to 3).toList.map(_ => Book.mnistTest.next())

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
