package com.egstep.stm.domain

import kotlin.reflect.full.memberProperties

/*
{
  "mapperFileName": "test/test",
  "namespace": "com.namespace",
  "queryType": "select",
  "id": "select0001",
  "parameterType": "String",
  "resultType": "select1001Out",
 */
data class Sql(
        var mapperFileName: String = "",
        var namespace: String = "",
        var queryType: String = "",
        var id: String = "",
        var parameterType: String?,
        var resultType: String?,
        var query: String = "",
        var sqlFileName: String = ""
) {
    var isValid: Boolean = false
        get() {
            val findFirst = Sql::class.memberProperties.stream()
                    .filter { it.name.matches("id|mapperFileName|namespace|query|queryType".toRegex()) }
                    .filter { it.get(this) == "" }
                    .map { it.name }
                    .findFirst()
            return if (findFirst.isPresent) {
                requiredProperty = findFirst.get()
                false
            } else {
                true
            }
        }
    var requiredProperty: String = ""
}
