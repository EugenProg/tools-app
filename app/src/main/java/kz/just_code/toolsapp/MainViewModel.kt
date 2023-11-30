package kz.just_code.toolsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class MainViewModel: ViewModel() {

    val names = NameRepository.names.asLiveData()
}