import agents.student.StandardStudent
import agents.Director
import agents.students.StandardStudents
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Book

object StandardSchool extends App {
  val system = ActorSystem("school")

  val studentList = (1 to 5).toList.map(id => system.actorOf(Props(StandardStudent(id)), "Student" + id))
  val students = system.actorOf(Props(StandardStudents(studentList)), "Students")
  val director = system.actorOf(Props(Director(students)), "Director")

  val training = (1 to 10).toList.flatMap(_ => Book.mnistTrainDatasets)
  val test = Book.mnistTestDatasets.take(3)

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
