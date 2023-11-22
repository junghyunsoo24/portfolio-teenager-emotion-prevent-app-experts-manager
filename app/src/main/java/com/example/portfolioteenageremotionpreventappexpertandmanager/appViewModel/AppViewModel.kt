package com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel private constructor() : ViewModel() {
    private val jwtTokenLiveData = MutableLiveData<String>()

    private val userIdLiveData = MutableLiveData<String>()
    private val userPwdLiveData = MutableLiveData<String>()
    private val userNameLiveData = MutableLiveData<String>()
    private val userEmailLiveData = MutableLiveData<String>()
    private val userInstitutionLiveData = MutableLiveData<String>()

    private val messageListLiveData = MutableLiveData<List<String>>()

    private val userLiveData = MutableLiveData<String>()

    private val teenagerIdLiveData = MutableLiveData<String>()

    private val expertIdLiveData = MutableLiveData<String>()

    private val approveExpertIdLiveData = MutableLiveData<String>()

    private val urlLiveData = MutableLiveData<String>()

    fun setJwtToken(token: String) {
        jwtTokenLiveData.value = token
    }

    fun getJwtToken(): LiveData<String> {
        return jwtTokenLiveData
    }

    fun setUserId(id: String) {
        userIdLiveData.value = id
    }

    fun getUserId(): LiveData<String> {
        return userIdLiveData
    }

    fun setTeenagerId(id: String) {
        teenagerIdLiveData.value = id
    }

    fun getTeenagerId(): LiveData<String> {
        return teenagerIdLiveData
    }

    fun getExpertId(): LiveData<String> {
        return expertIdLiveData
    }

    fun setExpertId(id: String) {
        expertIdLiveData.value = id
    }

    fun removeExpertId(id: String) {
        if (expertIdLiveData.value == id) {
            expertIdLiveData.value = null
        }
    }

    fun getApproveExpertId(): LiveData<String> {
        return approveExpertIdLiveData
    }

    fun setApproveExpertId(id: String) {
        approveExpertIdLiveData.value = id
    }

    fun setUserPwd(pwd: String) {
        userPwdLiveData.value = pwd
    }

    fun getUserPwd(): LiveData<String> {
        return userPwdLiveData
    }

    fun setUserName(name: String) {
        userNameLiveData.value = name
    }

    fun setUserEmail(email: String) {
        userEmailLiveData.value = email
    }

    fun getUserEmail(): LiveData<String> {
        return userEmailLiveData
    }

    fun setUserInstitution(institution: String) {
        userInstitutionLiveData.value = institution
    }

    fun getUserInstitution(): LiveData<String> {
        return userInstitutionLiveData
    }

    fun getUserName(): LiveData<String> {
        return userNameLiveData
    }

    fun setUrl(url: String) {
        urlLiveData.value = url
    }

    fun getUrl(): LiveData<String> {
        return urlLiveData
    }

    fun setMessageList(messages: List<String>) {
        messageListLiveData.value = messages
    }

    fun getMessageList(): LiveData<List<String>> {
        return messageListLiveData
    }

    fun setUser(user: String) {
        userLiveData.value = user
    }

    fun getUser(): LiveData<String> {
        return userLiveData
    }

    companion object {
        private var instance: AppViewModel? = null

        fun getInstance(): AppViewModel {
            if (instance == null) {
                instance = AppViewModel()
            }
            return instance!!
        }
    }
}