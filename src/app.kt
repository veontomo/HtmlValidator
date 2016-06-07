/**
 * Created by Andrey on 07/06/2016.
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

fun main(args: Array<String>) {
    val text = "<!DOCTYPE HTML><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"><title>Agent321 News</title></head><body><div style=\"background-color: #ffffff; color: #000001; line-height: normal; text-align: center; font-size: 13px; font-family: Arial, sans-serif; font-style: normal; font-weight: normal; width: 500px;\"><table style=\"border-style: none; padding: 0px; margin: 0px; width: 500px; border-spacing: 0px 0px; font-size: 13px; text-align: justify; font-family: Arial, sans-serif; background-color: #ffffff; max-width: 500px;min-width: 500px;\" width=\"500\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr width=\"500\" style=\"border-style: none; width: 500px; padding: 0px; margin: 0px; max-width: 500px; min-width: 500px;\"><td style=\"border-style: none; width: 500px; padding: 0px; margin: 0px; vertical-align: bottom; color: #000001; text-align: justify; max-width: 500px; min-width: 500px;font-size:12px;font-family: Arial, sans-serif;font-style: normal; font-weight: normal; width: 500px;\" width=\"500\"><img src=\"http://adv.networkagenti.it/images/forumagenti/aziende/milano2016/agentispagna/footer.jpg\" alt=\"\" border=\"0\" width=\"500\" height=\"303\" style=\"border-style: none; width: 500px; padding: 0px; margin: 0px; height 303px;display:block;\" /></td></tr><tr width=\"500\" style=\"border-style: none; width: 500px; padding: 0px; margin: 0px; max-width: 500px; min-width: 500px;\"><td style=\"border-style: none; width: 500px; padding-top: 6px; margin: 0px; vertical-align: top; color: #000001; text-align: justify; max-width: 500px; min-width: 500px;\" width=\"500\"><div style=\"border-style: none;font-size: 10px; font-family: Arial, sans-serif; font-weight: normal; font-style: normal;color: #808080;\">Partecipa a Forum Agenti Milano ed incontra migliaia di Agenti di Commercio Italiani e, grazie a questa iniziativa speciale, anche Agenti di Commercio Spagnoli. Se la Tua Azienda &egrave; alla ricerca di nuovi Agentidi Commercio chiama il Numero Verde 800.86.16.16 e richiedi maggiori informazioni per prenotare il Tuo Stand a Forum Agenti Milano. Se non desideri ricevere ulteriori mail da Forum Agenti Milano, puoi rimuovertirispondendo &ldquo;rimuovimi&rdquo; a questa email.</div></td></tr></tbody></table></div></center></body></html>";
    val doc = Jsoup.parse(text);
    val ascii = AsciiValidator(text);
    print(ascii.isValid());
    print(doc.charset());
}

