package br.com.matheus.reddit.base.extension

import br.com.matheus.reddit.sdk.model.DateType
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

fun DateType.fullDate(pattern: String = "dd MMM yyyy"): String {
    return localDateTime.toLocalDate().format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}