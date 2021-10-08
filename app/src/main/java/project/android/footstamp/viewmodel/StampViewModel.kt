package project.android.footstamp.viewmodel

import androidx.lifecycle.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.android.footstamp.model.Stamp
import project.android.footstamp.repository.StampRepository
import java.text.SimpleDateFormat
import java.util.*

class StampViewModel(private val repository: StampRepository) : ViewModel() {

    val allStamps: LiveData<List<Stamp>> = repository.allStamps.asLiveData()

    fun getStamp(id: Int): LiveData<Stamp> {
        return repository.getStamp(id).asLiveData()
    }

    private fun insert(stamp: Stamp) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(stamp)
    }
    private fun insertAll(stamps: List<Stamp>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(stamps)
    }
    fun insertItem(area: String, district: String, date: Date, image: ByteArray, memo: String) {
        insert(getStampEntry(area, district, date, image, memo))
    }

    fun update(stamp: Stamp) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(stamp)
    }

    fun delete(stamp: Stamp) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(stamp)
    }


    private fun getStampEntry(area: String, district: String, date: Date, image: ByteArray, memo: String): Stamp {
        val strDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(date)
        val id = "$area-$district-$strDate-${allStamps.value?.filter { it.area == area && it.district == district }?.size}"
        return Stamp(
            id,
            area,
            district,
            strDate,
            image,
            memo
        )
    }
}

class StampViewModelFactory(private val repository: StampRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StampViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StampViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}