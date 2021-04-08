package command

import org.nd4j.linalg.dataset.DataSet

sealed trait Command

case object Teached extends Command

case class Learn(sample: DataSet) extends Command
case object Learned extends Command

case class Exam(sample: DataSet) extends Command
case class Examined(predictions: List[Int]) extends Command

sealed trait Manage
case class BeginYear(lessons: List[DataSet], exams: List[DataSet]) extends Manage
case object TeachingEnded extends Manage
case class BeginExam(exam: DataSet) extends Manage
case class ExamEnded(predictions: List[List[Int]]) extends Manage

