package ro.jf.stuff.api.currency

import org.springframework.stereotype.Service
import ro.jf.stuff.api.currency.api.model.Currency

@Service
class CurrencyService {
    fun getCurrency(name: String): Currency {
        return Currency(name)
    }
}
