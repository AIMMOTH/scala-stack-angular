package scalajs.shared.util

// https://github.com/scala/scala-parser-combinators
import scala.util.parsing.combinator.RegexParsers
import scala.Left
import scala.Right

case class UrlTokens(
    scheme : String,
    authorization : Option[(String, Option[String])],
    domain : Option[String],
    port : Option[Int],
    path : String,
    query : Option[String],
    fragment : Option[String]) extends RegexParsers {

  lazy val splitPath = HelperParser.splitPathBySlash(path)
  lazy val splitQueryToValuePairs = query map (HelperParser.splitIntoValuePairs)
  lazy val splitFragmentToValuePairs = fragment map (HelperParser.splitIntoValuePairs)
}

/**
 * <p>
 * From Wikipedia:
 * <pre>
 * scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
 * </pre>
 * </p>
 *
 * <p>
 * Parsing an URL:
 * </p>
 * <ol>
 *   <li>Split by mandatory scheme, optional domain, mandatory path and then optional query and fragment</li>
 *   <li>If there's a domain split by optional authorization, mandatory domain and optional port</li>
 * <ol>
 * <p>
 * Usually a parser is used by greedy regexp matching until a delimiter.
 * </p>
 *
 * @see http://www.scala-lang.org/files/archive/api/2.11.2/scala-parser-combinators/#scala.util.parsing.combinator.RegexParsers
 */
class UrlParser extends RegexParsers {

  def urlParser = scheme ~ opt(domain) ~ path ~ opt(query) ~ opt(fragment) ^^ {
    case scheme ~ domain ~ path ~ query ~ fragment => domain match {
      case Some((authorization, domain, port)) =>
        Right(new UrlTokens(scheme, authorization, Some(domain), port, path, query, fragment))
      case None =>
        Right(new UrlTokens(scheme, None, None, None, path, query, fragment))
    }
  }

  val notSlash = """[^\/]+""".r
  val notDot = """[^\.]+""".r
  val notColon = """[^:]+""".r
  val notAt = """[^@]+""".r
  val notColonOrSlash = """[^:\/]+""".r
  val numbers = """\d+""".r
  val notQuestionmarkOrHash = """[^\?#]+""".r
  val notHash = """[^\#]*""".r
  val any = """.*""".r

  def scheme = notColon <~ ":"

  def domain = "//" ~> opt(authorization <~ "@") ~ notColonOrSlash ~ opt(":" ~> port) <~ "/" ^^ {
    case optionalAuthorization ~ domains ~ optionalPort => (optionalAuthorization, domains, optionalPort)
  }
  def authorization = notColon ~ opt(":" ~> notAt) ^^ { case user ~ optionalPassword => (user, optionalPassword) }
  def port = numbers ^^ { case number => number.toInt }

  def path = notQuestionmarkOrHash
  def query = "?" ~> notHash
  def fragment = "#" ~> any

  def apply(url : String) = toResult(url, urlParser)
  def applyWith(url : String, parser : Parser[_]) = toResult(url, parser)

  def toResult(text : String, parser : Parser[_]) = {
    parseAll(parser, text) match {
      case Success(result, _) => Right(result)
      case Failure(error, _)  => Left(error)
      case Error(error, _)    => Left(error)
    }
  }
}

object UrlParser {
  lazy val urlParser = new UrlParser

  def apply(url : String) = urlParser(url)
}

/**
 * Parse common patterns.
 * <ul>
 *   <li>Split domain with dot</li>
 *   <li>Split path with slash</li>
 *   <li>Split query or fragment with ampersand and then create value pairs by split by equals</li>
 * </ul>
 */
object HelperParser extends RegexParsers {

  implicit def parserToOption[T](parserResult : ParseResult[T]) = parserResult match {
    case Success(result, _) => Some(result)
    case _                  => None
  }

  val notDot = """[^\.]+""".r
  def domainsByDot = repsep(notDot, ".")
  def splitDomainByDot(domain : String) : Option[List[String]] = parseAll(domainsByDot, domain)

  val notSlash = """[^\/]*""".r
  def pathBySlash = repsep(notSlash, "/")
  def splitPathBySlash(path : String) : Option[List[String]] = parseAll(pathBySlash, path)

  val notEquals = """[^\=]+""".r
  def valuePairs = repsep(pair, "&")
  def pair = notEquals ~ "=" ~ opt(notEquals) ^^ {
    case key ~ equal ~ optionalValue => (key, optionalValue)
  }
  /**
   * Split by ambersand (&) and then equals (=)
   */
  def splitIntoValuePairs(text : String) : Option[List[(String, Option[String])]] = parseAll(valuePairs, text)
}

