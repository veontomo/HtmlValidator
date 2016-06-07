/**
 * Created by Andrey on 07/06/2016.
 */
class AsciiValidator {
    public val content: String;

    constructor(text: String) {
        content = text;
    }

    fun isValid(): Boolean {
        return content.toCharArray().map { x -> x.toInt() }.all {
            x ->
            x >= 32 && x <= 126
        }

    }
}