package com.evertecinc.processtransaction.core

import com.evertecinc.processtransaction.BuildConfig
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.relation.PurchaseWithTokenCreditCardRelation
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.domain.model.AmountModel
import com.evertecinc.processtransaction.domain.model.ResumeTransactionModel
import com.evertecinc.processtransaction.domain.model.request.*
import java.text.SimpleDateFormat
import java.util.*

val auth = AuthModel(BuildConfig.AUTH, Util.nonceBase64, Util.seed, Util.tranKeyBase64)

fun String.asFormatUser(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    val formatUSer = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateFormat = try {
        formatter.parse(this)
    } catch (e: Exception) {
        "-"
    }
    return formatUSer.format(dateFormat)
}

fun String.capitalize(): String =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.validThuFormat(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val customThuFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
    return try {
        val date = formatter.parse(this)
        customThuFormat.format(date!!)
    } catch (e: Exception) {
        customThuFormat.format(Date())
    }
}

fun List<Int>.asString(): String {
    val append = StringBuilder()
    for (i in this.indices) {
        append.append(this[i])
        append.append(",")
    }
    return append.toString().substring(0, append.length - 1)
}

fun String.asList(): List<String> {
    val installments = this.split(",")
    return installments.toList()
}

fun String.formatCreditCard(): String {
    val digits = this.toCharArray()
    var split = 0
    val format = StringBuilder()
    for (i in digits.indices) {
        if (split == 3) {
            format.append(digits[i]).append(" ")
            split = 0
        } else {
            format.append(digits[i])
            split++
        }
    }
    return format.toString()
}

fun PurchaseWithTokenCreditCardRelation.buildInfoCard(): CreditCardInformationReqModel {
    val payment = PaymentModel(
        amount = AmountModel("COP", this.productEntity.totalAmount),
        description = this.productEntity.description,
        reference = this.productEntity.reference
    )
    val instrument = Instrument(
        card = CardModel(
            number = this.tokenCreditCard!!.digits
        )
    )
    return CreditCardInformationReqModel(auth = auth, instrument = instrument, payment = payment)
}

fun TransactionWithDetailsRelation.toResumeTransactionModel(): ResumeTransactionModel {
    return ResumeTransactionModel(
        this.productEntity.reference,
        this.productEntity.franchise,
        this.productEntity.date,
        this.productEntity.status,
        this.details[0].name,
        this.details[0].address,
        this.details[0].reference,
        this.details[0].quantity,
        this.details[0].totalAmount,
        this.productEntity.lastDigits
    )
}

fun TokenCreditCardEntity.buildInfoCard(): CreditCardInformationReqModel {
    val payment = PaymentModel(
        amount = AmountModel("COP", 0.0),
        description = "",
        reference = ""
    )
    val instrument = Instrument(
        card = CardModel(
            number = this.digits
        )
    )
    return CreditCardInformationReqModel(auth = auth, instrument = instrument, payment = payment)
}

object Util {
    val nonce = Commons.getRandom()
    val seed = Commons.getCurrentDateFormat()
    val nonceBase64 = Commons.getBase64(nonce.toByteArray())
    val tranKeyBase64 = Commons.getBase64(Commons.getSHA256(nonce + seed + BuildConfig.TRAN_KEY))

    private const val ptVisa = "^4[0-9]{6,}$"
    private const val ptMasterCard = "^5[1-5][0-9]{5,}$"
    private const val ptAmeExp = "^3[47][0-9]{5,}$"
    private const val ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$"

    fun getImageCreditCard(value: String): Int {
        return when {
            ptVisa.toRegex().matches(value) -> R.drawable.ic_visa
            ptMasterCard.toRegex().matches(value) -> R.drawable.ic_master_card
            ptAmeExp.toRegex().matches(value) -> R.drawable.ic_american_express
            ptDinClb.toRegex().matches(value) -> R.drawable.ic_diners_club
            value.startsWith("59") -> R.drawable.ic_enel_codensa
            value.startsWith("63") -> R.drawable.ic_ris
            else -> R.drawable.ic_bbva
        }
    }

    fun getImageFromFranchise(franchise: String): Int {
        return when (franchise) {
            Constants.VISA -> R.drawable.ic_visa
            Constants.VISA_ELECTRON -> R.drawable.ic_visa_electron
            Constants.MASTER -> R.drawable.ic_master_card
            Constants.CODENSA -> R.drawable.ic_enel_codensa
            Constants.DINERS -> R.drawable.ic_diners_club
            Constants.RIS -> R.drawable.ic_ris
            Constants.AMEX -> R.drawable.ic_american_express
            else -> R.drawable.ic_bbva
        }
    }

    fun getBackgroundFromFranchise(franchise: String): Int {
        return when (franchise) {
            Constants.VISA, Constants.VISA_ELECTRON -> R.drawable.ic_credit_card_visa
            Constants.MASTER -> R.drawable.ic_credit_card_master_card
            Constants.CODENSA -> R.drawable.ic_credit_card_enel_codensa
            else -> R.drawable.ic_credit_card_common
        }
    }

    fun empty(): TokenCreditCardEntity =
        TokenCreditCardEntity(0, "", "", "", "", "", "", "", "")

}