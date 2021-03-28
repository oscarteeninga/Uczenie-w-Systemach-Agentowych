import agents.{Director, Student, Students}
import akka.actor.{ActorSystem, Props}
import command.BeginYear

object School extends App {
  val system = ActorSystem("school")

  val students =
    Students((1 to 5).toList.map(id => system.actorOf(Props(Student(id)), "Student" + id)))

  val director = system.actorOf(Props(Director(students)), "Director")

  val (training, test) = ((1 to 10).map(new Integer(_)).toList, (1 to 2).map(new Integer(_)).toList)

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
