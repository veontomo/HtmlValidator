/**
 * Created by Andrey on 07/06/2016.
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

fun main(args: Array<String>) {
    val c = Validator();
    print(c.sum(2, -1));
    val doc = Jsoup.connect("http://en.wikipedia.org/").get();
    val newsHeadlines = doc.select("#mp-itn b a");
}

