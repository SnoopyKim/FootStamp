package project.android.footstamp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.android.footstamp.model.Stamp
import project.android.footstamp.repository.StampRepository
import project.android.footstamp.utils.PostModel

class StampViewModel(private val repository: StampRepository) : ViewModel() {

    val postsLiveData = MutableLiveData<List<PostModel>>()
    val posts: ArrayList<PostModel> = arrayListOf()

    fun loadPosts() {
        repository.getAllFromFirebase?.addOnSuccessListener { list ->
            list.children.forEach {
                val post = it.getValue<PostModel>()
                Log.d("ViewModel", "loadPosts: " + post!!.key)
                posts.add(post!!)
            }
            postsLiveData.value = posts
        }
    }

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
    fun insertItem(area: String, district: String, date: String, image: ByteArray, memo: String) {
        insert(getStampEntry(area, district, date, image, memo))
    }

    fun update(stamp: Stamp) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(stamp)
    }

    fun delete(stamp: Stamp) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(stamp)
    }


    private fun getStampEntry(area: String, district: String, date: String, image: ByteArray, memo: String): Stamp {

        val id = "$area-$district-$date-${allStamps.value?.filter { it.area == area && it.district == district }?.size}"
        return Stamp(
            id,
            area,
            district,
            date,
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