sealed trait Command

case class Teach(samples: List[Object]) extends Command
case object Teached extends Command

case class Learn(sample: Object) extends Command
case object Learned extends Command

case class Exam(sample: Object) extends Command
case class Examined(prediction: Object) extends Command

sealed trait Manage
case class BeginYear(lessons: List[Object], exams: List[Object]) extends Manage
case object TeachingEnded extends Manage
case class BeginExam(exam: Object) extends Manage
case class ExamEnded(predictions: List[Object]) extends Manage

