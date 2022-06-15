package com.example.simplenotepad.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotepad.room.CategoryEntity
import com.example.simplenotepad.room.NoteDatabase
import com.example.simplenotepad.room.NoteEntity
import com.example.simplenotepad.room.NoteWithCategories
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {

    private lateinit var repository: NoteRepository

    val noteEntitiesLiveData = MutableLiveData<List<NoteEntity>>()
    val categoryEntitiesLiveData = MutableLiveData<List<CategoryEntity>>()
    val notesWithCategoriesLiveData = MutableLiveData<List<NoteWithCategories>>()

    val transactionCompleteListener = MutableLiveData<Event<Boolean>>()
    var onSelectionModeEnable = MutableLiveData<Boolean>()

    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData: LiveData<CategoriesViewState>
        get() = _categoriesViewStateLiveData

    private val _homeViewStateLiveData = MutableLiveData<HomeViewState>()
    val homeViewStateLiveData: LiveData<HomeViewState>
        get() = _homeViewStateLiveData



    fun init(noteDatabase: NoteDatabase){
        repository = NoteRepository(noteDatabase)

        viewModelScope.launch {
            repository.getNotes().collect { items ->
                noteEntitiesLiveData.postValue(items)
            }
        }

        viewModelScope.launch {
            repository.getAllCategories().collect { items ->
                categoryEntitiesLiveData.postValue(items)
            }
        }

        viewModelScope.launch {
            repository.getAllNotesWithCategories().collect { items ->
                notesWithCategoriesLiveData.postValue(items)
                updateHomeViewState(items)
            }
        }

    }

    //Sorting HomeViewState
    var currentSort : HomeViewState.Sort = HomeViewState.Sort.NEWEST
        set(value) {
            field = value
            updateHomeViewState(notesWithCategoriesLiveData.value!!)
        }

    //Sorting HomeViewState by Category
    var sortByCategoryId : String = CategoryEntity.DEFAULT_CATEGORY_ID
        set(value) {
            field = value
            updateHomeViewState(notesWithCategoriesLiveData.value!!)
        }

    private fun updateHomeViewState(dataList : List<NoteWithCategories>){

        var modifiedDataList = ArrayList<NoteWithCategories>()
        val viewStateItemList = ArrayList<HomeViewState.Data<*>>()

        //Observe note entities with specified category else observe all note entities
        if(sortByCategoryId != CategoryEntity.DEFAULT_CATEGORY_ID){
            dataList.forEach {
                if(it.noteEntity.categoryId == sortByCategoryId){
                    modifiedDataList.add(it)
                }
            }
        } else { modifiedDataList = dataList as ArrayList<NoteWithCategories> }


        //Sort the modified data
        when(currentSort){
            HomeViewState.Sort.TITLE -> {
                //adding header
                val headerItem = HomeViewState.Data(
                    data = HomeViewState.Sort.TITLE.name,
                    isHeader = true
                )
                viewStateItemList.add(headerItem)

                //populate with data
                modifiedDataList.sortedBy {
                    it.noteEntity.title
                }.forEach {
                    val dataItem = HomeViewState.Data(data = it)
                    viewStateItemList.add(dataItem)
                }

            }

            HomeViewState.Sort.NEWEST -> {
                val headerItem = HomeViewState.Data(
                    data = HomeViewState.Sort.OLDEST.name,
                    isHeader = true
                )
                viewStateItemList.add(headerItem)

                modifiedDataList.sortedByDescending {
                    it.noteEntity.dateCreated
                }.forEach {
                    val dataItem = HomeViewState.Data(data = it)
                    viewStateItemList.add(dataItem)
                }
            }

            HomeViewState.Sort.OLDEST ->{
                val headerItem = HomeViewState.Data(
                    data = HomeViewState.Sort.NEWEST.name,
                    isHeader = true
                )

                viewStateItemList.add(headerItem)

                modifiedDataList.sortedBy {
                    it.noteEntity.dateCreated
                }.forEach {
                    val dataItem = HomeViewState.Data(data = it)
                    viewStateItemList.add(dataItem)
                }
            }
        }

        _homeViewStateLiveData.postValue(
            HomeViewState(
                isLoading = false,
                dataList = viewStateItemList,
                sort = currentSort
            )
        )

    }


    data class HomeViewState(
        val isLoading: Boolean = false,
        val dataList : List<Data<*>> = emptyList(),
        val sort : Sort = Sort.NEWEST,
    )
    {
        data class Data<T>(
            val data: T,
            val isHeader: Boolean = false
        )

        enum class Sort(){
            NEWEST,
            OLDEST,
            TITLE
        }
    }


    fun onCategorySelected(categoryId: String, showLoading: Boolean = false) {

        if (showLoading) {
            val loadingViewState = CategoriesViewState(isLoading = true)
            _categoriesViewStateLiveData.value = loadingViewState
        }

        val categories = categoryEntitiesLiveData.value ?: return
        val viewStateItemList = ArrayList<CategoriesViewState.CategoryItem>()

        //unselecting category
                viewStateItemList.add(
                    CategoriesViewState.CategoryItem(
                        categoryEntity = CategoryEntity.getDefaultCategory(),
                        isSelected = CategoryEntity.DEFAULT_CATEGORY_ID == categoryId
                    )
                )

        // populate the rest with what we have
            categories.forEach {
                viewStateItemList.add(
                    CategoriesViewState.CategoryItem(
                        categoryEntity = it,
                        isSelected = it.id == categoryId
                    )
                )
            }

        val viewState = CategoriesViewState(itemList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)
    }


    data class CategoriesViewState(
        val isLoading : Boolean = false,
        val itemList: List<CategoryItem> = emptyList(),
        ){
            data class CategoryItem(
            val categoryEntity: CategoryEntity = CategoryEntity(),
            val isSelected : Boolean = false
            )

        fun getSelectedCategoryId(): String {
            return itemList.find { it.isSelected }?.categoryEntity?.id ?:
            CategoryEntity.DEFAULT_CATEGORY_ID
        }
    }

    //region note entity
    fun addNote(note: NoteEntity) = viewModelScope.launch {
        repository.addNote(note)
        transactionCompleteListener.postValue(Event(true))
    }

    fun deleteNotes(listOfNoteEntity: ArrayList<String>) = viewModelScope.launch {
        repository.deleteNotes(listOfNoteEntity)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch {
        repository.updateNote(note)
    }
    //endregion note entity

    //region category entity

    fun addCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.addCategory(category)
        transactionCompleteListener.postValue(Event(true))
    }

    fun deleteCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.deleteCategory(category)
    }

    fun updateCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.updateCategory(category)
    }

    //endregion category entity
}