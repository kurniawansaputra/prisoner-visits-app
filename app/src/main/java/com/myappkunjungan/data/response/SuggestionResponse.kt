package com.myappkunjungan.data.response

import com.google.gson.annotations.SerializedName

data class SuggestionResponse(

	@field:SerializedName("data")
	val data: List<Suggestion>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Suggestion(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
