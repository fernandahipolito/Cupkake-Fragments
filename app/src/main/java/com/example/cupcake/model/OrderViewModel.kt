package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


private const val  PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel  : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
        //converte o preço no formato da moeda local
    }

    val dateOptions = getPickupOptions()
    //inicializei a variavel utilizando o método criado

    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
    //atribui os argumentos transmitidos, numberCupcakes à propriedade mutável
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }
    //deixar esses métodos públicos pois serão acessados fora da VM

    //verifica se o sabor foi definido ou não
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Cria uma lista de datas começando com a data atual e os 3 dias seguintes
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE,1)
        }
        return options
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if(dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }
}