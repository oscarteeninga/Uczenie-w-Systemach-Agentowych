import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("system")

  val students =
    Students((1 to 10000).toList.map(id => system.actorOf(Props(Student(id)), "Student" + id)))

  val director = system.actorOf(Props(Director(students)), "Director")

  val (training, test) = ((1 to 10).map(new Integer(_)).toList, (1 to 2).map(new Integer(_)).toList)

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
