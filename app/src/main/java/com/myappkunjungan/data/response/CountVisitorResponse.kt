package com.myappkunjungan.data.response

import com.google.gson.annotations.SerializedName

data class CountVisitorResponse(

	@field:SerializedName("data")
	val data: List<CountVisitor>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class CountVisitor(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("month")
	val month: String,

	@field:SerializedName("year")
	val year: String
)
