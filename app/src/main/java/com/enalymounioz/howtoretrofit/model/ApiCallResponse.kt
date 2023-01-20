package com.enalymounioz.howtoretrofit.model

import com.google.gson.annotations.SerializedName

data class ApiCallResponse (
    val method: String?,
    val query: Map<String, String>?,
    @SerializedName("headers")
    val heads: Map<String, String>?,
    val body: Map<String, String>?
        ){
    fun flatten(): List<Item>{
        val flatpack = arrayListOf<Item>()
        //if we have sth in method then
        method?.let{
            flatpack.add(Item("method", method, TYPE_ITEM))}
        //Need to convert map into a structure
        query?.let{
            //if this is not empty
            if(!query.values.isEmpty()){
                flatpack.add(Item("query", "", TYPE_CATEGORY))
                addMapItems(query, flatpack)
            }

            body?.let{
                if(!body.values.isEmpty()){
                    flatpack.add(Item("body", "", TYPE_CATEGORY))
                    addMapItems(body, flatpack)
            }            }
        }
        heads?.let{
            if(!heads.values.isEmpty()){
                flatpack.add(Item("headers","", TYPE_CATEGORY))
                addMapItems(heads, flatpack)
            }
        }
        return flatpack
    }
    private fun addMapItems(map: Map<String, String>, flatpack: ArrayList<Item>){
        for (key in map.keys){
            flatpack.add(Item(key, map.getValue(key), TYPE_ITEM))
        }
    }
}